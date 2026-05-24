package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.simularfimproducao

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.components.ButtomFillMaxWidth
import com.santos.valdomiro.gestaoproducaochopp.common.components.CustomOutlinedTextField
import com.santos.valdomiro.gestaoproducaochopp.common.components.ErroComponent
import com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.components.CardNomeValor
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.AppTopBarColors
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimularFimProducaoScreen(
    producaoId: String,
    viewModel: SimularFimProducaoViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.carregarDadosIniciais(producaoId = producaoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Simular fim de produção") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
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
    ) { contentPadding ->
        when {
            state.producao != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .padding(horizontal = Dimens.paddingHorizontal)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.qtProgramada,
                        onValueChange = viewModel::onQtProgramadaChanged,
                        placeholder = "Ex: 392",
                        isErro = state.erroQtProgramada != null,
                        inputType = KeyboardType.Number,
                        label = "Quantidade programada",
                    )
                    if (state.erroQtProgramada != null) ErroComponent(state.erroQtProgramada!!)

                    CustomOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.qtProduzida,
                        onValueChange = viewModel::onQtProduzidaChanged,
                        placeholder = "Ex: 392",
                        isErro = state.erroQtProduzida != null,
                        inputType = KeyboardType.Number,
                        label = "Quantidade produzida"
                    )
                    if (state.erroQtProduzida != null) ErroComponent(state.erroQtProduzida!!)

                    CustomOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.nivelMaxTanque,
                        onValueChange = viewModel::onNivelMaxChanged,
                        placeholder = "Ex: 392",
                        isErro = state.erroNivelMaxTanque != null,
                        inputType = KeyboardType.Number,
                        label = "Nível máximo do Buffer"
                    )
                    if (state.erroNivelMaxTanque != null) ErroComponent(state.erroNivelMaxTanque!!)

                    Spacer(modifier = Modifier.height(8.dp))

                    ButtomFillMaxWidth(
                        text = "Simular",
                        onClick = { viewModel.simular() }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CardNomeValor(titulo = "Tipo de barril", valor = "30L")

                    CardNomeValor(
                        titulo = "Volume necessário",
                        valor = if (state.vlNecessario.isNullOrBlank()) "-" else "${state.vlNecessario} hl",
                        destaque = true // Pode usar isso para destacar o resultado final
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.erro != null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(contentPadding),
                    contentAlignment = Alignment.Center
                ) {
                    ErroComponent(mensagem = (state.erro!!))
                }
            }
        }
    }
}