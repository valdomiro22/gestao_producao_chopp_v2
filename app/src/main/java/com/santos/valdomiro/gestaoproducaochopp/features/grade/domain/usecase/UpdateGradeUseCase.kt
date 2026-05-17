package com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository.GradeRepository
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

data class UpdateGradeParams(
    val id: String,
    val numero: Int,
    val data: LocalDate,
    val criadoEm: Instant,
)

class UpdateGradeUseCase @Inject constructor(
    private val repository: GradeRepository
) {

    suspend operator fun invoke(params: UpdateGradeParams): Result<Unit> {
        val numero = params.numero

        if (numero <= 0) {
            return Result.failure(
                IllegalArgumentException("O numero da grade deve ser maior que zero")
            )
        }

        val editadoEm = Instant.now()

        val gradeAtualizada = GradeEntity(
            id = params.id,
            numero = numero,
            data = params.data,
            quantidadeBarris = 0,  // TODO - Requer atenção
            volumeHlNecessario = 0.0,  // TODO - Requer atenção
            criadoEm = params.criadoEm,
            editadoEm = editadoEm,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
        )

        repository.updateGrade(grade = gradeAtualizada)
            .getOrElse { return Result.failure(it) }

        return Result.success(Unit)
    }

}