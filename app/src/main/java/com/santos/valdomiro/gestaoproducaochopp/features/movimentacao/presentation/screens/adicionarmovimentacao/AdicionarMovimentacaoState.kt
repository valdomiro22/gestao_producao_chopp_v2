package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.screens.adicionarmovimentacao

data class AdicionarMovimentacaoState(
    val quantidade: String = "",

    val erroQuantidade: String? = null,
    val erroGeral: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)