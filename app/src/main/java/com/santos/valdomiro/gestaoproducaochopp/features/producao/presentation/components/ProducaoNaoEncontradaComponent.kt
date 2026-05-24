package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProducaoNaoEncontradaComponent(
    modifier: Modifier = Modifier,
    mensagem: String = "Não foi possível encontrar os dados desta produção.",
    onTentarNovamente: (() -> Unit)? = null,
    onVoltar: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ícone com fundo sutil
        Surface(
            modifier = Modifier.size(120.dp),
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Inventory2,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Título principal
        Text(
            text = "Produção não encontrada",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mensagem explicativa
        Text(
            text = mensagem,
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )

        if (onTentarNovamente != null) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onTentarNovamente
            ) {
                Text("Tentar novamente")
            }
        }

        if (onVoltar != null) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onVoltar
            ) {
                Text("Voltar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProducaoNaoEncontradaComponent() {
    ProducaoNaoEncontradaComponent(
        mensagem = "Essa produção pode ter sido excluída ou ainda não foi sincronizada."
    )
}