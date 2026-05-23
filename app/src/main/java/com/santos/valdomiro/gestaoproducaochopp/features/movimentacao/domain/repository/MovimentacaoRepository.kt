package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model.MovimentacaoLocalModel
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
    fun getAllMovimentacoesDoHorario(horarioReferente: Int, producaoId: String): Flow<List<MovimentacaoEntity>>
}