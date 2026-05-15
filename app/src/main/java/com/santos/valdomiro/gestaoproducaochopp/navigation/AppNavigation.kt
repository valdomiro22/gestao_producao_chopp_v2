package com.santos.valdomiro.gestaoproducaochopp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.adicionarbarril.AdicionarBarrilScreen
import com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.editarbarril.EditarBarrilScreen
import com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.listabarris.ListaBarrisScreen
import com.santos.valdomiro.gestaoproducaochopp.screens.HomeScreen
import com.santos.valdomiro.gestaoproducaochopp.screens.Tela2

@Composable
fun AppNavigation(
    navController: NavHostController,
    onOpenDrawer: () -> Unit,
    startDestination: String,
    modifier: Modifier = Modifier
) {

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(Route.HomeRoute.route) {
                HomeScreen(
                    onOpenDrawer = onOpenDrawer
                )
            }

            composable(
                route = "editar-barril/{barrilId}",
                arguments = listOf(navArgument("barrilId") { type = NavType.StringType })
            ) { backStackEntry ->
                val barrilId = backStackEntry.arguments?.getString("barrilId") ?: return@composable
                EditarBarrilScreen(barrilId = barrilId)
            }

            composable(Route.ListaBarrisRoute.route) {
                ListaBarrisScreen()
            }

            composable(Route.AdicionarBarrilRoute.route) {
                AdicionarBarrilScreen()
            }
        }
    }

}