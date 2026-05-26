package com.santos.valdomiro.gestaoproducaochopp.common.usecase

data class SincronizacaoInicialState(
    val isLoading: Boolean = false,
    val sincronizado: Boolean = false,
    val erro: String? = null
)