package com.santos.valdomiro.gestaoproducaochopp.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.usecase.SincronizacaoInicialViewModel
import com.santos.valdomiro.gestaoproducaochopp.navigation.Route

@Composable
fun AppRootScreen(
    viewModel: SincronizacaoInicialViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.sincronizarAoAbrirApp()
    }

    MainAppScreen(
        startDestination = Route.ListaGradesRoute.route
    )
}