package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.editarbarril

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.components.ButtomFillMaxWidth
import com.santos.valdomiro.gestaoproducaochopp.common.components.CarregandoComponent
import com.santos.valdomiro.gestaoproducaochopp.common.components.CustomOutlinedTextField
import com.santos.valdomiro.gestaoproducaochopp.common.components.ErroComponent
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.AppTopBarColors
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarBarrilScreen(
    barrilId: String,
    modifier: Modifier = Modifier,
    viewModel: EditarBarrilViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.buscarBarril(barrilId = barrilId)
    }

    LaunchedEffect(state.isEditSuccess) {
        if (state.isEditSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Barril",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                windowInsets = WindowInsets(0),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = AppTopBarColors.titleColor(),
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.paddingHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.nome,
                onValueChange = viewModel::onNomeChanged,
                isErro = state.erroNome != null,
                label = "Nome",
                inputType = KeyboardType.Text
            )
            if (state.erroNome != null) ErroComponent(state.erroNome!!)
            Spacer(modifier = Modifier.height(8.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.volume,
                onValueChange = viewModel::onVolumeChanged,
                isErro = state.erroVolume != null,
                label = "Volume",
                inputType = KeyboardType.Number
            )
            if (state.erroVolume != null) ErroComponent(state.erroVolume!!)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val descartavel = state.descartavel
                Text(
                    text = if (descartavel) "Descartável" else "Retornável"
                )

                Switch(
                    onCheckedChange = viewModel::onDescartavelChanged,
                    checked = state.descartavel,
                )
            }
            if (state.erroGeral != null) ErroComponent(state.erroGeral!!)
            Spacer(modifier = Modifier.height(16.dp))

            ButtomFillMaxWidth(
                onClick = { viewModel.editarBarril() },
                text = "Editar"
            )

            if (state.isLoading) CarregandoComponent(cor = MaterialTheme.colorScheme.primary)
        }
    }

}