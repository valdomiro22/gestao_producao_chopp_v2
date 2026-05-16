package com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository.ProdutoRepository
import javax.inject.Inject

class DeleteProdutoUseCase @Inject constructor(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(produto: ProdutoEntity): Result<Unit> {
        if (produto.id.isBlank()) {
            return Result.failure(
                IllegalArgumentException("ID do produto inválido")
            )
        }

        return repository.deleteProduto(produto = produto)
    }

}