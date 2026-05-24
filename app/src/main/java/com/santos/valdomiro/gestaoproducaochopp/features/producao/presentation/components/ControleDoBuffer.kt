package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoDetalhada
import java.text.DecimalFormat

@Composable
fun ControleDoBuffer(
    onClick: () -> Unit,
    quantidadePendente: Int,
    barril: BarrilEntity,
    pdDetalhada: ProducaoDetalhada,
    modifier: Modifier = Modifier,
) {
    val formatador = DecimalFormat("#.##")
    val nivelMaxBuffer = 30

    val volumeBarril = barril.volume
    val volumeNecessario = pdDetalhada.volumeNecessario
    val volumePendente = pdDetalhada.volumePendente
    val volumeConsumido = pdDetalhada.volumeConsumido
    val volumeNecessarioFormatado = formatador.format(pdDetalhada.volumeNecessario)
    val volumePendenteFormatado = formatador.format(pdDetalhada.volumePendente)
    val volumeConsumidoFormatado = formatador.format(pdDetalhada.volumeConsumido)
    val bufferOk = volumeNecessario >= nivelMaxBuffer

    val progresso = (volumeConsumido / volumeNecessario)
        .coerceIn(0.0, 1.0)
        .toFloat()

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = "Controle do buffer",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Simular fim da produção",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.primary)

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LinhaInfoBuffer(
                    titulo = "Barril",
                    valor = barril.nome
                )

                LinhaInfoBuffer(
                    titulo = "Volume da produção",
                    valor = "$volumeNecessarioFormatado hl"
                )

                LinhaInfoBuffer(
                    titulo = "Volume necessário",
                    valor = "$volumePendenteFormatado hl"
                )
            }

            LinearProgressIndicator(
                progress = { progresso },
                modifier = Modifier.fillMaxWidth(),
                color = if (bufferOk) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            if (bufferOk) {
                BufferOk()
            } else {
                AlertaBuffer()
            }
        }
    }
}

@Composable
private fun LinhaInfoBuffer(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun AlertaBuffer(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "VERIFIQUE O BUFFER",
            color = Color(0xFFB71C1C),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BufferOk(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEBFFEB), RoundedCornerShape(8.dp))
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "BUFFER OK",
            color = Color(0xFF21A74E),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}
