package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.remotedatasource

import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model.MovimentacaoRemoteModel
import kotlinx.coroutines.flow.Flow

interface MovimentacaoRemoteDataSource {
    suspend fun insertMovimentacao(movimentacao: MovimentacaoRemoteModel)
    suspend fun updateMovimentacao(movimentacao: MovimentacaoRemoteModel)
    suspend fun getMovimentacao(movimentacaoId: String): MovimentacaoRemoteModel?
    suspend fun deleteMovimentacao(movimentacaoId: String)
    suspend fun getAllMovimentacoes(): List<MovimentacaoRemoteModel>

    fun observarMovimentacoesDaProducao(
        producaoId: String
    ): Flow<List<MovimentacaoRemoteModel>>

    fun observarMovimentacoesDoHorario(
        horarioReferente: String,
        producaoId: String
    ): Flow<List<MovimentacaoRemoteModel>>

    suspend fun deleteMovimentacoesDaProducao(producaoId: String)

    fun getAllMovimentacoesRealtime(): Flow<List<MovimentacaoRemoteModel>>
}