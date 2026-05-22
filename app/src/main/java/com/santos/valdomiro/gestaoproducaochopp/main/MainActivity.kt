package com.santos.valdomiro.gestaoproducaochopp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.santos.valdomiro.gestaoproducaochopp.navigation.Route
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.GestaoProducaoChoppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            GestaoProducaoChoppTheme {
                MainAppScreen(
                    startDestination = Route.ListaGradesRoute.route
//                    startDestination = Route.AdicionarProdutoRoute.route
                )
            }
        }
    }
}