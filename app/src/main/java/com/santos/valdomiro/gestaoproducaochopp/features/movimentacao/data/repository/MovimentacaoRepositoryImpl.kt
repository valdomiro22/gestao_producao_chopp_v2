package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.repository

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.localdatasource.MovimentacaoLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.mapper.toEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.mapper.toLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.mapper.toRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.remotedatasource.MovimentacaoRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
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

    override suspend fun updateMovimentacao(movimentacao: MovimentacaoEntity): Result<Unit> {
        return try {
            val movimentacaoPendente = movimentacao.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
            )

            localDataSource.updateMovimentacao(
                movimentacaoPendente.toLocalModel()
            )

            try {
                remoteDataSource.updateMovimentacao(
                    movimentacao = movimentacaoPendente.toRemoteModel()
                )

                localDataSource.updateStatusSincronizacao(
                    movimentacaoId = movimentacaoPendente.id,
                    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                )
            } catch (e: Exception) {
                // mantém como AGUARDANDO_ATUALIZACAO
            }

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

    override suspend fun deleteMovimentacao(movimentacao: MovimentacaoEntity): Result<Unit> {
        return try {
            val movimentacaoParaExcluir = movimentacao.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_EXCLUSAO
            )

            localDataSource.updateMovimentacao(movimentacaoParaExcluir.toLocalModel())

            try {
                remoteDataSource.deleteMovimentacao(
                    movimentacaoId = movimentacaoParaExcluir.id  // TODO - Enviar o movimentacao inteiro, como no locaDataSource()
                )

                localDataSource.deleteMovimentacao(
                    movimentacao = movimentacaoParaExcluir.toLocalModel(),
                )
            } catch (e: Exception) {
            }

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
            val pendentes = localDataSource.getMovimentacoesAguardandoEnvio()

            pendentes.forEach { movimentacaoLocal ->
                try {
                    val movimentacaoEntity = movimentacaoLocal.toEntity()

                    remoteDataSource.insertMovimentacao(
                        movimentacaoEntity.toRemoteModel()
                    )

                    localDataSource.updateStatusSincronizacao(
                        movimentacaoId = movimentacaoEntity.id,
                        statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                    )
                } catch (e: Exception) {
                    // Se falhar uma movimentação, mantém AGUARDANDO_ENVIO
                    // e continua tentando as próximas.
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
}