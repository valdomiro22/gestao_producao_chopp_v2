package com.santos.valdomiro.gestaoproducaochopp.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LinhaChaveValorInfo(
    modifier: Modifier = Modifier,
    chave: String,
    valor: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$chave: ",
            color = Color.Black,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = valor,
            color = Color(0xFF770CC1),
            fontWeight = FontWeight.Medium
        )
    }
}