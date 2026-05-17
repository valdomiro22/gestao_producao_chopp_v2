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

    val volumeNecessario: Int
        get() = producao.quantidadeProgramada * barril.volume

    val volumeConsumido: Int
        get() = producao.quantidadeProduzida * barril.volume

    val volumePendente: Int
        get() = quantidadePendente * barril.volume
}