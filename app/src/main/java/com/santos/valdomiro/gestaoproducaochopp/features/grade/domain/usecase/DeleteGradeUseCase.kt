package com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository.GradeRepository
import javax.inject.Inject

class DeleteGradeUseCase @Inject constructor(
    private val repository: GradeRepository
) {

    suspend operator fun invoke(grade: GradeEntity): Result<Unit> {
        if (grade.id.isBlank()) {
            return Result.failure(IllegalArgumentException("O ID da grade é inválido"))
        }

        return repository.deleteGrade(grade = grade)
    }

}