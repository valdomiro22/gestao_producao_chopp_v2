package com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOneGradeUseCase @Inject constructor(
    private val repository: GradeRepository
) {

    operator fun invoke(gradeId: String): Flow<Result<GradeEntity>> {
        if (gradeId.isBlank()) {
            return flowOf(Result.failure(IllegalArgumentException("O ID da grade inválido")))
        }

        return repository.getOneById(gradeId = gradeId)
            .map { grade ->
                if (grade != null) {
                    Result.success(grade)
                } else {
                    Result.failure(NoSuchElementException("Grade não encontrada com o ID: $gradeId"))
                }
            }
    }

}