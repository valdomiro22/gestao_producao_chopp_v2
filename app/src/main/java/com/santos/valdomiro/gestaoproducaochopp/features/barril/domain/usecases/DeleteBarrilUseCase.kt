package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import javax.inject.Inject

class DeleteBarrilUseCase @Inject constructor(
    private val repository: BarrilRepository
) {

    suspend operator fun invoke(barril: BarrilEntity): Result<Unit> {
        if (barril.id.isBlank()) {
            return Result.failure(
                IllegalArgumentException("ID do barril inválido")
            )
        }

        return repository.deleteBarril(barril)
    }

}
