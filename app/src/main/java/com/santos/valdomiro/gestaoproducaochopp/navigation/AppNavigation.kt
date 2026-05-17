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
import com.santos.valdomiro.gestaoproducaochopp.features.grade.presentation.screens.adicionargrade.AdicionarGradeScreen
import com.santos.valdomiro.gestaoproducaochopp.features.grade.presentation.screens.editargrade.EditarGradeScreen
import com.santos.valdomiro.gestaoproducaochopp.features.grade.presentation.screens.listagrades.ListaGradesScreen
import com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.screens.adicionarproduto.AdicionarProdutoScreen
import com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.screens.editarproduto.EditarProdutoScreen
import com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.screens.listaprodutos.ListaProdutosScreen
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

            // Barril
            composable(Route.ListaBarrisRoute.route) {
                ListaBarrisScreen()
            }
            composable(Route.AdicionarBarrilRoute.route) {
                AdicionarBarrilScreen()
            }
            composable(
                route = "editar-barril/{barrilId}",
                arguments = listOf(navArgument("barrilId") { type = NavType.StringType })
            ) { backStackEntry ->
                val barrilId = backStackEntry.arguments?.getString("barrilId") ?: return@composable
                EditarBarrilScreen(barrilId = barrilId)
            }

            // Produto
            composable(Route.ListaProdutosRoute.route) {
                ListaProdutosScreen()
            }
            composable(Route.AdicionarProdutoRoute.route) {
                AdicionarProdutoScreen()
            }
            composable(
                route = "editar-produto/{produtoId}",
                arguments = listOf(navArgument("produtoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val produtoId = backStackEntry.arguments?.getString("produtoId") ?: return@composable
                EditarProdutoScreen(produtoId = produtoId)
            }

            // Grade
            composable(Route.ListaGradesRoute.route) {
                ListaGradesScreen()
            }
            composable(Route.AdicionarGradeRoute.route) {
                AdicionarGradeScreen()
            }
            composable(
                route = "editar-grade/{gradeId}",
                arguments = listOf(navArgument("gradeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val gradeId = backStackEntry.arguments?.getString("gradeId") ?: return@composable
                EditarGradeScreen(gradeId = gradeId)
            }

        }
    }

}