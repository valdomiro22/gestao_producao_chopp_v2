package com.santos.valdomiro.gestaoproducaochopp.common.screens.verificarvalidade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.santos.valdomiro.gestaoproducaochopp.common.AppDrawer
import com.santos.valdomiro.gestaoproducaochopp.common.components.DatePickerDialogComponent
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.AppTopBarColors
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.Dimens
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificarValidadeScreen(
    onOpenDrawer: () -> Unit,
    viewModel: VerificarValidadeViewModel = viewModel()
) {
    var mostrarDatePickerInicio by remember { mutableStateOf(false) }
    var mostrarDatePickerFim by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val formatter = remember {
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
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
                    title = { Text("Verificar Validade") }, // Título atualizado para a funcionalidade
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
                        navigationIconContentColor = AppTopBarColors.titleColor(),
                        actionIconContentColor = AppTopBarColors.titleColor()
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = Dimens.SpaceM),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cards de seleção de data lado a lado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DateSelectionCard(
                        modifier = Modifier.weight(1f),
                        title = "Data Inicial",
                        date = viewModel.dataInicio,
                        formatter = formatter,
                        onClick = { mostrarDatePickerInicio = true }
                    )

                    DateSelectionCard(
                        modifier = Modifier.weight(1f),
                        title = "Data Final",
                        date = viewModel.dataFim,
                        formatter = formatter,
                        onClick = { mostrarDatePickerFim = true }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Card de Resultado em Destaque
                ResultCard(quantidadeDias = viewModel.quantidadeDias)
            }
        }

        if (mostrarDatePickerInicio) {
            DatePickerDialogComponent(
                onDismiss = { mostrarDatePickerInicio = false },
                initialDate = viewModel.dataInicio ?: LocalDate.now(),
                onConfirm = { dataSelecionada ->
                    viewModel.atualizarDataInicio(dataSelecionada)
                    mostrarDatePickerInicio = false
                }
            )
        }

        if (mostrarDatePickerFim) {
            DatePickerDialogComponent(
                onDismiss = { mostrarDatePickerFim = false },
                initialDate = viewModel.dataFim ?: LocalDate.now(),
                onConfirm = { dataSelecionada ->
                    viewModel.atualizarDataFim(dataSelecionada)
                    mostrarDatePickerFim = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelectionCard(
    modifier: Modifier = Modifier,
    title: String,
    date: LocalDate?,
    formatter: DateTimeFormatter,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = date?.format(formatter) ?: "--/--/----",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (date != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun ResultCard(quantidadeDias: Long?) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.CalendarMonth,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 8.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Quantidade de Dias",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (quantidadeDias == null) {
                Text(
                    text = "Aguardando datas",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$quantidadeDias",
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (quantidadeDias == 1L) " dia" else " dias",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}