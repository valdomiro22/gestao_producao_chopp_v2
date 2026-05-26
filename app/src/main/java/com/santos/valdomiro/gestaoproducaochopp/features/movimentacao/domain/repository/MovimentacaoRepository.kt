package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import kotlinx.coroutines.flow.Flow

interface MovimentacaoRepository {
    suspend fun insertMovimentacao(movimentacao: MovimentacaoEntity): Result<Unit>
    suspend fun updateMovimentacao(movimentacao: MovimentacaoEntity): Result<Unit>
    suspend fun updateStatusSincronizacao(
        movimentacaoId: String,
        statusSincronizacao: StatusSincronizacao
    ): Result<Unit>

    suspend fun deleteMovimentacao(movimentacao: MovimentacaoEntity): Result<Unit>
    fun getOneById(movimentacaoId: String): Flow<MovimentacaoEntity?>
    fun getAllMovimentacoes(): Flow<List<MovimentacaoEntity>>
    fun getAllOfProducao(producaoId: String): Flow<List<MovimentacaoEntity>>
    suspend fun sincronizarMovimentacoesPendentes(): Result<Unit>
    fun getAllMovimentacoesDoHorario(
        horarioReferente: String,
        producaoId: String
    ): Flow<List<MovimentacaoEntity>>
    suspend fun sincronizarMovimentacoesDoRemoto(): Result<Unit>
}