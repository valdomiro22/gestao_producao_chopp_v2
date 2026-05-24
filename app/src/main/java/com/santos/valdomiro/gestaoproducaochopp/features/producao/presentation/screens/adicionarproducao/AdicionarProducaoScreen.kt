package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.adicionarproducao

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
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
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.listabarris.ListaBarrisViewModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.components.DropdownBarril
import com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.components.DropdownProduto
import com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.screens.listaprodutos.ListaProdutosViewModel
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarProducaoScreen(
    gradeId: String,
    producaoViewModel: AdicionarProducaoViewModel = hiltViewModel(),
    produtosViewModel: ListaProdutosViewModel = hiltViewModel(),
    barrisViewModel: ListaBarrisViewModel = hiltViewModel()
) {
    val producaoState by producaoViewModel.uiState.collectAsState()
    val produtosUiState by produtosViewModel.uiState.collectAsState()
    val barrisUiState by barrisViewModel.uiState.collectAsState()

    val navController = LocalNavController.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        produtosViewModel.getAll()
        barrisViewModel.getAll()
    }

    LaunchedEffect(producaoState.isSuccess) {
        if (producaoState.isSuccess) {
            navController.popBackStack()
            Toast.makeText(context, "Produção salva", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Produção") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                windowInsets = WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6450A1),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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

            when (val state = produtosUiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Carregando produtos...",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                is UiState.Success -> {
                    DropdownProduto(
                        listaProdutos = state.data,
                        produtoIdAtual = producaoState.produtoId,
                        onProdutoSelecionado = producaoViewModel::onProdutoChanged,
                        navController = navController
                    )
                }

                is UiState.Error -> {
                    Text(
                        text = "Erro ao carregar produtos: ${state.message}",
                        color = Color.Red
                    )
                }

                is UiState.Idle -> {
                    Text("Aguardando carregamento dos produtos...")
                }
            }
            if (producaoState.erroProduto != null) ErroComponent(producaoState.erroProduto!!)
            Spacer(modifier = Modifier.height(8.dp))

            when (val state = barrisUiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Carregando barris...",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                is UiState.Success -> {
                    DropdownBarril(
                        listaBarris = state.data,
                        barrilIdAtual = producaoState.barrilId,
                        onBarrilSelecionado = producaoViewModel::onBarrilChanged,
                        navController = navController
                    )
                }

                is UiState.Error -> {
                    Text(
                        text = "Erro ao carregar barris: ${state.message}",
                        color = Color.Red
                    )
                }

                is UiState.Idle -> {
                    Text("Aguardando carregamento dos barris...")
                }
            }
            if (producaoState.erroBarril != null) ErroComponent(producaoState.erroBarril!!)
            Spacer(modifier = Modifier.height(8.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = producaoState.quantidadeProgramada,
                onValueChange = producaoViewModel::onQtProgramadaChanged,
                isErro = producaoState.erroQuantidadeProgramada != null,
                inputType = KeyboardType.Text,
                placeholder = "Ex: 740",
                label = "Quantidade Produzida",
            )
            if (producaoState.erroQuantidadeProgramada != null) ErroComponent(producaoState.erroQuantidadeProgramada!!)
            Spacer(modifier = Modifier.height(8.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = producaoState.quantidadeProduzida,
                onValueChange = producaoViewModel::onQtProduzidaChanged,
                isErro = producaoState.erroQuantidadeProduzida != null,
                inputType = KeyboardType.Text,
                placeholder = "Ex: 120",
                label = "Quantidade Produzida",
            )
            if (producaoState.erroQuantidadeProduzida != null) ErroComponent(producaoState.erroQuantidadeProduzida!!)
            if (producaoState.erroGeral != null) ErroComponent(producaoState.erroGeral!!)
            Spacer(modifier = Modifier.height(24.dp))

            ButtomFillMaxWidth(
                onClick = { producaoViewModel.inserirProducao(gradeId = gradeId,) },
                text = "Salvar"
            )

            if (producaoState.isLoading) CarregandoComponent(cor = Color.Magenta)

        }
    }
}
