package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.listaproducoes

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.AppDrawer
import com.santos.valdomiro.gestaoproducaochopp.common.components.AlertaDialogDeletar
import com.santos.valdomiro.gestaoproducaochopp.common.components.EmptyListState
import com.santos.valdomiro.gestaoproducaochopp.common.components.ErroComponent
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoDetalhada
import com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.components.ItemListaProducao
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.navigation.Route
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.AppTopBarColors
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.Dimens
import com.santos.valdomiro.gestaoproducaochopp.util.TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaProducaoScreen(
    onOpenDrawer: () -> Unit,
    gradeId: String,
    viewModel: ListaProducaoViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsState()
    var dialogDeletar by remember { mutableStateOf(false) }

    LaunchedEffect(gradeId) {
        viewModel.getAllDaGrade(gradeId = gradeId)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                selectedRoute = "",
                onItemClick = {},
                onLogoutClick = {}
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Lista de Produções",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onOpenDrawer) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Abrir Menu"
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
            },
            floatingActionButton = {
                LargeFloatingActionButton(
                    onClick = {
                        navController.navigate(
                            Route.AdicionarProducaoRoute.criarRota(
                                gradeId = gradeId
                            )
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar Produção"
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
                            ?: "Erro desconhecido ao listar produções"
                    )
                }

                state.isSuccess -> {
                    val listaProducoes = (state as? UiState.Success<List<ProducaoDetalhada>>)?.data ?: emptyList()

                    if (listaProducoes.isEmpty()) {
                        EmptyListState(mensagem = "Toque no botão + para adicionar uma produção.")
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
                                .padding(horizontal = Dimens.paddingHorizontal),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                items = listaProducoes,
                                key = { item -> item.producao.id }
                            ) { item ->
                                ItemListaProducao(
                                    producao = item.producao,
                                    barril = item.barril,
                                    produto = item.produto,
                                    onDeletarClick = { dialogDeletar = true },
                                    onEditarClick = { navController.navigate(Route.EditarProducaoRoute.criarRota(item.producao.id)) },
                                    navController = navController,
                                )

                                if (dialogDeletar) {
                                    AlertaDialogDeletar(
                                        onDismiss = { dialogDeletar = false },
                                        onConfirm = {
                                            viewModel.deletarProducao(producao = item.producao)
                                            Toast.makeText(
                                                context,
                                                "Grade deletada",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        mensagem = "Está ação não poderá ser desfeita, você realmente deseja deletar está produção?",
                                        icone = Icons.Default.Delete
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}