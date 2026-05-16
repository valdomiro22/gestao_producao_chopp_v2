package com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import java.time.Instant

@Composable
fun ItemListaProduto(
    produto: ProdutoEntity,
    onEditarClick: () -> Unit,
    onDeletarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Coluna de Informações do Produto
            Column(modifier = Modifier.weight(1f)) {
                // Título principal (Nome do Chopp/Produto)
                Text(
                    text = produto.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtítulo descritivo (Validade)
                Text(
                    text = "Validade: ${produto.prazoValidade} dias",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Botões de Ação na Lateral Direita
            Row {
                IconButton(onClick = onEditarClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Produto",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeletarClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Deletar Produto",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val produto = ProdutoEntity(
        id = "asdo2432",
        nome = "Petra",
        prazoValidade = 30,
        criadoEm = Instant.now(),
        editadoEm = Instant.now(),
        statusSincronizacao = StatusSincronizacao.AGUARDANDO_EXCLUSAO
    )
    MaterialTheme {
        ItemListaProduto(
            produto = produto,
            onEditarClick = {},
            onDeletarClick = {},
            modifier = Modifier.padding(8.dp)
        )
    }
}