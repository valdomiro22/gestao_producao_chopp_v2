package com.santos.valdomiro.gestaoproducaochopp.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(
    val route: String,
    val title: String = "",
    val icon: ImageVector? = null
) {
    data object HomeRoute: Route(route = "home", title = "Home")
    data object Tela2Route: Route(route = "tela2", title = "Tela 2")
    data object AdicionarBarrilRoute: Route(route = "adicionar-barril", title = "Adicionar Barril")
    data object AdicionarProdutoRoute: Route(route = "adicionar-produto", title = "Adicionar Produto")
    data object ListaBarrisRoute: Route(route = "lista-barris", title = "Lista de Barris")
    data object ListaProdutosRoute: Route(route = "lista-produtos", title = "Lista de Produtos")

    data object EditarBarrilRoute: Route(route = "editar-barril/{barrilId}", title = "Editar Barril") {
        fun criarRota(barrilId: String) = "editar-barril/$barrilId"
    }
    data object EditarProdutoRoute: Route(route = "editar-produto/{produtoId}", title = "Editar Produto") {
        fun criarRota(produtoId: String) = "editar-produto/$produtoId"
    }
}
