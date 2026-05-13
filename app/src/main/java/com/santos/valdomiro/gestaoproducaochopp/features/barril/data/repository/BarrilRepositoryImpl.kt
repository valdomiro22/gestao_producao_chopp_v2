package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.repository

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.localdatasource.BarrilLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper.toEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper.toRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource.BarrilRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BarrilRepositoryImpl @Inject constructor(
    private val localDataSource: BarrilLocalDataSource,
    private val remoteDataSource: BarrilRemoteDataSource
) : BarrilRepository {

    override suspend fun insertBarril(barril: BarrilLocalModel): Result<Unit> {
        return try {
            val barrilPendente = barril.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO
            )

            localDataSource.insertBarril(barrilPendente)

            try {
                remoteDataSource.insertBarril(
                    barrilPendente.toEntity().toRemoteModel()
                )

                localDataSource.updateStatusSincronizacao(
                    barrilId = barrilPendente.id,
                    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                )
            } catch (e: Exception) {
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBarril(barril: BarrilLocalModel): Result<Unit> {
        return try {
            val barrilPendente = barril.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
            )
            localDataSource.updateBarril(barrilPendente)

            try {
                remoteDataSource.updateBarril(
                    id = barrilPendente.id,
                    barril = barrilPendente.toEntity().toRemoteModel()
                )

                localDataSource.updateStatusSincronizacao(
                    barrilId = barrilPendente.id,
                    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                )
            } catch (e: Exception) {
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStatusSincronizacao(
        barrilId: String,
        statusSincronizacao: StatusSincronizacao
    ): Result<Unit> {
        return try {
            localDataSource.updateStatusSincronizacao(
                barrilId = barrilId,
                statusSincronizacao = statusSincronizacao
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBarril(barril: BarrilLocalModel): Result<Unit> {
        return try {
            val barrilParaExcluir = barril.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_EXCLUSAO
            )

            localDataSource.updateBarril(barrilParaExcluir)

            try {
                remoteDataSource.deleteBarril(
                    id = barrilParaExcluir.id
                )

                localDataSource.deleteBarril(barril = barrilParaExcluir)
            } catch (e: Exception) {
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOneById(barrilId: String): Flow<BarrilLocalModel?> {
        return localDataSource.getOneById(barrilId)
            .map { barril ->
                if (barril == null) {
                    null
                } else if (barril.statusSincronizacao == StatusSincronizacao.AGUARDANDO_EXCLUSAO) {
                    null
                } else {
                    barril
                }
            }
    }

    override fun getAllBarris(): Flow<List<BarrilLocalModel>> {
        return localDataSource.getAllBarris()
            .map { listaBarris ->
                listaBarris.filter { barril ->
                    barril.statusSincronizacao != StatusSincronizacao.AGUARDANDO_EXCLUSAO
                }
            }
    }
}