package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity

interface BarrilRepository {
    suspend fun insertBarril(barril: BarrilEntity): Result<Unit>
    suspend fun updateBarril(id: String, barril: BarrilEntity): Result<Unit>
    suspend fun getBarril(id: String): Result<BarrilEntity?>
    suspend fun deleteBarril(id: String): Result<Unit>
    suspend fun getAllBarris(): Result<List<BarrilEntity>>
}