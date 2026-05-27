package com.santos.valdomiro.gestaoproducaochopp.features.produto.data.repository

import android.util.Log
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.localdatasource.ProdutoLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.mapper.toEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.mapper.toLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.mapper.toRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.remotedatasource.ProdutoRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository.ProdutoRepository
import com.santos.valdomiro.gestaoproducaochopp.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdutoRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProdutoRemoteDataSource,
    private val localDataSource: ProdutoLocalDataSource,
) : ProdutoRepository {

    override suspend fun insertProduto(produto: ProdutoEntity): Result<Unit> {
        return try {

            val produtoPendente = produto.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO
            )

            localDataSource.insertProduto(produtoPendente.toLocalModel())

            try {
                remoteDataSource.insertProduto(
                    produtoPendente.toRemoteModel()
                )

                localDataSource.updateStatusSincronizacao(
                    produtoId = produtoPendente.id,
                    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                )
            } catch (e: Exception) {
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProduto(produto: ProdutoEntity): Result<Unit> {
        return try {
            val produtoPendente = produto.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
            )

            localDataSource.updateProduto(produtoPendente.toLocalModel())

            try {
                remoteDataSource.updateProduto(
                    produto = produtoPendente.toRemoteModel()
                )

                localDataSource.updateStatusSincronizacao(
                    produtoId = produtoPendente.id,
                    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                )
            } catch (e: Exception) {
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStatusSincronizacao(
        produtoId: String,
        statusSincronizacao: StatusSincronizacao
    ): Result<Unit> {
        return try {
            localDataSource.updateStatusSincronizacao(
                produtoId = produtoId,
                statusSincronizacao = statusSincronizacao
            )

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProduto(produto: ProdutoEntity): Result<Unit> {
        return try {
            val produtoParaExcluir = produto.copy(
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_EXCLUSAO
            )

            localDataSource.updateProduto(produtoParaExcluir.toLocalModel())

            try {
                remoteDataSource.deleteProduto(
                    produtoParaExcluir.id  // TODO - Enviar o barril inteiro, como no locaDataSource()
                )

                localDataSource.deleteProduto(
                    produto = produtoParaExcluir.toLocalModel()
                )
            } catch (e: Exception) {
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOneById(produtoId: String): Flow<ProdutoEntity?> {
        return localDataSource.getOneById(produtoId)
            .map { produto ->
                if (produto == null) {
                    null
                } else if (produto.statusSincronizacao == StatusSincronizacao.AGUARDANDO_EXCLUSAO) {
                    null
                } else {
                    produto.toEntity()
                }
            }
    }

    override fun getAllProdutos(): Flow<List<ProdutoEntity>> {
        return localDataSource.getAllProdutos()
            .map { listaProdutosLocal ->
                listaProdutosLocal
                    .filter { produtoLocal ->
                        produtoLocal.statusSincronizacao != StatusSincronizacao.AGUARDANDO_EXCLUSAO
                    }
                    .map { produtoLocal ->
                        produtoLocal.toEntity()
                    }
            }
    }

    override suspend fun sincronizarProdutosDoRemoto(): Result<Unit> {
        return try {
            val produtosRemotos = remoteDataSource.getAllProdutos()

            val produtosLocais = localDataSource
                .getAllProdutos()
                .first()

            val idsRemotos = produtosRemotos
                .map { it.id }
                .toSet()

            val idsLocais = produtosLocais
                .map { it.id }
                .toSet()

            val produtosParaSalvar = produtosRemotos
                .filter { produtoRemote ->
                    produtoRemote.id !in idsLocais
                }
                .map { it.toLocalModel() }

            if (produtosParaSalvar.isNotEmpty()) {
                localDataSource.insertAllProdutos(produtosParaSalvar)
            }

            val idsProdutosParaExcluirDoLocal = produtosLocais
                .filter { produtoLocal ->
                    produtoLocal.id !in idsRemotos &&
                            produtoLocal.statusSincronizacao == StatusSincronizacao.SINCRONIZADO
                }
                .map { it.id }

            if (idsProdutosParaExcluirDoLocal.isNotEmpty()) {
                localDataSource.deleteVariosProdutos(idsProdutosParaExcluirDoLocal)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao sincronizar produtos do remoto", e)
            Result.failure(e)
        }
    }
}