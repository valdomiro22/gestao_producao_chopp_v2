package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.repository

import android.util.Log
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.localdatasource.MovimentacaoLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.mapper.toEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.mapper.toLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.mapper.toRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.remotedatasource.MovimentacaoRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import com.santos.valdomiro.gestaoproducaochopp.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovimentacaoRepositoryImpl @Inject constructor(
    private val localDataSource: MovimentacaoLocalDataSource,
    private val remoteDataSource: MovimentacaoRemoteDataSource
) : MovimentacaoRepository {

    override suspend fun insertMovimentacao(
        movimentacao: MovimentacaoEntity
    ): Result<Unit> {
        return try {
            val movimentacaoPendente = movimentacao.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO
            )

            localDataSource.insertMovimentacao(
                movimentacaoPendente.toLocalModel()
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMovimentacao(
        movimentacao: MovimentacaoEntity
    ): Result<Unit> {
        return try {
            val movimentacaoPendente = movimentacao.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
            )

            localDataSource.updateMovimentacao(
                movimentacaoPendente.toLocalModel()
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStatusSincronizacao(
        movimentacaoId: String,
        statusSincronizacao: StatusSincronizacao
    ): Result<Unit> {
        return try {
            localDataSource.updateStatusSincronizacao(
                movimentacaoId = movimentacaoId,
                statusSincronizacao = statusSincronizacao
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMovimentacao(
        movimentacao: MovimentacaoEntity
    ): Result<Unit> {
        return try {
            val movimentacaoParaExcluir = movimentacao.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_EXCLUSAO
            )

            localDataSource.updateMovimentacao(
                movimentacaoParaExcluir.toLocalModel()
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOneById(movimentacaoId: String): Flow<MovimentacaoEntity?> {
        return localDataSource.getOneById(movimentacaoId)
            .map { barril ->
                if (barril == null) {
                    null
                } else if (barril.statusSincronizacao == StatusSincronizacao.AGUARDANDO_EXCLUSAO) {
                    null
                } else {
                    barril.toEntity()
                }
            }
    }

    override fun getAllMovimentacoes(): Flow<List<MovimentacaoEntity>> {
        return localDataSource.getAllMovimentacoes()
            .map { listaMovimentacao ->
                listaMovimentacao
                    .filter { movimentacaoLocal ->
                        movimentacaoLocal.statusSincronizacao != StatusSincronizacao.AGUARDANDO_EXCLUSAO
                    }
                    .map { barrilLocal ->
                        barrilLocal.toEntity()
                    }
            }
    }

    override fun getAllOfProducao(producaoId: String): Flow<List<MovimentacaoEntity>> {
        return localDataSource.getAllOfProducao(producaoId = producaoId)
            .map { listaMovimentacao ->
                listaMovimentacao
                    .filter { movimentacaoLocal ->
                        movimentacaoLocal.statusSincronizacao != StatusSincronizacao.AGUARDANDO_EXCLUSAO
                    }
                    .map { barrilLocal ->
                        barrilLocal.toEntity()
                    }
            }
    }

    override suspend fun sincronizarMovimentacoesPendentes(): Result<Unit> {
        return try {
            val aguardandoEnvio = localDataSource.getMovimentacoesAguardandoEnvio()

            aguardandoEnvio.forEach { movimentacaoLocal ->
                try {
                    val entity = movimentacaoLocal.toEntity()

                    remoteDataSource.insertMovimentacao(
                        entity.toRemoteModel()
                    )

                    localDataSource.updateStatusSincronizacao(
                        movimentacaoId = entity.id,
                        statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                    )
                } catch (e: Exception) {
                    // mantém AGUARDANDO_ENVIO
                }
            }

            val aguardandoAtualizacao =
                localDataSource.getMovimentacoesAguardandoAtualizacao()

            aguardandoAtualizacao.forEach { movimentacaoLocal ->
                try {
                    val entity = movimentacaoLocal.toEntity()

                    remoteDataSource.updateMovimentacao(
                        movimentacao = entity.toRemoteModel()
                    )

                    localDataSource.updateStatusSincronizacao(
                        movimentacaoId = entity.id,
                        statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                    )
                } catch (e: Exception) {
                    // mantém AGUARDANDO_ATUALIZACAO
                }
            }

            val aguardandoExclusao =
                localDataSource.getMovimentacoesAguardandoExclusao()

            aguardandoExclusao.forEach { movimentacaoLocal ->
                try {
                    val entity = movimentacaoLocal.toEntity()

                    remoteDataSource.deleteMovimentacao(
                        movimentacaoId = entity.id
                    )

                    localDataSource.deleteMovimentacao(
                        movimentacao = movimentacaoLocal
                    )
                } catch (e: Exception) {
                    // mantém AGUARDANDO_EXCLUSAO
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllMovimentacoesDoHorario(
        horarioReferente: String,
        producaoId: String
    ): Flow<List<MovimentacaoEntity>> {
        return localDataSource.getAllMovimentacoesDoHorario(
            horarioReferente = horarioReferente,
            producaoId = producaoId
        )
            .map { lista ->
                lista
                    .filter { movimentacao ->
                        movimentacao.statusSincronizacao != StatusSincronizacao.AGUARDANDO_EXCLUSAO
                    }
                    .map { movimentacao ->
                        movimentacao.toEntity()
                    }
            }
    }

    override suspend fun sincronizarMovimentacoesDoRemoto(): Result<Unit> {
        Log.d(TAG, "sincronizarMovimentacoesDoRemoto: Entrou no metodo")
        return try {
            val movimentacoesRemotas = remoteDataSource.getAllMovimentacoes()
        Log.d(TAG, "sincronizarMovimentacoesDoRemoto: Buscou no remote")

            val movimentacoesLocais = movimentacoesRemotas.map { gradeRemote ->
                gradeRemote.toLocalModel()
            }
        Log.d(TAG, "sincronizarMovimentacoesDoRemoto: Converteu")

            localDataSource.insertAllMovimentacoes(movimentacoesLocais)
        Log.d(TAG, "sincronizarMovimentacoesDoRemoto: Sincronizou")

            Result.success(Unit)
        } catch (e: Exception) {
        Log.d(TAG, "sincronizarMovimentacoesDoRemoto: Deu erro: $e")
            Result.failure(e)
        }
    }
}