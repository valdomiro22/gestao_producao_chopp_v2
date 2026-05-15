package com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model

import com.google.firebase.Timestamp
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import kotlin.String

data class ProdutoRemoteModel(
    val id: String = "",
    val nome: String = "",
    val prazoValidade: Int = 0,
    val criadoEm: Timestamp? = null,
    val editadoEm: Timestamp? = null,
    val statusSincronizacao: StatusSincronizacao
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id"  to id,
            "nome"  to nome,
            "prazoValidade"  to prazoValidade,
            "criadoEm"  to criadoEm,
            "editadoEm"  to editadoEm,
            "statusSincronizacao"  to statusSincronizacao
        )
    }
}