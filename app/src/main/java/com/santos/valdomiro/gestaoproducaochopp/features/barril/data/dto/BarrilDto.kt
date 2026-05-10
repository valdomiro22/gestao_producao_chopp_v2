package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class BarrilDto(
    val id: String? = null,
    val nome: String = "",
    val volume: Int = -1,
    val descartavel: Boolean = false
) {

    /** Converte o DTO para Map usado no .update() do Firestore */
    fun toMap() : Map<String, Any?> {
        return mapOf(
            "nome" to nome,
            "volume" to volume,
            "descartavel" to descartavel,
        )
    }
}