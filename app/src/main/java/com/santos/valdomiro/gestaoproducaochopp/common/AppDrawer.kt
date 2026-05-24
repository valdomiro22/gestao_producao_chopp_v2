package com.santos.valdomiro.gestaoproducaochopp.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.santos.valdomiro.gestaoproducaochopp.navigation.Route

@Composable
fun AppDrawer(
    selectedRoute: String,
    onItemClick: (Route) -> Unit,
    onLogoutClick: () -> Unit = {},
) {
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.75f),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        DrawerHeader()

        Spacer(modifier = Modifier.height(12.dp))

        // Itens Principais
        val drawerItems = listOf(
            Route.HomeRoute,
            Route.CalcularTempoDeParadaRoute,
            Route.ListaGradesRoute,
            Route.ListaProducoesRoute,
            Route.ListaBarrisRoute,
            Route.ListaProdutosRoute,
        )

        Column(
            modifier = Modifier
                .weight(1f) // Faz a lista ocupar o espaço central
                .padding(horizontal = 12.dp)
        ) {
            drawerItems.forEach { item ->
                NavigationDrawerItem(
                    label = { Text(item.title, fontWeight = FontWeight.Medium) },
                    selected = item.route == selectedRoute,
                    onClick = { onItemClick(item) },
                    icon = {
                        item.icon?.let {
                            Icon(it, contentDescription = null)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // Rodapé / Botão de Sair
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        NavigationDrawerItem(
            label = { Text("Sair da Conta") },
            selected = false,
            onClick = onLogoutClick,
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair") },
            modifier = Modifier.padding(12.dp),
            colors = NavigationDrawerItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.error,
                unselectedTextColor = MaterialTheme.colorScheme.error
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Versão do App (Opcional, mas profissional)
        Text(
            text = "v1.0.4",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
            .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
    ) {
        Column {
            // Ícone circular para dar identidade visual
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = Icons.Default.PrecisionManufacturing, // Exemplo de ícone de produção
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Gestão de Produção",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = "Unidade: Chopp Industrial",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        }
    }
}