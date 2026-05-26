package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilLocalModel
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import kotlinx.coroutines.flow.Flow

interface BarrilRepository {
    suspend fun insertBarril(barril: BarrilEntity): Result<Unit>
    suspend fun updateBarril(barril: BarrilEntity): Result<Unit>
    suspend fun updateStatusSincronizacao(barrilId: String, statusSincronizacao: StatusSincronizacao): Result<Unit>
    suspend fun deleteBarril(barril: BarrilEntity): Result<Unit>
    fun getOneById(barrilId: String): Flow<BarrilEntity?>
    fun getAllBarris(): Flow<List<BarrilEntity>>
    suspend fun sincronizarBarrisDoRemoto(): Result<Unit>
}