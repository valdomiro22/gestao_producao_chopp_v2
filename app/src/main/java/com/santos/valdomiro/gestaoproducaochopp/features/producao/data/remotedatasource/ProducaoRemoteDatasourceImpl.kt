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

    val producaoCollection = "producao"

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
        id: String,
        producao: ProducaoRemoteModel
    ) {
        mapearExecution {
            firestore.collection(producaoCollection)
                .document(id)
                .set(producao, SetOptions.merge())
                .await()
        }
    }

    override suspend fun getProducao(id: String): ProducaoRemoteModel? {
        return mapearExecution {
            val snapshot = firestore.collection(producaoCollection)
                .document(id)
                .get()
                .await()

            snapshot.toObject(ProducaoRemoteModel::class.java)
        }
    }

    override suspend fun deleteProducao(id: String) {
        mapearExecution {
            firestore.collection(producaoCollection)
                .document(id)
                .delete()
                .await()
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