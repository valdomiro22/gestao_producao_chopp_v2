package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.components.ErroComponent
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.screens.adicionarmovimentacao.AdicionarMovimentacaoViewModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import kotlin.math.abs

@Composable
fun AddQtHorariaDialog(
    horario: String,
    producao: ProducaoEntity,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AdicionarMovimentacaoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onSuccess()
            onDismiss()
            viewModel.resetState()
        }
    }

    fun somarAoInput(valor: Int) {
        val qtAtual = state.quantidade.toIntOrNull() ?: 0
        val novaQuantidade = qtAtual + valor
        viewModel.onQuantidadeChanged(novaQuantidade.toString())
    }

    fun salvarMovimentacao(ehPositivo: Boolean) {
        val qtAtual = state.quantidade.toIntOrNull() ?: 0

        val quantidadeFinal = if (ehPositivo) {
            abs(qtAtual)
        } else {
            abs(qtAtual) * -1
        }

        viewModel.onQuantidadeChanged(quantidadeFinal.toString())

        viewModel.inserirMovimentacao(
            producaoId = producao.id,
            horarioTurno = horario
        )
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = horario,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Movimentação do horário",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedTextField(
                    value = state.quantidade,
                    onValueChange = viewModel::onQuantidadeChanged,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "0",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    singleLine = true,
                    isError = state.erroQuantidade != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    textStyle = MaterialTheme.typography.displaySmall.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                )

                if (state.erroQuantidade != null) {
                    ErroComponent(state.erroQuantidade!!)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(1, 5, 10, 20).forEach { valor ->
                        OutlinedButton(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            onClick = { somarAoInput(valor) },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "+$valor",
                                maxLines = 1,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { salvarMovimentacao(false) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            text = "Retirar",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    ElevatedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { salvarMovimentacao(true) },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Adicionar",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}