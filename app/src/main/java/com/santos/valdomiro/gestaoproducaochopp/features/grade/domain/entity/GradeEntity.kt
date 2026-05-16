package com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import java.time.Instant

data class GradeEntity(
    val id: String,
    val numero: Int,
    val data: Instant,
    val quantidadeBarris: Int,
    val volumeHlNecessario: Double,
    val criadoEm: Instant,
    val editadoEm: Instant? = null,
    val statusSincronizacao: StatusSincronizacao,
)
