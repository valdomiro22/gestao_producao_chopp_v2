package com.santos.valdomiro.gestaoproducaochopp.features.grade.presentation.screens.adicionargrade

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.features.grade.presentation.components.AppDatePicker
import com.santos.valdomiro.gestaoproducaochopp.common.components.ButtomFillMaxWidth
import com.santos.valdomiro.gestaoproducaochopp.common.components.CarregandoComponent
import com.santos.valdomiro.gestaoproducaochopp.common.components.CustomOutlinedTextField
import com.santos.valdomiro.gestaoproducaochopp.common.components.ErroComponent
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.AppTopBarColors
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarGradeScreen(
    viewModel: AdicionarGradeViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val context = LocalContext.current

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.popBackStack()
            Toast.makeText(context, "Grade salva", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Adicionar Grade",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
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
                    navigationIconContentColor = AppTopBarColors.titleColor(),
                    actionIconContentColor = AppTopBarColors.titleColor()
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.paddingHorizontal),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Numero da Grade")
            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.numero,
                onValueChange = viewModel::onNumeroChanged,
                isErro = state.erroNumero != null,
                inputType = KeyboardType.Text,
                placeholder = "Número",
                label = "Número da Grade",
            )
            if (state.erroNumero != null) ErroComponent(state.erroNumero!!)
            Spacer(modifier = Modifier.height(8.dp))

            AppDatePicker(
                selectedDate = state.data,
                onDateSelected = viewModel::onDataChanged,
                label = "Data de Produção"
            )
            if (state.erroData != null) ErroComponent(state.erroData!!)
            Spacer(modifier = Modifier.height(24.dp))

            ButtomFillMaxWidth(
                text = "Salvar",
                onClick = viewModel::inserirGrade,
            )

            if (state.isLoading) CarregandoComponent(cor = MaterialTheme.colorScheme.primary)
        }
    }
}