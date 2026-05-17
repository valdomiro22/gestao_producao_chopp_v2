package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProducoesUseCase @Inject constructor(
    private val repository: ProducaoRepository
) {

    operator fun invoke(): Flow<List<ProducaoEntity>> {
        return repository.getAllProducoes()
    }
}