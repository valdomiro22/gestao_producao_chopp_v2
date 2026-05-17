package com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import java.time.Instant
import java.time.LocalDate

data class GradeEntity(
    val id: String,
    val numero: Int,
    val data: LocalDate,
    val quantidadeBarris: Int,
    val volumeHlNecessario: Double,
    val criadoEm: Instant,
    val editadoEm: Instant? = null,
    val statusSincronizacao: StatusSincronizacao,
)
