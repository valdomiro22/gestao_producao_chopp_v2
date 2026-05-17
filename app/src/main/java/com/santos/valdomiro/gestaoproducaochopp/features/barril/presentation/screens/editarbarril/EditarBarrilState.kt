package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.editarbarril

data class EditarBarrilState(
    val nome: String = "",
    val volume: String = "",
    val descartavel: Boolean = false,

    val erroNome: String? = null,
    val erroVolume: String? = null,
    val erroGeral: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isEditSuccess: Boolean = false,
)