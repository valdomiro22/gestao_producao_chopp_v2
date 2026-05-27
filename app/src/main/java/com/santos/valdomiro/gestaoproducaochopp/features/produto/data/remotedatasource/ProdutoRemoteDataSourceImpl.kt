package com.santos.valdomiro.gestaoproducaochopp.features.produto.data.remotedatasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.AcessoNegadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.NaoEncontradoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ServicoIndisponivelException
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model.ProdutoRemoteModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProdutoRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProdutoRemoteDataSource {

    private val produtoCollection = "produto"

    override suspend fun insertProduto(produto: ProdutoRemoteModel) {
        mapearExecution {
            if (produto.id.isEmpty()) {
                throw IllegalArgumentException("Erro: Tentativa de salvar Produto sem ID")
            }

            firestore.collection(produtoCollection)
                .document(produto.id)
                .set(produto)
                .await()
        }
    }

    override suspend fun updateProduto(
        produto: ProdutoRemoteModel
    ) {
        mapearExecution {
            firestore.collection(produtoCollection)
                .document(produto.id)
                .update(produto.toMap())
                .await()
        }
    }

    override suspend fun getProduto(produtoId: String): ProdutoRemoteModel? {
        return mapearExecution {
            val snapshot = firestore.collection(produtoCollection)
                .document(produtoId)
                .get()
                .await()

            snapshot.toObject(ProdutoRemoteModel::class.java)
        }
    }

    override suspend fun deleteProduto(produtoId: String) {
        mapearExecution {
            firestore.collection(produtoCollection)
                .document(produtoId)
                .delete()
                .await()
        }
    }

    override suspend fun getAllProdutos(): List<ProdutoRemoteModel> {
        return mapearExecution {
            val snapshot = firestore.collection(produtoCollection)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(ProdutoRemoteModel::class.java) }
        }
    }

    /**
     * Função auxiliar para centralizar o tratamento de erros do Firebase.
     * Ela "traduz" exceções técnicas para exceções de domínio.
     */
    private suspend fun <T> mapearExecution(action: suspend () -> T): T {
        return try {
            action()
        } catch (e: FirebaseFirestoreException) {
            throw when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> AcessoNegadoException(e)
                FirebaseFirestoreException.Code.NOT_FOUND -> NaoEncontradoException(e)
                FirebaseFirestoreException.Code.UNAVAILABLE -> ServicoIndisponivelException(e)
                else -> ErroBancoDadosDesconhecidoException(e)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}