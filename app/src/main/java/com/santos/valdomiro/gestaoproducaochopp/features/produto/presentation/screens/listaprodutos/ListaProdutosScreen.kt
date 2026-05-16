package com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.screens.listaprodutos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.components.EmptyListState
import com.santos.valdomiro.gestaoproducaochopp.common.components.ErroComponent
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.components.ItemListaProduto
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.navigation.Route
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.AppTopBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaProdutosScreen(
    viewModel: ListaProdutosViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAll()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de Produtos",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                windowInsets = WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = AppTopBarColors.titleColor(),
                )
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { navController.navigate(Route.AdicionarProdutoRoute.route) },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color(0xFFFFFFFF)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Grade"
                )
            }
        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.isError -> {
                ErroComponent(
                    mensagem = (state as? UiState.Error)?.message
                        ?: "Erro desconhecido ao listar produtos"
                )
            }

            state.isSuccess -> {
                val listaProdutos =
                    (state as? UiState.Success<List<ProdutoEntity>>)?.data ?: emptyList()

                if (listaProdutos.isEmpty()) {
                    EmptyListState(mensagem = "Toque no botão + para adicionar um produto e utilizar na produção.")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = innerPadding.calculateTopPadding(),
                                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                                bottom = 0.dp
                            )
                            .padding(start = 10.dp, top = 8.dp, end = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = listaProdutos,
                            key = { produto -> produto.id }
                        ) { produto ->
                            ItemListaProduto(
                                produto = produto,
                                onEditarClick = {
                                    navController.navigate(
                                        Route.EditarProdutoRoute.criarRota(
                                            produtoId = produto.id
                                        )
                                    )
                                },
                                onDeletarClick = { viewModel.deleteProduto(produto = produto) },
                            )
                        }
                    }
                }
            }
        }
    }

}