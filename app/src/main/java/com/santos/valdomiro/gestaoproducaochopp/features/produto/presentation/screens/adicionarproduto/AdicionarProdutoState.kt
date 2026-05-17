package com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.screens.adicionarproduto

data class AdicionarProdutoState(
    val nome: String = "",
    val prazoValidade: String = "",

    val erroNome: String? = null,
    val erroPrazoValidade: String? = null,
    val erroGeral: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)