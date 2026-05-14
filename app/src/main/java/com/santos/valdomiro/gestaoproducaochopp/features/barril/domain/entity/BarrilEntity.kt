package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.StatusSincronizacao
import java.time.Instant

data class BarrilEntity(
    val id: String,
    val nome: String,
    val volume: Int,
    val criadoEm: Instant,
    val editadoEm: Instant? = null,
    val descartavel: Boolean,
    val statusSincronizacao: StatusSincronizacao,
)
