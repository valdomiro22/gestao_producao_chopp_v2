package com.santos.valdomiro.gestaoproducaochopp.features.homescreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardHorario(
    modifier: Modifier = Modifier,
    horario: String, quantidade: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8F9FA))
            .border(1.dp, Color(0xFFE9ECEF), RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = horario,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF495057)
        )
        Text(
            text = quantidade,
            fontSize = 14.sp,
            color = Color(0xFF0BA884),
            fontWeight = FontWeight.Medium
        )
    }
}