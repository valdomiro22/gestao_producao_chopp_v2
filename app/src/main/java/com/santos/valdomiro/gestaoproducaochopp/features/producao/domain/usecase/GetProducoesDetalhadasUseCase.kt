package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository.ProdutoRepository
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoDetalhada
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProducoesDetalhadasUseCase @Inject constructor(
    private val producaoRepository: ProducaoRepository,
    private val barrilRepository: BarrilRepository,
    private val produtoRepository: ProdutoRepository
) {

    operator fun invoke(): Flow<Result<List<ProducaoDetalhada>>> {
        return combine(
            producaoRepository.getAllProducoes(),
            barrilRepository.getAllBarris(),
            produtoRepository.getAllProdutos()
        ) { producoes, barris, produtos ->

            val producoesDetalhadas = producoes.mapNotNull { producao ->
                val barril = barris.firstOrNull { it.id == producao.barrilId }
                val produto = produtos.firstOrNull { it.id == producao.produtoId }

                if (barril != null && produto != null) {
                    ProducaoDetalhada(
                        producao = producao,
                        barril = barril,
                        produto = produto
                    )
                } else {
                    null
                }
            }

            Result.success(producoesDetalhadas)
        }
    }
}