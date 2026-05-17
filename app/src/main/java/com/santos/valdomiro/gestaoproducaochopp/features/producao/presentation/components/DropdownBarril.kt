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
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBarril(
    listaBarris: List<BarrilEntity>,
    barrilIdAtual: String?,
    onBarrilSelecionado: (BarrilEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    var textoBusca by remember(barrilIdAtual, listaBarris) {
        mutableStateOf(
            listaBarris.firstOrNull { it.id == barrilIdAtual }?.nome ?: ""
        )
    }

    val nomeBarrilAtual = remember(barrilIdAtual, listaBarris) {
        listaBarris.firstOrNull { it.id == barrilIdAtual }?.nome ?: ""
    }

    // 2. Atualizamos a lógica do filtro
    val listaFiltrada by remember(textoBusca, listaBarris, nomeBarrilAtual) {
        derivedStateOf {
            // Se o campo estiver vazio OU se o texto for exatamente o produto já selecionado,
            // nós mostramos a lista inteira novamente.
            if (textoBusca.isBlank() || textoBusca == nomeBarrilAtual) {
                listaBarris
            } else {
                // Caso contrário, o usuário está realmente digitando para buscar algo novo
                listaBarris.filter {
                    it.nome.contains(textoBusca, ignoreCase = true)
                }
            }
        }
    }

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
            label = { Text("Barril") },
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
                    text = { Text("Barril de ${produto.nome}") },
                    onClick = {
                        onBarrilSelecionado(produto)
                        textoBusca = produto.nome
                        expanded = false
                    }
                )
            }
        }
    }
}