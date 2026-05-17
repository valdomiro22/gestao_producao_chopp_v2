package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import java.time.Instant

data class ProducaoEntity(
    val id: String,
    val gradeId: String,
    val barrilId: String,
    val produtoId: String,
    val quantidadeProgramada: Int,
    val quantidadeProduzida: Int,
    val volumeNecessario: Double,
    val status: StatusProducao,
    val criadoEm: Instant,
    val editadaEm: Instant? = null,
    val dataFimDeProducao: Instant? = null,
    val statusSincronizacao: StatusSincronizacao,
) {
    val quantidadePendente: Int
        get() = quantidadeProgramada - quantidadeProduzida

}