package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.editarproducao

data class EditarProducaoState(
    val quantidadeProgramada: String = "",
    val quantidadeProduzida: String = "",
    val barrilId: String? = null,
    val barrilNome: String? = null,
    val produtoId: String? = null,
    val produtoNome: String? = null,
    val quantidade: String = "",

    val erroBarril: String? = null,
    val erroProduto: String? = null,

    val erroQuantidadeProgramada: String? = null,
    val erroQuantidadeProduzida: String? = null,
    val erroGeral: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isEditSuccess: Boolean = false,
)