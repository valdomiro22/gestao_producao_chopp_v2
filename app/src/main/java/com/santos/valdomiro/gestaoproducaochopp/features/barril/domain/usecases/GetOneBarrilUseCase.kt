package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOneBarrilUseCase @Inject constructor(
    private val repository: BarrilRepository
) {

    operator fun invoke(barrilId: String): Flow<Result<BarrilEntity>> {
        if (barrilId.isBlank()) {
            return flowOf(Result.failure(IllegalArgumentException("ID do barril inválido")))
        }

        return repository.getOneById(barrilId = barrilId)
            .map { barril ->
                if (barril != null) {
                    Result.success(barril)
                } else {
                    Result.failure(Exception("Contador não encontrado com o ID: $barrilId"))
                }
            }
    }

}