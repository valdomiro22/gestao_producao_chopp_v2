package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import javax.inject.Inject

class DeleteProducaoUseCase @Inject constructor(
    private val repository: ProducaoRepository
) {

    suspend operator fun invoke(producao: ProducaoEntity): Result<Unit> {
        if (producao.id.isBlank()) {
            return Result.failure(
                IllegalArgumentException("ID da produção inválido")
            )
        }

        return repository.deleteProducao(producao)
    }

}