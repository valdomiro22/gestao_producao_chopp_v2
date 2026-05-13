package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import kotlinx.coroutines.flow.Flow

interface BarrilRepository {
    suspend fun insertBarril(barril: BarrilLocalModel): Result<Unit>
    suspend fun updateBarril(barril: BarrilLocalModel): Result<Unit>
    suspend fun updateStatusSincronizacao(barrilId: String, statusSincronizacao: StatusSincronizacao): Result<Unit>
    suspend fun deleteBarril(barril: BarrilLocalModel): Result<Unit>
    fun getOneById(barrilId: String): Flow<BarrilLocalModel?>
    fun getAllBarris(): Flow<List<BarrilLocalModel>>
}