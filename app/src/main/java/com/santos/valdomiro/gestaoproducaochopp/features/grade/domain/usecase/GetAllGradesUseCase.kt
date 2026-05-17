package com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllGradesUseCase @Inject constructor(
    private val repository: GradeRepository
) {

    operator fun invoke(): Flow<List<GradeEntity>> {
        return repository.getAllGrades()
    }

}