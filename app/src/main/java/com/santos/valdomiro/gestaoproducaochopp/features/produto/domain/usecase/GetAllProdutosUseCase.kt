package com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository.ProdutoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProdutosUseCase @Inject constructor(
    private val repository: ProdutoRepository
) {

    operator fun invoke(): Flow<List<ProdutoEntity>> {
        return repository.getAllProdutos()
    }

}