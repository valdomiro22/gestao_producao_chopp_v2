package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.santos.valdomiro.gestaoproducaochopp.common.components.ButtomFillMaxWidth
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownProduto(
    listaProdutos: List<ProdutoEntity>,
    produtoIdAtual: String?,
    onProdutoSelecionado: (ProdutoEntity) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }

    var textoBusca by remember(produtoIdAtual, listaProdutos) {
        mutableStateOf(
            listaProdutos.firstOrNull { it.id == produtoIdAtual }?.nome ?: ""
        )
    }

    val nomeProdutoAtual = remember(produtoIdAtual, listaProdutos) {
        listaProdutos.firstOrNull { it.id == produtoIdAtual }?.nome ?: ""
    }

    // 2. Atualizamos a lógica do filtro
    val listaFiltrada by remember(textoBusca, listaProdutos, nomeProdutoAtual) {
        derivedStateOf {
            // Se o campo estiver vazio OU se o texto for exatamente o produto já selecionado,
            // nós mostramos a lista inteira novamente.
            if (textoBusca.isBlank() || textoBusca == nomeProdutoAtual) {
                listaProdutos
            } else {
                // Caso contrário, o usuário está realmente digitando para buscar algo novo
                listaProdutos.filter {
                    it.nome.contains(textoBusca, ignoreCase = true)
                }
            }
        }
    }

    if (listaFiltrada.isEmpty()) {
        ButtomFillMaxWidth(
            onClick = { navController.navigate(Route.AdicionarProdutoRoute.route) },
            text = "Adicionar Produto"
        )
    } else {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = textoBusca,
                onValueChange = {
                    textoBusca = it
                    expanded = true
                },
                label = { Text("Produto") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listaFiltrada.forEach { produto ->
                    DropdownMenuItem(
                        text = { Text(produto.nome) },
                        onClick = {
                            onProdutoSelecionado(produto)
                            textoBusca = produto.nome
                            expanded = false
                        }
                    )
                }
            }
        }
    }

}