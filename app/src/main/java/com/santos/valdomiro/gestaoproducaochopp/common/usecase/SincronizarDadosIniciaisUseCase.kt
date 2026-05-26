package com.santos.valdomiro.gestaoproducaochopp.common.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository.GradeRepository
import jakarta.inject.Inject

class SincronizarDadosIniciaisUseCase @Inject constructor(
    private val gradeRepository: GradeRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return gradeRepository.sincronizarGradesDoRemoto()
    }
}