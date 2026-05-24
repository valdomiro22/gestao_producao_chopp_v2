package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import javax.inject.Inject

class SincronizarMovimentacoesPendentesUseCase @Inject constructor(
    private val repository: MovimentacaoRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.sincronizarMovimentacoesPendentes()
    }
}