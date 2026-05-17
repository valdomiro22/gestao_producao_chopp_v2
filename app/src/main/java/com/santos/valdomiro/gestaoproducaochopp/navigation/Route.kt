package com.santos.valdomiro.gestaoproducaochopp.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(
    val route: String,
    val title: String = "",
    val icon: ImageVector? = null
) {
    data object HomeRoute: Route(route = "home", title = "Home")

    // Barril
    data object AdicionarBarrilRoute: Route(route = "adicionar-barril", title = "Adicionar Barril")
    data object ListaBarrisRoute: Route(route = "lista-barris", title = "Lista de Barris")
    data object EditarBarrilRoute: Route(route = "editar-barril/{barrilId}", title = "Editar Barril") {
        fun criarRota(barrilId: String) = "editar-barril/$barrilId"
    }

    // Produto
    data object AdicionarProdutoRoute: Route(route = "adicionar-produto", title = "Adicionar Produto")
    data object ListaProdutosRoute: Route(route = "lista-produtos", title = "Lista de Produtos")
    data object EditarProdutoRoute: Route(route = "editar-produto/{produtoId}", title = "Editar Produto") {
        fun criarRota(produtoId: String) = "editar-produto/$produtoId"
    }

    // Grade
    data object AdicionarGradeRoute: Route(route = "adicionar-grade", title = "Adicionar Grade")
    data object ListaGradesRoute: Route(route = "lista-grades", title = "Lista de Grades")
    data object EditarGradeRoute: Route(route = "editar-grade/{gradeId}", title = "Editar Grade") {
        fun criarRota(gradeId: String) = "editar-grade/$gradeId"
    }

}
