package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.remotedatasource

import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model.ProducaoRemoteModel

interface ProducaoRemoteDatasource {
    suspend fun insertProducao(producao: ProducaoRemoteModel)
    suspend fun updateProducao(id: String, producao: ProducaoRemoteModel)
    suspend fun getProducao(id: String): ProducaoRemoteModel?
    suspend fun deleteProducao(id: String)
    suspend fun getAllProducoes(): List<ProducaoRemoteModel>
    suspend fun getAllProducoesDaGrade(gradeId: String): List<ProducaoRemoteModel>
    suspend fun deleteProducoesDaGrade(gradeId: String)
}