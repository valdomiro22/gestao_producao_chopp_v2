package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilRemoteModel

interface BarrilRemoteDataSource {
    suspend fun insertBarril(barril: BarrilRemoteModel)
    suspend fun updateBarril(barril: BarrilRemoteModel)
    suspend fun getBarril(barrilId: String): BarrilRemoteModel?
    suspend fun deleteBarril(barrilId: String)
    suspend fun getAllBarris(): List<BarrilRemoteModel>
}