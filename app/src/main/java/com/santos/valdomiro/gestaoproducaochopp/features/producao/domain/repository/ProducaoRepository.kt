package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import kotlinx.coroutines.flow.Flow

interface ProducaoRepository {
    suspend fun insertProducao(producao: ProducaoEntity): Result<Unit>
    suspend fun updateProducao(producao: ProducaoEntity): Result<Unit>
    suspend fun updateStatusSincronizacao(
        producaoId: String,
        statusSincronizacao: StatusSincronizacao
    ): Result<Unit>

    suspend fun deleteProducao(producao: ProducaoEntity): Result<Unit>
    fun getOneById(producaoId: String): Flow<ProducaoEntity?>
    fun getAllProducoes(): Flow<List<ProducaoEntity>>
    fun getAllProducoesDaGrade(gradeId: String): Flow<List<ProducaoEntity>>
    suspend fun sincronizarProducoesDoRemoto(): Result<Unit>
}