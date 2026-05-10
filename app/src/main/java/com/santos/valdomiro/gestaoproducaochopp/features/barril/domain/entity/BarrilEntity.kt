package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity

data class BarrilEntity(
    val id: String? = null,
    val nome: String = "",
    val volume: Int = -1,
    val descartavel: Boolean = false
)
