package com.santos.valdomiro.gestaoproducaochopp.features.homescreen.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.AppDrawer
import com.santos.valdomiro.gestaoproducaochopp.common.components.ErroComponent
import com.santos.valdomiro.gestaoproducaochopp.common.enums.Turno
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.buscarproducao.BuscarProducaoDetalhadaViewModel
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.features.homescreen.components.CardStatusProducaoComponent
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.AppTopBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    producaoId: String,
    onOpenDrawer: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    buscarProducaoDetalhadaViewModel: BuscarProducaoDetalhadaViewModel = hiltViewModel()
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val state by buscarProducaoDetalhadaViewModel.uiState.collectAsState()

    var menuExpandido by remember { mutableStateOf(false) }  // Para o controle do DropdownMenu
    val turnoAtual by homeViewModel.turnoSelecionado.collectAsState()

    LaunchedEffect(Unit) {
        buscarProducaoDetalhadaViewModel.buscarProducaoDatalhada(producaoId)
//        listaQtViewModel.carregarDadosDaProducao(producaoId)
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
        when {
            state.isSuccess -> {
                val pdDetalhada = (state as? UiState.Success)?.data ?: run {
                    ErroComponent("Produção não encontrada")
                    return@ModalNavigationDrawer
                }

//                val pendente = pdDetalhada.producao.quantidadeProgramada - pdDetalhada.producao.quantidadeProduzida
                val msgTopBarr = "${pdDetalhada.produto.nome} ${pdDetalhada.barril.nome}"


                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = msgTopBarr,
                                    textAlign = TextAlign.Center
                                )
                            },

                            actions = {
                                IconButton(onClick = { menuExpandido = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Mais opções",
                                    )
                                }

                                DropdownMenu(
                                    expanded = menuExpandido,
                                    onDismissRequest = { menuExpandido = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Barris") },
                                        onClick = {
                                            menuExpandido = false
//                                            navController.navigate(Route.ListaDeBarrisRoute.route)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Produtos") },
                                        onClick = {
                                            menuExpandido = false
//                                            navController.navigate(Route.ListaDeProdutosRoute.route)
                                        }
                                    )
                                }
                            },
                            windowInsets = WindowInsets(0),
                            navigationIcon = {
                                IconButton(
                                    onClick = onOpenDrawer
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Abrir menu"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                titleContentColor = AppTopBarColors.titleColor(),
                                actionIconContentColor = AppTopBarColors.titleColor()
                            )
                        )
                    },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CardStatusProducaoComponent(
                                titulo = "Programado",
                                quantidade = pdDetalhada.producao.quantidadeProgramada.toString(),
                                backGround = Color(0xFF1E5FDB),
                            )
                            CardStatusProducaoComponent(
                                titulo = "Produzido",
                                quantidade = pdDetalhada.producao.quantidadeProduzida.toString(),
                                backGround = Color(0xFF15AD1C),
                            )
                            CardStatusProducaoComponent(
                                titulo = "Pendente",
                                quantidade = pdDetalhada.producao.quantidadePendente.toString(),
                                backGround = Color(0xFFE52828),
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        // Seção de seleção de turno
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Turno.entries.forEach { turno ->
                                val selecionado = turno == turnoAtual
                                val corBase =
                                    if (selecionado) Color(0xFF2563EB) else Color(0xFFF0F0F0)

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(corBase)
                                        .clickable { homeViewModel.alterarTurno(turno) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = turno.label,
                                        color = if (selecionado) Color.White else Color.DarkGray,
                                        fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                    }


                }

            }

            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.isError -> {
                ErroComponent(
                    mensagem = (state as? UiState.Error)?.message
                        ?: "Erro desconhecido ao buscar produção"
                )
            }
        }
    }
}