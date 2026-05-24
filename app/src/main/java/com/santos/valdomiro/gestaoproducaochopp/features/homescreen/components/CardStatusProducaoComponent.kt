package com.santos.valdomiro.gestaoproducaochopp.features.homescreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.OnStatusPendente
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.OnStatusProgramado
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.OnStatusProduzido
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.StatusPendente
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.StatusProgramado
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.StatusProduzido

@Composable
fun CardStatusProducaoComponent(
    modifier: Modifier = Modifier,
    backGround: Color,
    contentColor: Color = Color.White,
    titulo: String,
    quantidade: String,
    largura: Dp = 110.dp,
    altura: Dp = 120.dp,
    conteudoTextSize: TextUnit = 22.sp
) {
    val shape = RoundedCornerShape(12.dp)
    val cardBackground = MaterialTheme.colorScheme.surface
    val quantidadeBackground = backGround.copy(
        alpha = if (isSystemInDarkTheme()) 0.22f else 0.12f
    )

    Column(
        modifier = modifier
            .width(largura)
            .height(altura)
            .clip(shape)
            .border(
                width = 1.5.dp,
                color = backGround.copy(alpha = 0.85f),
                shape = shape
            )
            .background(cardBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .background(backGround),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = titulo,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .background(quantidadeBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quantidade,
                color = backGround,
                fontSize = conteudoTextSize,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardStatusProducaoPreview() {
    MaterialTheme {
        CardStatusProducaoComponent(
            backGround = StatusPendente,
            contentColor = OnStatusPendente,
            titulo = "Pendente",
            quantidade = "120"
        )
    }
}