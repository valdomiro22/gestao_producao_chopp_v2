package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.statusproducao

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraficoPizzaProducao(
    produzido: Int,
    pendente: Int,
    modifier: Modifier = Modifier
) {
    val total = produzido + pendente

    if (total <= 0) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Sem dados para exibir")
        }
        return
    }

    val produzidoAngle = (produzido.toFloat() / total.toFloat()) * 360f
    val pendenteAngle = 360f - produzidoAngle

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(220.dp)
        ) {
            val strokeWidth = 55.dp.toPx()

            drawArc(
                color = Color(0xFF4CAF50),
                startAngle = -90f,
                sweepAngle = produzidoAngle,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Butt
                )
            )

            drawArc(
                color = Color(0xFFB39DDB),
                startAngle = -90f + produzidoAngle,
                sweepAngle = pendenteAngle,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Butt
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${((produzido.toFloat() / total.toFloat()) * 100).toInt()}%",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "concluído",
                fontSize = 14.sp
            )
        }
    }
}