package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.localdatasource

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilLocalModel
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import kotlinx.coroutines.flow.Flow

interface BarrilLocalDataSource {
    suspend fun insertBarril(barril: BarrilLocalModel)
    suspend fun updateBarril(barril: BarrilLocalModel)
    suspend fun updateStatusSincronizacao(barrilId: String, statusSincronizacao: StatusSincronizacao)
    suspend fun deleteBarril(barril: BarrilLocalModel)
    fun getOneById(barrilId: String): Flow<BarrilLocalModel?>
    fun getAllBarris(): Flow<List<BarrilLocalModel>>
    suspend fun insertAllBarris(barris: List<BarrilLocalModel>)
    suspend fun deleteVariosBarris(ids: List<String>)
}