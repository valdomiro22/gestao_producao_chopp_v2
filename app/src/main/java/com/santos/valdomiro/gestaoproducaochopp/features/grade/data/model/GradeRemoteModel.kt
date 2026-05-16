package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model

import com.google.firebase.Timestamp

data class GradeRemoteModel(
    val id: String = "",
    val numero: Int = 0,
    val data: Timestamp? = null,
    val quantidadeBarris: Int = 0,
    val volumeHlNecessario: Double = 0.0,
    val criadoEm: Timestamp? = null,
    val editadoEm: Timestamp? = null,
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "numero" to numero,
            "data" to data,
            "quantidadeBarris" to quantidadeBarris,
            "volumeHlNecessario" to volumeHlNecessario,
            "criadoEm" to criadoEm,
            "editadoEm" to editadoEm,
        )
    }
}
