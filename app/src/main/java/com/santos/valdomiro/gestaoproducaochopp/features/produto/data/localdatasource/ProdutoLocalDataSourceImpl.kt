package com.santos.valdomiro.gestaoproducaochopp.features.produto.data.localdatasource

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import android.util.Log
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.RegistroDuplicadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.RegistroInvalidoException
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model.ProdutoLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.produtodao.ProdutoDao
import com.santos.valdomiro.gestaoproducaochopp.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class ProdutoLocalDataSourceImpl @Inject constructor(
    private val produtoDao: ProdutoDao
) : ProdutoLocalDataSource {

    override suspend fun insertProduto(produto: ProdutoLocalModel) {
        mapearExceptions { produtoDao.insert(produto = produto) }
    }

    override suspend fun updateProduto(produto: ProdutoLocalModel) {
        mapearExceptions { produtoDao.update(produto = produto) }
    }

    override suspend fun updateStatusSincronizacao(
        produtoId: String,
        statusSincronizacao: StatusSincronizacao
    ) {
        mapearExceptions {
            val linhasAfetadas = produtoDao.updateStatusSincronizacao(
                id = produtoId,
                statusSincronizacao = statusSincronizacao.name
            )

            if (linhasAfetadas == 0) {
                throw RegistroInvalidoException(
                    IllegalArgumentException("Produto não encontrado para atualizar status.")
                )
            }
        }
    }

    override suspend fun deleteProduto(produto: ProdutoLocalModel) {
        mapearExceptions { produtoDao.delete(produto = produto) }
    }

    override fun getOneById(produtoId: String): Flow<ProdutoLocalModel?> {
        return produtoDao.getOneById(id = produtoId)
            .catch { e ->
                throw mapearException(e)
            }
    }

    override fun getAllProdutos(): Flow<List<ProdutoLocalModel>> {
        return produtoDao.getAll()
            .catch { e ->
                throw mapearException(e)
            }
    }

    override suspend fun insertAllProdutos(produtos: List<ProdutoLocalModel>) {
        produtoDao.insertAllProdutos(produtos)
    }

    override suspend fun deleteVariosProdutos(ids: List<String>) {
        produtoDao.deleteVariosProdutos(ids = ids)
    }

    private suspend fun <T> mapearExceptions(action: suspend () -> T): T {
        return try {
            action()
        } catch (e: Exception) {
            throw mapearException(e)
        }
    }

    private fun mapearException(e: Throwable): Exception {
        return when (e) {
            is SQLiteConstraintException -> RegistroDuplicadoException(e)
            is IllegalStateException -> RegistroInvalidoException(e)
            is SQLiteException -> ErroBancoDadosDesconhecidoException(e)
            is Exception -> e
            else -> ErroBancoDadosDesconhecidoException(e)
        }
    }

}