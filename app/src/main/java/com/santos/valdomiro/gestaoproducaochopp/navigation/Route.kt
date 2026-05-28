package com.santos.valdomiro.gestaoproducaochopp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(
    val route: String,
    val title: String = "",
    val icon: ImageVector? = null
) {
    data object HomeRoute: Route(route = "home/{producaoId}", title = "Home") {
        fun criarRota(producaoId: String) = "home/$producaoId"
    }

    // Barril
    data object AdicionarBarrilRoute: Route(route = "adicionar-barril", title = "Adicionar Barril")
    data object ListaBarrisRoute: Route(route = "lista-barris", title = "Lista de Barris")
    data object EditarBarrilRoute: Route(route = "editar-barril/{barrilId}", title = "Editar Barril") {
        fun criarRota(barrilId: String) = "editar-barril/$barrilId"
    }

    // Produto
    data object AdicionarProdutoRoute: Route(route = "adicionar-produto", title = "Adicionar Produto")
    data object ListaProdutosRoute: Route(route = "lista-produtos", title = "Lista de Produtos")
    data object EditarProdutoRoute: Route(route = "editar-produto/{produtoId}", title = "Editar Produto") {
        fun criarRota(produtoId: String) = "editar-produto/$produtoId"
    }

    // Grade
    data object AdicionarGradeRoute: Route(route = "adicionar-grade", title = "Adicionar Grade")
    data object ListaGradesRoute: Route(route = "lista-grades", title = "Lista de Grades")
    data object EditarGradeRoute: Route(route = "editar-grade/{gradeId}", title = "Editar Grade") {
        fun criarRota(gradeId: String) = "editar-grade/$gradeId"
    }

    // Produção
    data object AdicionarProducaoRoute: Route(route = "adicionar-producao/{gradeId}", title = "Adicionar Produção") {
        fun criarRota(gradeId: String) = "adicionar-producao/$gradeId"
    }
    data object ListaProducoesRoute: Route(route = "lista-producoes/{gradeId}", title = "Lista de Produções") {
        fun criarRota(gradeId: String) = "lista-producoes/$gradeId"
    }
    data object EditarProducaoRoute: Route(route = "editar-producao/{producaoId}", title = "Editar Produção") {
        fun criarRota(producaoId: String) = "editar-producao/$producaoId"
    }

    // Movimentação
    data object ListaMovimentacaoRoute: Route(route = "lista-movimentacao/{producaoId}", title = "Histórico de Movimentações") {
        fun criarRota(producaoId: String) = "lista-movimentacao/$producaoId"
    }

    // Calcular tempo de parada
    data object CalcularTempoDeParadaRoute: Route(route = "calcular-tempo-parada", title = "Tempo de Parada")

    // Verificar validade
    data object VerificarValidadeRoute: Route(route = "verificar-validade", title = "Verificar validade")

    // Simular fim de Produção
    data object SimularFimProducaoRoute :
        Route(route = "simular-fim-producao/{producaoId}", title = "Fim de produção") {
        fun criarRota(producaoId: String) =
            "simular-fim-producao/$producaoId"
    }

    // Status da Produção
    data object StatusDaProducaoRoute :
        Route(route = "status-producao/{producaoId}", title = "Status da Produção") {
        fun criarRota(producaoId: String) =
            "status-producao/$producaoId"
    }

}
