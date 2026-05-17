package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model

import com.google.firebase.Timestamp
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.StatusProducao

data class ProducaoRemoteModel(
    val id: String,
    val gradeId: String,
    val barrilId: String,
    val produtoId: String,
    val quantidadeProgramada: Int,
    val quantidadeProduzida: Int,
    val volumeNecessario: Double,
    val status: StatusProducao,
    val criadoEm: Timestamp,
    val editadoEm: Timestamp?,
    val dataFimDeProducao: Timestamp?,
    val statusSincronizacao: StatusSincronizacao,
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
            "status" to status,
            "criadoEm" to criadoEm,
            "editadoEm" to editadoEm,
            "dataFimDeProducao" to dataFimDeProducao,
            "statusSincronizacao" to statusSincronizacao
        )
    }
}