package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity

data class ProducaoDetalhada(
    val producao: ProducaoEntity,
    val barril: BarrilEntity,
    val produto: ProdutoEntity
) {
    val quantidadePendente: Int
        get() = producao.quantidadeProgramada - producao.quantidadeProduzida

    val volumeNecessario: Double
        get() {
            return (producao.quantidadeProgramada * barril.volume) / 100.0
        }

    val volumeConsumido: Double
        get() {
            return (producao.quantidadeProduzida * barril.volume) / 100.0
        }

    val volumePendente: Double
        get() {
            return (quantidadePendente * barril.volume) / 100.0
        }
}