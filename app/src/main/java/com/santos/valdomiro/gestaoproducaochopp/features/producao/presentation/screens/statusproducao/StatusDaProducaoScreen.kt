package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.statusproducao

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.components.ErroComponent
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.buscarproducao.BuscarProducaoDetalhadaViewModel
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.AppTopBarColors
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusDaProducaoScreen(
    producaoId: String,
    modifier: Modifier = Modifier,
    viewmodel: BuscarProducaoDetalhadaViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val navController = LocalNavController.current
    val state by viewmodel.uiState.collectAsState()


    LaunchedEffect(producaoId) {
        viewmodel.buscarProducaoDatalhada(producaoId = producaoId)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Status da Produção")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                windowInsets = WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = AppTopBarColors.titleColor(),
                    actionIconContentColor = AppTopBarColors.titleColor()
                )
            )
        }
    ) { innerPadding ->

        when (val currentState = state) {

            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding.calculateTopPadding()),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            is UiState.Success -> {
                val pdDetalhada = currentState.data

                val producao = pdDetalhada.producao
                val barrilNome = pdDetalhada.barril.nome
                val produtoNome = pdDetalhada.produto.nome

                val quantidadeProgramada = producao.quantidadeProgramada
                val quantidadeProduzida = producao.quantidadeProduzida
                val quantidadePendente = quantidadeProgramada - quantidadeProduzida

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Dimens.paddingHorizontal),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HeaderStatusProducao(
                        titulo = "$produtoNome $barrilNome"
                    )

                    LinhaStatusProducao(
                        texto = "Quantidade Programada",
                        quantidade = quantidadeProgramada.toString(),
                        corDeFundo = Color(0xFF64B5F6)
                    )

                    LinhaStatusProducao(
                        texto = "Quantidade Produzida",
                        quantidade = quantidadeProduzida.toString(),
                        corDeFundo = Color(0xFF81C784)
                    )

                    LinhaStatusProducao(
                        texto = "Falta Produzir",
                        quantidade = quantidadePendente.toString(),
                        corDeFundo = Color(0xFFFFCDD2)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    GraficoPizzaProducao(
                        produzido = quantidadeProduzida,
                        pendente = quantidadePendente
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LegendaGraficoProducao()
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding.calculateTopPadding()),
                    contentAlignment = Alignment.Center
                ) {
                    ErroComponent(
                        mensagem = currentState.message
                    )
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}