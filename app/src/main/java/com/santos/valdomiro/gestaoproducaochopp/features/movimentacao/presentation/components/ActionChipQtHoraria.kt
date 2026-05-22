package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.components

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ActionChipCustom(
    valor: Int,
    isIncrement: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cor = if (isIncrement) Color(0xFF1E88E5) else Color(0xFFB71C1C)
    val sinal = if (isIncrement) "+" else "-"

    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = "$sinal$valor",
                style = MaterialTheme.typography.labelMedium,
                color = cor
            )
        },
        border = AssistChipDefaults.assistChipBorder(
            enabled = true,
            borderColor = cor
        ),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
    )
}