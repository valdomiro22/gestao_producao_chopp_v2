package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.remotedatasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.AcessoNegadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.NaoEncontradoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ServicoIndisponivelException
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model.ProducaoRemoteModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProducaoRemoteDatasourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProducaoRemoteDatasource {

    private val producaoCollection = "producao"
    private val movimentacaoCollection = "movimentacao"

    override suspend fun insertProducao(producao: ProducaoRemoteModel) {
        mapearExecution {
            if (producao.id.isEmpty()) {
                throw IllegalArgumentException("Erro: Tentativa de salvar Producao sem ID")
            }

            firestore.collection(producaoCollection)
                .document(producao.id)
                .set(producao)
                .await()
        }
    }

    override suspend fun updateProducao(
        producao: ProducaoRemoteModel
    ) {
        mapearExecution {
            firestore.collection(producaoCollection)
                .document(producao.id)
                .set(producao, SetOptions.merge())
                .await()
        }
    }

    override suspend fun getProducao(producaoId: String): ProducaoRemoteModel? {
        return mapearExecution {
            val snapshot = firestore.collection(producaoCollection)
                .document(producaoId)
                .get()
                .await()

            snapshot.toObject(ProducaoRemoteModel::class.java)
        }
    }

    override suspend fun deleteProducao(producaoId: String) {
        mapearExecution {
            val batch = firestore.batch()

            val movimentacoesSnapshot = firestore
                .collection(movimentacaoCollection)
                .whereEqualTo("producaoId", producaoId)
                .get()
                .await()

            movimentacoesSnapshot.documents.forEach { document ->
                batch.delete(document.reference)
            }

            val producaoRef = firestore
                .collection(producaoCollection)
                .document(producaoId)

            batch.delete(producaoRef)

            batch.commit().await()
        }
    }

    override suspend fun getAllProducoes(): List<ProducaoRemoteModel> {
        return mapearExecution {
            val snapshot = firestore.collection(producaoCollection)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(ProducaoRemoteModel::class.java) }
        }
    }

    override suspend fun getAllProducoesDaGrade(gradeId: String): List<ProducaoRemoteModel> {
        return mapearExecution {
            val snapshot = firestore
                .collection(producaoCollection)
                .whereEqualTo("gradeId", gradeId)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(ProducaoRemoteModel::class.java) }
        }
    }

    override suspend fun deleteProducoesDaGrade(gradeId: String) {
        mapearExecution {
            val snapshot = firestore
                .collection(producaoCollection)
                .whereEqualTo("gradeId", gradeId)
                .get()
                .await()

            val batch = firestore.batch()

            snapshot.documents.forEach { document ->
                batch.delete(document.reference)
            }

            batch.commit().await()
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