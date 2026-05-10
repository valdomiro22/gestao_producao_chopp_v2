package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.useCase

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import javax.inject.Inject

class GetListaBarrisUseCase @Inject constructor(
    private val barrilRepository: BarrilRepository
) {

    suspend operator fun invoke(): Result<List<BarrilEntity>> {
        return barrilRepository.getAllBarris()
    }

}