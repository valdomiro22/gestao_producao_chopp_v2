package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.remotedatasource

import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model.ProducaoRemoteModel

interface ProducaoRemoteDatasource {
    suspend fun insertProducao(producao: ProducaoRemoteModel)
    suspend fun updateProducao(producao: ProducaoRemoteModel)
    suspend fun getProducao(producaoId: String): ProducaoRemoteModel?
    suspend fun deleteProducao(producaoId: String)
    suspend fun getAllProducoes(): List<ProducaoRemoteModel>
    suspend fun getAllProducoesDaGrade(gradeId: String): List<ProducaoRemoteModel>
    suspend fun deleteProducoesDaGrade(gradeId: String)
}