package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoDetalhada
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository.ProdutoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetOneProducaoDetalhadaUseCase @Inject constructor(
    private val producaoRepository: ProducaoRepository,
    private val barrilRepository: BarrilRepository,
    private val produtoRepository: ProdutoRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        producaoId: String
    ): Flow<Result<ProducaoDetalhada>> {

        return producaoRepository.getOneById(producaoId)
            .flatMapLatest { producao ->

                if (producao == null) {
                    flowOf(Result.failure(Exception("Produção não encontrada")))
                } else {
                    combine(
                        barrilRepository.getOneById(producao.barrilId),
                        produtoRepository.getOneById(producao.produtoId)
                    ) { barril, produto ->

                        when {
                            barril == null -> {
                                Result.failure(Exception("Barril não encontrado"))
                            }

                            produto == null -> {
                                Result.failure(Exception("Produto não encontrado"))
                            }

                            else -> {
                                Result.success(
                                    ProducaoDetalhada(
                                        producao = producao,
                                        barril = barril,
                                        produto = produto
                                    )
                                )
                            }
                        }
                    }
                }
            }.catch { exception ->
                emit(Result.failure(exception))
            }
    }
}