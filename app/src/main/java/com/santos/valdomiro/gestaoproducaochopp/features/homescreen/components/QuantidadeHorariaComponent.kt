package com.santos.valdomiro.gestaoproducaochopp.features.homescreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.components.AddQtHorariaDialog
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity

@Composable
fun QuantidadeHorariaComponent(
    horarios: List<String>,
    producao: ProducaoEntity,
    quantidades: Map<String, MovimentacaoEntity>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showInfoDialog by remember { mutableStateOf(false) }
    var horarioSelecionado by remember { mutableStateOf("") }

    val totalProduzido = quantidades.values.sumOf { it.quantidade }

    Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 260.dp),
                contentPadding = PaddingValues(2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(horarios) { horario ->

                    val movimentacao = quantidades[horario]
                    val quantidade = movimentacao?.quantidade ?: 0

                    CardHorarioNovo(
                        horario = horario,
                        quantidade = quantidade,
                        modifier = Modifier.clickable {
                            horarioSelecionado = horario
                            showInfoDialog = true
                        }
                    )
                }
            }
        }

    if (showInfoDialog) {
        AddQtHorariaDialog(
            producao = producao,
            horario = horarioSelecionado,
            onSuccess = {
                showInfoDialog = false
                onRefresh()
            },
            onDismiss = {
                showInfoDialog = false
            }
        )
    }
}