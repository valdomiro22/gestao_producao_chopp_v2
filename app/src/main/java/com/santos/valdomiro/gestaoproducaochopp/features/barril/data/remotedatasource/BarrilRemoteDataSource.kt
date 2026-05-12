package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilRemoteModel

interface BarrilRemoteDataSource {
    suspend fun insertBarril(barril: BarrilRemoteModel)
    suspend fun updateBarril(id: String, barril: BarrilRemoteModel)
    suspend fun getBarril(id: String): BarrilRemoteModel?
    suspend fun deleteBarril(id: String)
    suspend fun getAllBarris(): List<BarrilRemoteModel>
}