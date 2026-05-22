package com.santos.valdomiro.gestaoproducaochopp.common.enums

enum class Turno(
    val id: Int,
    val label: String,
    val horarios: List<String>
) {
    TURNO_A(
        id = 1,
        label = "Turno A",
        horarios = listOf(
            "07:00",
            "08:00",
            "09:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "14:20"
        )
    ),

    TURNO_B(
        id = 2,
        label = "Turno B",
        horarios = listOf(
            "15:00",
            "16:00",
            "17:00",
            "18:00",
            "19:00",
            "20:00",
            "21:00",
            "22:00",
            "22:40"
        )
    ),

    TURNO_C(
        id = 3,
        label = "Turno C",
        horarios = listOf(
            "23:00",
            "00:00",
            "01:00",
            "02:00",
            "03:00",
            "04:00",
            "05:00",
            "06:00"
        )
    );

    companion object {
        fun fromId(id: Int): Turno =
            entries.firstOrNull { it.id == id }
                ?: throw IllegalArgumentException("Id de Turno inválido: $id")
    }
}