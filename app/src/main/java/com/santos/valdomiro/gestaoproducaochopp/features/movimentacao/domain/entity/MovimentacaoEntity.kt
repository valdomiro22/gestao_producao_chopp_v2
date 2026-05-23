package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import java.time.Instant

data class MovimentacaoEntity(
    val id: String,
    val producaoId: String,
    val turnoId: Int,
    val horarioReferente: String,
    val quantidade: Int,
    val tipo: TipoMovimentacao,
    val criadoEm: Instant,
    val statusSincronizacao: StatusSincronizacao
)