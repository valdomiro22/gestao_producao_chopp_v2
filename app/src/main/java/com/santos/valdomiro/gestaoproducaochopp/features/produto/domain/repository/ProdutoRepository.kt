package com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import kotlinx.coroutines.flow.Flow

interface ProdutoRepository {
    suspend fun insertProduto(produto: ProdutoEntity): Result<Unit>
    suspend fun updateProduto(produto: ProdutoEntity): Result<Unit>
    suspend fun updateStatusSincronizacao(
        produtoId: String,
        statusSincronizacao: StatusSincronizacao
    ): Result<Unit>

    suspend fun deleteProduto(produto: ProdutoEntity): Result<Unit>
    fun getOneById(produtoId: String): Flow<ProdutoEntity?>
    fun getAllProdutos(): Flow<List<ProdutoEntity>>
}