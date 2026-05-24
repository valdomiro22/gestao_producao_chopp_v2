package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.statusproducao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LegendaGraficoProducao(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemLegenda(
            cor = Color(0xFFB39DDB),
            texto = "Falta"
        )

        Spacer(modifier = Modifier.width(24.dp))

        ItemLegenda(
            cor = Color(0xFF4CAF50),
            texto = "Concluído"
        )
    }
}

@Composable
fun ItemLegenda(
    cor: Color,
    texto: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(cor)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = texto,
            fontSize = 14.sp
        )
    }
}