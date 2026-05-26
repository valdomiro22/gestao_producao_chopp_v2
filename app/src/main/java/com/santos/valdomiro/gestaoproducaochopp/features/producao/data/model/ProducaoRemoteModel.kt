package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model

import com.google.firebase.Timestamp
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.StatusProducao

data class ProducaoRemoteModel(
    val id: String = "",
    val gradeId: String = "",
    val barrilId: String = "",
    val produtoId: String = "",
    val quantidadeProgramada: Int = 0,
    val quantidadeProduzida: Int = 0,
    val volumeNecessario: Double = 0.0,
    val status: StatusProducao = StatusProducao.EM_PRODUCAO,
    val criadoEm: Timestamp? = null,
    val editadoEm: Timestamp? = null,
    val dataFimDeProducao: Timestamp? = null,
    val statusSincronizacao: StatusSincronizacao = StatusSincronizacao.SINCRONIZADO,
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "gradeId" to gradeId,
            "barrilId" to barrilId,
            "produtoId" to produtoId,
            "quantidadeProgramada" to quantidadeProgramada,
            "quantidadeProduzida" to quantidadeProduzida,
            "volumeNecessario" to volumeNecessario,
            "status" to status.name,
            "criadoEm" to criadoEm,
            "editadoEm" to editadoEm,
            "dataFimDeProducao" to dataFimDeProducao,
            "statusSincronizacao" to statusSincronizacao.name
        )
    }
}