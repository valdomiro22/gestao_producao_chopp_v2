package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SincronizarMovimentacoesRealtimeUseCase @Inject constructor(
    private val movimentacaoRepository: MovimentacaoRepository
) {
    operator fun invoke(): Flow<Unit> {
        return movimentacaoRepository.sincronizarMovimentacoesRealtime()
    }
}