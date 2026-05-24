package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.remotedatasource

import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model.MovimentacaoRemoteModel

interface MovimentacaoRemoteDataSource {
    suspend fun insertMovimentacao(movimentacao: MovimentacaoRemoteModel)
    suspend fun updateMovimentacao(movimentacao: MovimentacaoRemoteModel)
    suspend fun getMovimentacao(movimentacaoId: String): MovimentacaoRemoteModel?
    suspend fun deleteMovimentacao(movimentacaoId: String)
    suspend fun getAllMovimentacaos(): List<MovimentacaoRemoteModel>
    suspend fun getAllMovimentacoesDaProducao(producaoId: String): List<MovimentacaoRemoteModel>
    suspend fun getAllMovimentacoesOfHorario(horarioReferente: Int, producaoId: String): List<MovimentacaoRemoteModel>
    suspend fun deleteMovimentacoesDaProducao(producaoId: String)
}