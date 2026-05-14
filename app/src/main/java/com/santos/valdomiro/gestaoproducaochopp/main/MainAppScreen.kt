package com.santos.valdomiro.gestaoproducaochopp.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.santos.valdomiro.gestaoproducaochopp.common.AppDrawer
import com.santos.valdomiro.gestaoproducaochopp.navigation.AppNavigation
import com.santos.valdomiro.gestaoproducaochopp.navigation.Route
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen (
    startDestination: String
) {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = currentBackStackEntry?.destination?.route ?: startDestination

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                selectedRoute = selectedRoute,
                onItemClick = { route ->
                    navController.navigate(route.route) {
                        popUpTo(Route.HomeRoute.route) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }

                    scope.launch {
                        drawerState.close()
                    }
                },
                onLogoutClick = {
                    scope.launch {
                        drawerState.close()
                    }

                    // Aqui você coloca a lógica de logout depois
                    // Exemplo:
                    // authViewModel.logout()
                    // navController.navigate(Route.LoginRoute.route) {
                    //     popUpTo(0)
                    // }
                }
            )
        }
    ) {
        Scaffold { paddingValues ->
            AppNavigation(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues),
                onOpenDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}
