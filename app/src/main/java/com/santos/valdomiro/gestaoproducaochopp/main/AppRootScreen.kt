package com.santos.valdomiro.gestaoproducaochopp.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.santos.valdomiro.gestaoproducaochopp.common.screens.splashscreen.SplashScreen
import com.santos.valdomiro.gestaoproducaochopp.navigation.Route

@Composable
fun AppRootScreen() {
    var splashFinalizada by remember {
        mutableStateOf(false)
    }

    if (splashFinalizada) {
        MainAppScreen(
            startDestination = Route.ListaGradesRoute.route
        )
    } else {
        SplashScreen(
            onFinish = {
                splashFinalizada = true
            }
        )
    }
}