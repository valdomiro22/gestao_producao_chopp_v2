package com.santos.valdomiro.gestaoproducaochopp.features.homescreen.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.AppDrawer
import com.santos.valdomiro.gestaoproducaochopp.common.enums.Turno
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.homescreen.components.CardStatusProducaoComponent
import com.santos.valdomiro.gestaoproducaochopp.features.homescreen.components.QuantidadeHorariaComponent
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.screens.listamvproducao.MapMovimentacoesDaProducaoViewModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.components.ControleDoBuffer
import com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.components.ProducaoNaoEncontradaComponent
import com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.buscarproducao.BuscarProducaoDetalhadaViewModel
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.navigation.Route
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.AppTopBarColors
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.Dimens
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.OnStatusPendente
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.OnStatusProduzido
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.OnStatusProgramado
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.OnTurnoNaoSelecionadoDark
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.OnTurnoNaoSelecionadoLight
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.OnTurnoSelecionado
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.StatusPendente
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.StatusProduzido
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.StatusProgramado
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.TurnoNaoSelecionadoDark
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.TurnoNaoSelecionadoLight
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.TurnoSelecionado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    producaoId: String,
    onOpenDrawer: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    buscarProducaoDetalhadaViewModel: BuscarProducaoDetalhadaViewModel = hiltViewModel(),
    mapMovimentacoesViewModel: MapMovimentacoesDaProducaoViewModel = hiltViewModel(),
) {

    val darkTheme = isSystemInDarkTheme()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val state by buscarProducaoDetalhadaViewModel.uiState.collectAsState()
    val movimentacaoState by mapMovimentacoesViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var menuExpandido by remember { mutableStateOf(false) }  // Para o controle do DropdownMenu
    val turnoAtual by homeViewModel.turnoSelecionado.collectAsState()

    LaunchedEffect(Unit) {
        buscarProducaoDetalhadaViewModel.buscarProducaoDatalhada(producaoId)
        mapMovimentacoesViewModel.getMovimentacoesDaProducao(producaoId = producaoId)
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
                    ProducaoNaoEncontradaComponent(
                        mensagem = "Selecione uma Produção na tela da lista de Produções",
                        goListaDeGrades = {
                            buscarProducaoDetalhadaViewModel.buscarProducaoDatalhada(producaoId)
                        },
                        onVoltar = {
                            navController.popBackStack()
                        }
                    )
                    return@ModalNavigationDrawer
                }

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
                                        text = { Text("Histórico") },
                                        onClick = {
                                            menuExpandido = false
                                            navController.navigate(
                                                Route.ListaMovimentacaoRoute.criarRota(
                                                    producaoId = producaoId
                                                )
                                            )
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Opções") },
                                        onClick = {
//                                            menuExpandido = false
//                                            navController.navigate(Route.ListaProdutosRoute.route)
                                            Toast.makeText(context, "Opções", Toast.LENGTH_SHORT).show()
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
                                navigationIconContentColor = AppTopBarColors.titleColor(),
                                actionIconContentColor = AppTopBarColors.titleColor()
                            )
                        )
                    },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding())
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = Dimens.paddingHorizontal),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CardStatusProducaoComponent(
                                titulo = "Programado",
                                quantidade = pdDetalhada.producao.quantidadeProgramada.toString(),
                                backGround = StatusProgramado,
                                contentColor = OnStatusProgramado
                            )

                            CardStatusProducaoComponent(
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        Route.StatusDaProducaoRoute.criarRota(producaoId = producaoId)
                                    )
                                },
                                titulo = "Produzido",
                                quantidade = pdDetalhada.producao.quantidadeProduzida.toString(),
                                backGround = StatusProduzido,
                                contentColor = OnStatusProduzido
                            )

                            CardStatusProducaoComponent(
                                titulo = "Pendente",
                                quantidade = pdDetalhada.producao.quantidadePendente.toString(),
                                backGround = StatusPendente,
                                contentColor = OnStatusPendente
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
                                    if (selecionado) {
                                        TurnoSelecionado
                                    } else {
                                        if (darkTheme) TurnoNaoSelecionadoDark else TurnoNaoSelecionadoLight
                                    }

                                val corTexto =
                                    if (selecionado) {
                                        OnTurnoSelecionado
                                    } else {
                                        if (darkTheme) OnTurnoNaoSelecionadoDark else OnTurnoNaoSelecionadoLight
                                    }

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
                                        color = corTexto,
                                        fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        val mapaQuantidades =
                            (movimentacaoState as? UiState.Success)?.data ?: emptyMap()

                        // Seção de horários do turno selecionado
                        val listaDeHorarios = turnoAtual.horarios
                        QuantidadeHorariaComponent(
                            horarios = listaDeHorarios,
                            producao = pdDetalhada.producao,
                            quantidades = mapaQuantidades,
                            onRefresh = {
                                mapMovimentacoesViewModel.getMovimentacoesDaProducao(producaoId = producaoId)
                                buscarProducaoDetalhadaViewModel.buscarProducaoDatalhada(producaoId)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Informações para o final de produção
                        ControleDoBuffer(
                            onClick = {
                                navController.navigate(
                                    Route.SimularFimProducaoRoute.criarRota(producaoId = producaoId)
                                )
                            },
                            quantidadePendente = pdDetalhada.quantidadePendente,
                            pdDetalhada = pdDetalhada,
                            barril = pdDetalhada.barril
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                    }
                }
            }

            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            state.isError -> {
                ProducaoNaoEncontradaComponent(
                    mensagem = "Selecione uma Produção na tela da lista de Produções",
                    goListaDeGrades = {
                        navController.navigate(Route.ListaGradesRoute.route)
                    },
                    onVoltar = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}