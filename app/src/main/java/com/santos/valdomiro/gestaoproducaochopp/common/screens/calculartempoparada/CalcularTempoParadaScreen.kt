package com.santos.valdomiro.gestaoproducaochopp.common.screens.calculartempoparada

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Timer
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.santos.valdomiro.gestaoproducaochopp.common.AppDrawer
import com.santos.valdomiro.gestaoproducaochopp.common.components.TimePickerDialogComponent
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.AppTopBarColors
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.Dimens
import java.time.Duration
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalcularTempoParadaScreen(
    onOpenDrawer: () -> Unit,
    viewModel: CalcularTempoParadaViewModel = viewModel()
) {
    val inicio = viewModel.inicio
    val fim = viewModel.fim
    val diferenca = viewModel.diferenca

    var mostrarDialogInicio by remember { mutableStateOf(false) }
    var mostrarDialogFim by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

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
                    title = { Text("Calculadora de Horas") },
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
                // Cards de seleção de horário lado a lado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TimeSelectionCard(
                        modifier = Modifier.weight(1f),
                        title = "Início",
                        time = inicio,
                        onClick = { mostrarDialogInicio = true }
                    )

                    TimeSelectionCard(
                        modifier = Modifier.weight(1f),
                        title = "Fim",
                        time = fim,
                        onClick = { mostrarDialogFim = true }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Card de Resultado em Destaque
                ResultCard(diferenca = diferenca)
            }
        }

        // Dialogs
        if (mostrarDialogInicio) {
            TimePickerDialogComponent(
                onDismiss = { mostrarDialogInicio = false },
                onConfirm = { selectedTime ->
                    viewModel.atualizarInicio(selectedTime)
                    mostrarDialogInicio = false
                },
                initialTime = inicio ?: LocalTime.now()
            )
        }

        if (mostrarDialogFim) {
            TimePickerDialogComponent(
                onDismiss = { mostrarDialogFim = false },
                onConfirm = { selectedTime ->
                    viewModel.atualizarFim(selectedTime)
                    mostrarDialogFim = false
                },
                initialTime = fim ?: LocalTime.now()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeSelectionCard(
    modifier: Modifier = Modifier,
    title: String,
    time: LocalTime?,
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
                    imageVector = Icons.Rounded.AccessTime,
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
                text = time?.let { formatarHora(it) } ?: "--:--",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = if (time != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun ResultCard(diferenca: Duration?) {
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
                imageVector = Icons.Rounded.Timer,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 8.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Tempo Total de Parada",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (diferenca == null) {
                Text(
                    text = "Aguardando horários",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                val horas = diferenca.toHours()
                val minutos = diferenca.toMinutes() % 60

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${horas}h",
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = " ${minutos}m",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
private fun formatarHora(time: LocalTime): String {
    return String.format("%02d:%02d", time.hour, time.minute)
}