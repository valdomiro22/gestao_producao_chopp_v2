package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.repository

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.localdatasource.BarrilLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper.toEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper.toLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper.toRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource.BarrilRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BarrilRepositoryImpl @Inject constructor(
    private val localDataSource: BarrilLocalDataSource,
    private val remoteDataSource: BarrilRemoteDataSource
) : BarrilRepository {

    override suspend fun insertBarril(barril: BarrilEntity): Result<Unit> {
        return try {
            val barrilPendente = barril.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO
            )

            localDataSource.insertBarril(barrilPendente.toLocalModel())

            try {
                remoteDataSource.insertBarril(
                    barrilPendente.toRemoteModel()
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

    override suspend fun updateBarril(barril: BarrilEntity): Result<Unit> {
        return try {
            val barrilPendente = barril.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
            )
            localDataSource.updateBarril(barrilPendente.toLocalModel())

            try {
                remoteDataSource.updateBarril(
                    id = barrilPendente.id,
                    barril = barrilPendente.toRemoteModel()
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

    override suspend fun deleteBarril(barril: BarrilEntity): Result<Unit> {
        return try {
            val barrilParaExcluir = barril.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_EXCLUSAO
            )

            localDataSource.updateBarril(barrilParaExcluir.toLocalModel())

            try {
                remoteDataSource.deleteBarril(
                    id = barrilParaExcluir.id  // TODO - Enviar o barril inteiro, como no locaDataSource()
                )

                localDataSource.deleteBarril(barril = barrilParaExcluir.toLocalModel())
            } catch (e: Exception) {
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOneById(barrilId: String): Flow<BarrilEntity?> {
        return localDataSource.getOneById(barrilId)
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

    override fun getAllBarris(): Flow<List<BarrilEntity>> {
        return localDataSource.getAllBarris()
            .map { listaBarrisLocal ->
                listaBarrisLocal
                    .filter { barrilLocal ->
                        barrilLocal.statusSincronizacao != StatusSincronizacao.AGUARDANDO_EXCLUSAO
                    }
                    .map { barrilLocal ->
                        barrilLocal.toEntity()
                    }
            }
    }
}