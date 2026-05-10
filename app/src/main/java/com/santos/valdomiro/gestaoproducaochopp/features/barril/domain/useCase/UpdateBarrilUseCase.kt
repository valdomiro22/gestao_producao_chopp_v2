package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.useCase

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import javax.inject.Inject

class UpdateBarrilUseCase @Inject constructor(
    private val barrilRepository: BarrilRepository
) {

    suspend operator fun invoke(id: String, barril: BarrilEntity): Result<Unit> {
        return barrilRepository.updateBarril(id, barril)
    }

}