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
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacaoproducao.presentation.components.AddQtHorariaDialog
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
    // Estados internos para controlar o Dialog e qual horário foi clicado
    var showInfoDialog by remember { mutableStateOf(false) }
    var horarioSelecionado by remember { mutableStateOf("") }



    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
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

 // O Dialog agora fica "escutando" o estado interno do componente
    if (showInfoDialog) {
        AddQtHorariaDialog(
            producao = producao,
            // Aqui você passaria o horarioSelecionado para o seu Dialog
            // Supondo que seu Dialog aceite um parâmetro 'horario'
            horario = horarioSelecionado,
            onConfirm = {
                showInfoDialog = false
                Toast.makeText(
                    context,
                    "Salvo para o horário: $horarioSelecionado",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onSuccess = {
                onRefresh()
            },
            onDismiss = {
                showInfoDialog = false
            },

            )
    }
}