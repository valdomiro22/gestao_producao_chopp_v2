package com.santos.valdomiro.gestaoproducaochopp.common.enums

enum class Turno(
    val id: Int,
    val label: String,
    val horarios: Map<String, String>
) {
    TURNO_A(
        id = 1,
        label = "Turno A",
        horarios = mapOf(
            "7h" to "07:00",
            "8h" to "08:00",
            "9h" to "09:00",
            "10h" to "10:00",
            "11h" to "11:00",
            "12h" to "12:00",
            "13h" to "13:00",
            "14h" to "14:00",
            "14e20h" to "14:20"
        )
    ),
    TURNO_B(
        id = 2,
        label = "Turno B",
        horarios = mapOf(
            "15h" to "15:00",
            "16h" to "16:00",
            "17h" to "17:00",
            "18h" to "18:00",
            "19h" to "19:00",
            "20h" to "20:00",
            "21h" to "21:00",
            "22h" to "22:00",
            "22d40" to "22:40"
        )
    ),
    TURNO_C(
        id = 3,
        label = "Turno C",
        horarios = mapOf(
            "23h" to "23:00",
            "0h" to "00:00",
            "1hh" to "01:00",
            "2h" to "02:00",
            "3h" to "03:00",
            "4h" to "04:00",
            "5h" to "05:00",
            "6h" to "06:00"
        )
    );

    companion object {
        fun fromId(id: Int): Turno =
            entries.firstOrNull { it.id == id }
                ?: throw IllegalArgumentException("Id de Turno inválido: $id")
    }
}