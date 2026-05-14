package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model

import com.google.firebase.Timestamp

data class BarrilRemoteModel(
    val id: String = "",
    val nome: String = "",
    val volume: Int = 0,
    val criadoEm: Timestamp? = null,
    val editadoEm: Timestamp? = null,
    val descartavel: Boolean = false
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "nome" to nome,
            "volume" to volume,
            "criadoEm" to criadoEm,
            "atualizadoEm" to editadoEm,
            "descartavel" to descartavel
        )
    }
}