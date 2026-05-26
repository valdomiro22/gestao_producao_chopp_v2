package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.repository

import android.util.Log
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.mapper.toEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.mapper.toLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.localdatasource.ProducaoLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.mapper.toRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.remotedatasource.ProducaoRemoteDatasource
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import com.santos.valdomiro.gestaoproducaochopp.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class ProducaoRepositoryImpl @Inject constructor(
    private val localDataSource: ProducaoLocalDataSource,
    private val remoteDataSource: ProducaoRemoteDatasource,
) : ProducaoRepository {

    override suspend fun insertProducao(producao: ProducaoEntity): Result<Unit> {
        return try {
            val producaoPendente = producao.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO
            )

            localDataSource.insertProducao(producaoPendente.toLocalModel())

            try {
                remoteDataSource.insertProducao(
                    producaoPendente.toRemoteModel()
                )

                localDataSource.updateStatusSincronizacao(
                    producaoId = producaoPendente.id,
                    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                )
            } catch (e: Exception) {
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProducao(producao: ProducaoEntity): Result<Unit> {
        return try {
            val producaoPendente = producao.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
            )
            localDataSource.updateProducao(producaoPendente.toLocalModel())

            try {
                remoteDataSource.updateProducao(
                    id = producaoPendente.id,
                    producao = producaoPendente.toRemoteModel()
                )

                localDataSource.updateStatusSincronizacao(
                    producaoId = producaoPendente.id,
                    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                )
            } catch (e: Exception) {
                Log.d(
                    TAG,
                    "updateProducao: Erro ao enviar atualização da producao para o servidor. Producao atualizada localmente com status AGUARDANDO_ENVIO. Detalhes do erro: ${e.message}"
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStatusSincronizacao(
        producaoId: String,
        statusSincronizacao: StatusSincronizacao
    ): Result<Unit> {
        return try {
            localDataSource.updateStatusSincronizacao(
                producaoId = producaoId,
                statusSincronizacao = statusSincronizacao
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProducao(producao: ProducaoEntity): Result<Unit> {
        return try {
            val producaoParaExcluir = producao.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_EXCLUSAO
            )

            // 1. Marca como aguardando exclusão no Room
            localDataSource.updateProducao(
                producao = producaoParaExcluir.toLocalModel()
            )

            try {
                // 2. Apaga no Firestore
                remoteDataSource.deleteProducao(id = producao.id)

                // 3. Só depois que apagou no Firestore, apaga definitivamente no Room
                localDataSource.deleteProducao(
                    producao = producaoParaExcluir.toLocalModel()
                )

            } catch (e: Exception) {
                Log.d(
                    TAG,
                    "deleteProducao: Erro ao deletar produção no servidor. Produção marcada como AGUARDANDO_EXCLUSAO. Detalhes: ${e.message}"
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOneById(producaoId: String): Flow<ProducaoEntity?> {
        return localDataSource.getOneById(producaoId = producaoId)
            .map { producao ->
                if (producao == null) {
                    null
                } else if (producao.statusSincronizacao == StatusSincronizacao.AGUARDANDO_EXCLUSAO) {
                    null
                } else {
                    producao.toEntity()
                }
            }
    }

    override fun getAllProducoes(): Flow<List<ProducaoEntity>> {
        return localDataSource.getAllProducoes()
            .map { listaProducoes ->
                listaProducoes.filter { producao ->
                    producao.statusSincronizacao != StatusSincronizacao.AGUARDANDO_EXCLUSAO
                }.map { producao ->
                    producao.toEntity()
                }
            }
    }

    override fun getAllProducoesDaGrade(gradeId: String): Flow<List<ProducaoEntity>> {
        return localDataSource.getAllProducoesDaGrade(gradeId = gradeId)
            .map { listaProducoes ->
                listaProducoes.filter { producao ->
                    producao.statusSincronizacao != StatusSincronizacao.AGUARDANDO_EXCLUSAO
                }.map { producao ->
                    producao.toEntity()
                }
            }
    }

    override suspend fun sincronizarProducoesDoRemoto(): Result<Unit> {
        return try {
            val producoesRemotas = remoteDataSource.getAllProducoes()

            val producoesLocais = producoesRemotas.map { producaoRemote ->
                producaoRemote.toLocalModel()
            }

            localDataSource.insertAllProducoes(producoesLocais)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}