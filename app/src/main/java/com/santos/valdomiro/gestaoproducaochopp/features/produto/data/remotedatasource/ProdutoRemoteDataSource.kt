package com.santos.valdomiro.gestaoproducaochopp.features.produto.data.remotedatasource

import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model.ProdutoRemoteModel

interface ProdutoRemoteDataSource {
    suspend fun insertProduto(produto: ProdutoRemoteModel)
    suspend fun updateProduto(produto: ProdutoRemoteModel)
    suspend fun getProduto(produtoId: String): ProdutoRemoteModel?
    suspend fun deleteProduto(produtoId: String)
    suspend fun getAllProdutos(): List<ProdutoRemoteModel>
}