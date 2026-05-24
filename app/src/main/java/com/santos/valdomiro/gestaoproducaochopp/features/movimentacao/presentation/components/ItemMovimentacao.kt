package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santos.valdomiro.gestaoproducaochopp.common.helper.formatarData
import com.santos.valdomiro.gestaoproducaochopp.common.helper.formatarHorarioComMilissegundos
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.TipoMovimentacao

@Composable
fun ItemMovimentacao(
    movimentacao: MovimentacaoEntity,
    modifier: Modifier = Modifier
) {

    val corlor = when (movimentacao.tipo) {
        TipoMovimentacao.SOMA -> Color(0xFF36A03B) // Verde para aumento
        TipoMovimentacao.SUBTRACAO -> Color(0xFFE59E34) // Amarelo para subtração
        TipoMovimentacao.DEFINICAO_INICIAL -> Color(0xFF2E6AD0) // Azul para definição inicial
        TipoMovimentacao.AJUSTE_CORRECAO -> Color(0xFF8E1DDE) // Roxo para ajuste manual
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = movimentacao.tipo.name,
                    fontWeight = FontWeight.Bold,
                    color = corlor,
                    fontSize = 16.sp,
                )

                Text(
                    text = movimentacao.quantidade.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = movimentacao.criadoEm.formatarData(), fontSize = 13.sp)
                Text("às")
                ItemHoraData(movimentacao.criadoEm.formatarHorarioComMilissegundos())
            }

        }
    }
}

@Composable
fun ItemHoraData(conteudo: String) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFE8EEF3),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = conteudo,
            fontSize = 13.sp,
            color = Color(0xFF2D4A53),
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ItemHoraData("24/05/2026")
}