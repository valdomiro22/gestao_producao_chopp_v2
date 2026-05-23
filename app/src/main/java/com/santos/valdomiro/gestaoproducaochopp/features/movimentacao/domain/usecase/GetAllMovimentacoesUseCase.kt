package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMovimentacoesUseCase @Inject constructor(
    private val repository: MovimentacaoRepository
) {

    operator fun invoke(): Flow<List<MovimentacaoEntity>> {
        return repository.getAllMovimentacoes()
    }

}