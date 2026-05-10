package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.useCase

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import javax.inject.Inject

class GetOneBarrilUseCase @Inject constructor(
    private val barrilRepository: BarrilRepository
) {

    suspend operator fun invoke(id: String): Result<BarrilEntity> {
        return barrilRepository.getBarril(id).fold(
            onSuccess = { barril ->
                if (barril != null) {
                    Result.success(barril)
                } else {
                    Result.failure(Exception("Barril não encontrado com o ID: $id"))
                }
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }

}