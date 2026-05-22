package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllOfProducaoUseCase @Inject constructor(
    private val repository: MovimentacaoRepository
) {

    operator fun invoke(producaoId: String): Flow<List<MovimentacaoEntity>> {
        return repository.getAllOfProducao(producaoId = producaoId)
    }

}