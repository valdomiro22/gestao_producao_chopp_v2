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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.collections.toSet

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
                    .map { movimentacaoLocal ->
                        movimentacaoLocal.toEntity()
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
                    .map { movimentacaoLocal ->
                        movimentacaoLocal.toEntity()
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
        return try {
            val movimentacoesRemotos = remoteDataSource.getAllMovimentacoes()

            val movimentacoesLocais = localDataSource
                .getAllMovimentacoes()
                .first()

            val idsRemotos = movimentacoesRemotos
                .map { it.id }
                .toSet()

            val idsLocais = movimentacoesLocais
                .map { it.id }
                .toSet()

            val movimentacoesParaSalvar = movimentacoesRemotos
                .filter { movimentacaoRemote ->
                    movimentacaoRemote.id !in idsLocais
                }
                .map { movimentacaoRemote ->
                    movimentacaoRemote.toLocalModel()
                }

            if (movimentacoesParaSalvar.isNotEmpty()) {
                localDataSource.insertAllMovimentacoes(movimentacoesParaSalvar)
            }

            val idsBarrisParaExcluirDoLocal = movimentacoesLocais
                .filter { movimentacaoLocal ->
                    movimentacaoLocal.id !in idsRemotos &&
                            movimentacaoLocal.statusSincronizacao == StatusSincronizacao.SINCRONIZADO
                }
                .map { it.id }

            if (idsBarrisParaExcluirDoLocal.isNotEmpty()) {
                localDataSource.deleteVariasMovimentacoes(idsBarrisParaExcluirDoLocal)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}