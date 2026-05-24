package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoDetalhada
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.ChoppDarkBrown
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.ChoppOrange
import com.santos.valdomiro.gestaoproducaochopp.ui.theme.ChoppTeal
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

    val volumeNecessario = pdDetalhada.volumeNecessario
    val volumePendente = pdDetalhada.volumePendente
    val volumeConsumido = pdDetalhada.volumeConsumido

    val volumeNecessarioFormatado = formatador.format(volumeNecessario)
    val volumePendenteFormatado = formatador.format(volumePendente)
    val volumeConsumidoFormatado = formatador.format(volumeConsumido)

    val bufferOk = volumeNecessario >= nivelMaxBuffer

    val progresso = if (volumeNecessario <= 0.0) {
        0f
    } else {
        (volumeConsumido / volumeNecessario)
            .coerceIn(0.0, 1.0)
            .toFloat()
    }

    val corStatus = if (bufferOk) ChoppTeal else ChoppOrange
    val darkTheme = isSystemInDarkTheme()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onClick() }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = corStatus.copy(alpha = if (darkTheme) 0.22f else 0.14f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = corStatus
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = "Controle do buffer",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
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

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LinhaInfoBuffer(
                    titulo = "Barril",
                    valor = barril.nome
                )

                LinhaInfoBuffer(
                    titulo = "Volume da necessário",
                    valor = "$volumeNecessarioFormatado hl"
                )

                LinhaInfoBuffer(
                    titulo = "Volume pendente",
                    valor = "$volumePendenteFormatado hl"
                )

            }

            LinearProgressIndicator(
                progress = { progresso },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp)),
                color = corStatus,
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
    val darkTheme = isSystemInDarkTheme()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = ChoppOrange.copy(alpha = if (darkTheme) 0.24f else 0.14f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "VERIFIQUE O BUFFER",
            color = if (darkTheme) Color(0xFFFFB787) else ChoppOrange,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun BufferOk(
    modifier: Modifier = Modifier
) {
    val darkTheme = isSystemInDarkTheme()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = ChoppTeal.copy(alpha = if (darkTheme) 0.24f else 0.14f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "BUFFER OK",
            color = if (darkTheme) Color(0xFF4FD8DC) else ChoppTeal,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.ExtraBold
        )
    }
}