package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.localdatasource

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model.ProducaoLocalModel
import kotlinx.coroutines.flow.Flow

interface ProducaoLocalDataSource {
    suspend fun insertProducao(producao: ProducaoLocalModel)
    suspend fun updateProducao(producao: ProducaoLocalModel)
    suspend fun updateStatusSincronizacao(
        producaoId: String,
        statusSincronizacao: StatusSincronizacao
    )

    suspend fun deleteProducao(producao: ProducaoLocalModel)
    fun getOneById(producaoId: String): Flow<ProducaoLocalModel?>
    fun getAllProducoes(): Flow<List<ProducaoLocalModel>>
    fun getAllProducoesDaGrade(gradeId: String): Flow<List<ProducaoLocalModel>>
    suspend fun insertAllProducoes(producoes: List<ProducaoLocalModel>)
    suspend fun deleteVariasProducoes(ids: List<String>)
}