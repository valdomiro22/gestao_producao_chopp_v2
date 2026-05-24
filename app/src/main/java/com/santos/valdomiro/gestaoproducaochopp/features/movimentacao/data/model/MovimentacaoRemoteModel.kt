package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model

import com.google.firebase.Timestamp
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.TipoMovimentacao

data class MovimentacaoRemoteModel(
    val id: String,
    val producaoId: String,
    val turnoId: Int,
    val horarioReferente: String,
    val quantidade: Int,
    val tipo: TipoMovimentacao,
    val criadoEm: Timestamp,
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "producaoId" to producaoId,
            "turnoId" to turnoId,
            "horarioReferente" to horarioReferente,
            "quantidade" to quantidade,
            "tipo" to tipo.name,
            "criadoEm" to criadoEm,
        )
    }
}