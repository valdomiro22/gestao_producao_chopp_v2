package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSaldoMovimentacaoHorarioUseCase @Inject constructor(
    private val repository: MovimentacaoRepository
) {
    suspend operator fun invoke(
        producaoId: String,
        horarioReferente: String
    ): Flow<List<MovimentacaoEntity>> {
        return repository.getAllMovimentacoesDoHorario(
            producaoId = producaoId,
            horarioReferente = horarioReferente
        )
    }
}