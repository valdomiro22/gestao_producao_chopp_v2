package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.adicionarbarril

data class AdicionarBarrilState(
    val nome: String = "",
    val volume: String = "",
    val descartavel: Boolean = false,

    val erroNome: String? = null,
    val erroVolume: String? = null,
    val erro: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)