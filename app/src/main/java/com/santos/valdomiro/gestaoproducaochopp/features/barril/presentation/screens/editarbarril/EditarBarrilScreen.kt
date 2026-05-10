package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.editarbarril

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.components.ButtomFillMaxWidth
import com.santos.valdomiro.gestaoproducaochopp.common.components.CarregandoComponent
import com.santos.valdomiro.gestaoproducaochopp.common.components.CustomOutlinedTextField
import com.santos.valdomiro.gestaoproducaochopp.common.components.ErroComponent
import com.santos.valdomiro.gestaoproducaochopp.common.components.LinhaTextoComSwitch
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarBarrilScreen(
    barrilId: String,
    viewModel: EditarBarrilViewModel = hiltViewModel(),
) {

    val state by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val context = LocalContext.current

    LaunchedEffect(barrilId) {
        viewModel.carregarBarril(barrilId)
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.popBackStack()
            Toast.makeText(context, "Barril atualizado", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Barril") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                windowInsets = WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6450A1), // Sua cor de fundo (Ex: Roxo)
                    titleContentColor = Color.White,    // Cor padrão do título
                    navigationIconContentColor = Color.White // Cor padrão do ícone
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text("Nome")
            CustomOutlinedTextField(
                value = state.nome,
                onValueChange = viewModel::onNomeChanged,
                isErro = state.erroNome != null,
                inputType = KeyboardType.Text,
                placeholder = "Nome"
            )
            if (state.erroNome != null) ErroComponent(state.erroNome!!)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Volume")
            CustomOutlinedTextField(
                value = state.volume,
                onValueChange = viewModel::onVolumeChanged,
                isErro = state.erroVolume != null,
                placeholder = "Volume",
                inputType = KeyboardType.Number
            )
            if (state.erroVolume != null) ErroComponent(state.erroVolume!!)

            LinhaTextoComSwitch(
                texto = "Barril descartável",
                onCheckedChange = viewModel::onDescartavelChanged,
                isCheck = state.descartavel
            )

            Spacer(modifier = Modifier.height(16.dp))

            ButtomFillMaxWidth(
                onClick = { viewModel.atualizar() },
                nome = "Salvar"
            )

            if (state.isLoading) CarregandoComponent(cor = Color.Magenta)

        }

    }
}