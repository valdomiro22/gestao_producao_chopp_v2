package com.santos.valdomiro.gestaoproducaochopp.common.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ErroComponent(mensagem: String) {
    Text(mensagem, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
}