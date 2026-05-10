package com.santos.valdomiro.gestaoproducaochopp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            composable(Route.Tela2Route.route) {
                Tela2(
                    onOpenDrawer = onOpenDrawer
                )
            }
        }
    }

}