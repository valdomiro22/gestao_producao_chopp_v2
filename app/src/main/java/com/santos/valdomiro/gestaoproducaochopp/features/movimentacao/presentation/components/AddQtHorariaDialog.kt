package com.santos.valdomiro.gestaoproducaochopp.features.movimentacaoproducao.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.components.ButtomFillMaxWidth
import com.santos.valdomiro.gestaoproducaochopp.common.components.ErroComponent
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.components.ActionChipCustom
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.screens.adicionarmovimentacao.AdicionarMovimentacaoViewModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import com.santos.valdomiro.gestaoproducaochopp.navigation.LocalNavController

@Composable
fun AddQtHorariaDialog(
    horario: String,
    producao: ProducaoEntity,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: AdicionarMovimentacaoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val context = LocalContext.current

    val adicionar = { valor: Int ->
        val qtAtual = state.quantidade.toIntOrNull() ?: 0
        val novaQuantidade = qtAtual + valor
        viewModel.onQuantidadeChanged(novaQuantidade.toString())
    }

    val subtrair = { valor: Int ->
        val qtAtual = state.quantidade.toIntOrNull() ?: 0
        val novaQuantidade = qtAtual - valor
        viewModel.onQuantidadeChanged(novaQuantidade.toString())
    }

    // Fica de olho no estado de sucesso do ViewModel de inserção
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onSuccess() // Avisa a tela principal para recarregar
            onDismiss() // Fecha o dialog
            viewModel.resetState()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Barris produzidos",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Horario: $horario",
                    color = Color.Black,
                    fontSize = 11.sp,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.quantidade,
                    onValueChange = viewModel::onQuantidadeChanged,
                    isError = state.erroQuantidade != null,
                    placeholder = { Text("Ex: 50") },
                    label = { Text("Quantidade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                if (state.erroQuantidade != null) ErroComponent(state.erroQuantidade!!)
                Spacer(modifier = Modifier.height(4.dp))

                // Chips de incremento
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ActionChipCustom(
                        valor = 1,
                        isIncrement = true,
                        onClick = { adicionar(1) }
                    )
                    ActionChipCustom(
                        valor = 5,
                        isIncrement = true,
                        onClick = { adicionar(5) }
                    )
                    ActionChipCustom(
                        valor = 10,
                        isIncrement = true,
                        onClick = { adicionar(10) }
                    )
                    ActionChipCustom(
                        valor = 20,
                        isIncrement = true,
                        onClick = { adicionar(20) }
                    )
                }

                // Chips de decremento
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ActionChipCustom(
                        valor = 1,
                        isIncrement = false,
                        onClick = { subtrair(1) }
                    )
                    ActionChipCustom(
                        valor = 5,
                        isIncrement = false,
                        onClick = { subtrair(5) }
                    )
                    ActionChipCustom(
                        valor = 10,
                        isIncrement = false,
                        onClick = { subtrair(10) }
                    )
                    ActionChipCustom(
                        valor = 20,
                        isIncrement = false,
                        onClick = { subtrair(20) }
                    )
                }

                ButtomFillMaxWidth(
                    text = "Adicionar",
                    onClick = {
//                        viewModel.setProducaoId(producao.id!!)
//                        viewModel.setHorario(horario)
                        viewModel.inserirMovimentacal(producaoId = producao.id, horarioTurno = horario)
                    }
                )
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}