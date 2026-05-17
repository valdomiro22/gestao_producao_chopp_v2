package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOneProducaoUseCase @Inject constructor(
    private val repository: ProducaoRepository
) {

    operator fun invoke(producaoId: String): Flow<Result<ProducaoEntity>> {
        if (producaoId.isBlank()) {
            return flowOf(Result.failure(IllegalArgumentException("ID da produção inválido")))
        }

        return repository.getOneById(producaoId = producaoId)
            .map { producao ->
                if (producao != null) {
                    Result.success(producao)
                } else {
                    Result.failure(Exception("Produção não encontrada com o ID: $producaoId"))
                }
            }
    }

}