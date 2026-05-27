package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.repository

import android.util.Log
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.localdatasource.BarrilLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper.toEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper.toLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper.toRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource.BarrilRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import com.santos.valdomiro.gestaoproducaochopp.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
                Log.e(TAG, "insertBarril: Erro ao sincronizar Barril como o repositório remoto", e)
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
                    barril = barrilPendente.toRemoteModel()
                )

                localDataSource.updateStatusSincronizacao(
                    barrilId = barrilPendente.id,
                    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                )
            } catch (e: Exception) {
                Log.e(TAG, "updateBarril: Erro ao sincronizar Barril como o repositório remoto", e)
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
                remoteDataSource.deleteBarril(barrilId = barrilParaExcluir.id)

                localDataSource.deleteBarril(barril = barrilParaExcluir.toLocalModel())
            } catch (e: Exception) {
                Log.e(TAG, "deleteBarril: Erro ao sincronizar Barril como o repositório remoto", e)
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

    override suspend fun sincronizarBarrisDoRemoto(): Result<Unit> {
        return try {
            val barrisRemotos = remoteDataSource.getAllBarris()

            val barrisLocais = localDataSource
                .getAllBarris()
                .first()

            val idsRemotos = barrisRemotos
                .map { barrilRemote -> barrilRemote.id }
                .toSet()

            val idsLocais = barrisLocais
                .map { barrilLocal -> barrilLocal.id }
                .toSet()

            val barrisParaSalvar = barrisRemotos
                .filter { barrilRemote ->
                    barrilRemote.id !in idsLocais
                }
                .map { barrilRemote ->
                    barrilRemote.toLocalModel()
                }

            if (barrisParaSalvar.isNotEmpty()) {
                localDataSource.insertAllBarris(barrisParaSalvar)
            }

            val idsBarrisParaExcluirDoLocal = barrisLocais
                .filter { barrilLocal ->
                    barrilLocal.id !in idsRemotos &&
                            barrilLocal.statusSincronizacao == StatusSincronizacao.SINCRONIZADO
                }
                .map { barrilLocal ->
                    barrilLocal.id
                }

            if (idsBarrisParaExcluirDoLocal.isNotEmpty()) {
                localDataSource.deleteVariosBarris(idsBarrisParaExcluirDoLocal)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}