package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.remotedatasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.AcessoNegadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.NaoEncontradoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ServicoIndisponivelException
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model.MovimentacaoRemoteModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MovimentacaoRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MovimentacaoRemoteDataSource {

    val movimentacaoCollection = "movimentacao"

    override suspend fun insertMovimentacao(movimentacao: MovimentacaoRemoteModel) {
        mapearExecution {
            if (movimentacao.id.isEmpty()) {
                throw IllegalArgumentException("Erro: Tentativa de salvar QuantidadeHoraria sem ID")
            }

            firestore.collection(movimentacaoCollection)
                .document(movimentacao.id)
                .set(movimentacao)
                .await()
        }
    }

    override suspend fun updateMovimentacao(movimentacao: MovimentacaoRemoteModel) {
        mapearExecution {
            firestore.collection(movimentacaoCollection)
                .document(movimentacao.id)
                .set(movimentacao, SetOptions.merge())
                .await()
        }
    }

    override suspend fun getMovimentacao(movimentacaoId: String): MovimentacaoRemoteModel? {
        return mapearExecution {
            val snapshot = firestore.collection(movimentacaoCollection)
                .document(movimentacaoId)
                .get()
                .await()

            snapshot.toObject(MovimentacaoRemoteModel::class.java)
        }
    }

    override suspend fun deleteMovimentacao(movimentacaoId: String) {
        mapearExecution {
            firestore.collection(movimentacaoCollection)
                .document(movimentacaoId)
                .delete()
                .await()
        }
    }

    override suspend fun deleteMovimentacoesDaProducao(producaoId: String) {
        mapearExecution {
            val snapshot = firestore
                .collection(movimentacaoCollection)
                .whereEqualTo("producaoId", producaoId)
                .get()
                .await()

            val batch = firestore.batch()

            snapshot.documents.forEach { document ->
                batch.delete(document.reference)
            }

            batch.commit().await()
        }
    }

    override suspend fun getAllMovimentacoes(): List<MovimentacaoRemoteModel> {
        return mapearExecution {
            val snapshot = firestore.collection(movimentacaoCollection)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(MovimentacaoRemoteModel::class.java) }
        }
    }

    override suspend fun getAllMovimentacoesDaProducao(
        producaoId: String
    ): List<MovimentacaoRemoteModel> {
        return mapearExecution {
            val snapshot = firestore
                .collection(movimentacaoCollection)
                .whereEqualTo("producaoId", producaoId)
                .get()
                .await()

            snapshot.documents.mapNotNull {
                it.toObject(MovimentacaoRemoteModel::class.java)
            }
        }
    }

    override suspend fun getAllMovimentacoesOfHorario(
        horarioReferente: Int, producaoId: String
    ): List<MovimentacaoRemoteModel> {
        return mapearExecution {
            val snapshot = firestore
                .collection(movimentacaoCollection)
                .whereEqualTo("horarioReferente", horarioReferente)
                .whereEqualTo("producaoId", producaoId)
                .orderBy("criadoEm", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull {
                it.toObject(MovimentacaoRemoteModel::class.java)
            }
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