package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.useCase

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import javax.inject.Inject

class DeleteBarrilUseCase @Inject constructor(
    private val barrilRepository: BarrilRepository
) {

    suspend operator fun invoke(id: String): Result<Unit> {
        return barrilRepository.deleteBarril(id)
    }

}