package com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository.ProdutoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOneProdutoUseCase @Inject constructor(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(produtoId: String): Flow<Result<ProdutoEntity>> {
        if (produtoId.isBlank()) {
            return flowOf(Result.failure(IllegalArgumentException("ID do produto inválido")))
        }

        return repository.getOneById(produtoId = produtoId)
            .map { produto ->
                if (produto != null) {
                    Result.success(produto)
                } else {
                    Result.failure(Exception("Produto não encontrado com o ID: $produtoId"))
                }
            }
    }

}