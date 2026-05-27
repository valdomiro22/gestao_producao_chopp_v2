package com.santos.valdomiro.gestaoproducaochopp.features.produto.data.localdatasource

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model.ProdutoLocalModel
import kotlinx.coroutines.flow.Flow

interface ProdutoLocalDataSource {
    suspend fun insertProduto(produto: ProdutoLocalModel)
    suspend fun updateProduto(produto: ProdutoLocalModel)
    suspend fun updateStatusSincronizacao(produtoId: String, statusSincronizacao: StatusSincronizacao)
    suspend fun deleteProduto(produto: ProdutoLocalModel)
    fun getOneById(produtoId: String): Flow<ProdutoLocalModel?>
    fun getAllProdutos(): Flow<List<ProdutoLocalModel>>
    suspend fun insertAllProdutos(produtos: List<ProdutoLocalModel>)
    suspend fun deleteVariosProdutos(ids: List<String>)
}