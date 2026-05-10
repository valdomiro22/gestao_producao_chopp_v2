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
    data object EditarBarrilRoute: Route(route = "editar-barril", title = "Editar Barril")
}