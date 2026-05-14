package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllBarrisUseCase @Inject constructor(
    private val repository: BarrilRepository
) {

    operator fun invoke(): Flow<List<BarrilEntity>> {
        return repository.getAllBarris()
    }

}