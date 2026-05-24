package com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository.GradeRepository
import javax.inject.Inject

class InserirQuantidadeEVolumeNaGradeUseCase @Inject constructor(
    private val repository: GradeRepository
) {

    suspend operator fun invoke(grade: GradeEntity): Result<Unit> {
        repository.updateGrade(grade)
            .getOrElse { return Result.failure(it) }

        return Result.success(Unit)
    }

}