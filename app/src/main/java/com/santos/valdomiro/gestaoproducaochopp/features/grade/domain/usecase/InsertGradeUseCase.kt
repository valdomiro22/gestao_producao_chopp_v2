package com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository.GradeRepository
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class InsertGradeParams(
    val numero: Int,
    val data: LocalDate,
    val descartavel: Boolean = false,
)

class InsertGradeUseCase @Inject constructor(
    private val repository: GradeRepository
) {

    suspend operator fun invoke(params: InsertGradeParams): Result<Unit> {
        val numero = params.numero

        if (numero <= 0) {
            return Result.failure(
                IllegalArgumentException("O numero da grade deve ser maior que zero")
            )
        }

        val criadoEm = Instant.now()
        val idGerado = UUID.randomUUID().toString()

        val grade = GradeEntity(
            id = idGerado,
            numero = numero,
            data = params.data,
            quantidadeBarris = 0,
            volumeHlNecessario = 0.0,
            criadoEm = criadoEm,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO
        )

        repository.insertGrade(grade = grade)
            .getOrElse { return Result.failure(it) }
        return Result.success(Unit)
    }

}