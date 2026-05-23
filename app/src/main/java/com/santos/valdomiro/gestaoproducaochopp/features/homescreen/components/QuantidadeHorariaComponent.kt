package com.santos.valdomiro.gestaoproducaochopp.features.homescreen.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    val context = LocalContext.current

    var showInfoDialog by remember { mutableStateOf(false) }
    var horarioSelecionado by remember { mutableStateOf("") }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 230.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        items(horarios) { horario ->
            val quantidadeObjeto = quantidades[horario]
            val valorExibido = quantidadeObjeto?.quantidade?.toString() ?: "0"

            CardHorario(
                modifier = Modifier
                    .clickable {
                        horarioSelecionado = horario
                        showInfoDialog = true
                    },
                horario = horario,
                quantidade = valorExibido,
            )
        }
    }

    if (showInfoDialog) {

        AddQtHorariaDialog(
            producao = producao,
            horario = horarioSelecionado,
            onSuccess = {
                onRefresh()
            },
            onDismiss = {
                showInfoDialog = false
            }
        )
    }
}