package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.localdatasource

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model.MovimentacaoLocalModel
import kotlinx.coroutines.flow.Flow

interface MovimentacaoLocalDataSource {
    suspend fun insertMovimentacao(movimentacao: MovimentacaoLocalModel)
    suspend fun updateMovimentacao(movimentacao: MovimentacaoLocalModel)
    suspend fun updateStatusSincronizacao(
        movimentacaoId: String,
        statusSincronizacao: StatusSincronizacao
    )

    suspend fun deleteMovimentacao(movimentacao: MovimentacaoLocalModel)
    fun getOneById(movimentacaoId: String): Flow<MovimentacaoLocalModel?>
    fun getAllMovimentacoes(): Flow<List<MovimentacaoLocalModel>>
    fun getAllOfProducao(producaoId: String): Flow<List<MovimentacaoLocalModel>>
    suspend fun getMovimentacoesAguardandoEnvio(): List<MovimentacaoLocalModel>
    fun getAllMovimentacoesDoHorario(horarioReferente: String, producaoId: String): Flow<List<MovimentacaoLocalModel>>
}