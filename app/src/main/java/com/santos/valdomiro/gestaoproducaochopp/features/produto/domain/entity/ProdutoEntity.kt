package com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import java.time.Instant

data class ProdutoEntity(
    val id: String,
    val nome: String,
    val prazoValidade: Int,
    val criadoEm: Instant,
    val editadoEm: Instant? = null,
    val statusSincronizacao: StatusSincronizacao
)