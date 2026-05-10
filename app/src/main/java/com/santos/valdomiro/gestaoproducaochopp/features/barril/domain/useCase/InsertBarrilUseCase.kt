package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.useCase

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import java.util.UUID
import javax.inject.Inject

class InsertBarrilUseCase @Inject constructor(
    private val barrilRepository: BarrilRepository
) {
    suspend operator fun invoke(barril: BarrilEntity): Result<Unit> {
        val idGerado = UUID.randomUUID().toString()
        val barrilComId = barril.copy(id = idGerado)
        return barrilRepository.insertBarril(barrilComId)
    }
}