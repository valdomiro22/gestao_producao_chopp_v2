package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.AcessoNegadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroRemoteDBException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.NaoEncontradoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ServicoIndisponivelException
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilRemoteModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BarrilRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : BarrilRemoteDataSource {

    private val barrilCollection = "barris"

    override suspend fun insertBarril(barril: BarrilRemoteModel) {
        mapearExecution {
            if (barril.id.isEmpty()) {
                throw IllegalArgumentException("Erro: Tentativa de salvar Barril sem ID")
            }

            firestore.collection(barrilCollection)
                .document(barril.id)
                .set(barril)
                .await()
        }
    }

    override suspend fun updateBarril(id: String, barril: BarrilRemoteModel) {
        mapearExecution {
            firestore.collection(barrilCollection)
                .document(id)
                .update(barril.toMap())
                .await()
        }
    }

    override suspend fun getBarril(id: String): BarrilRemoteModel? {
        return mapearExecution {
            val snapshot = firestore.collection(barrilCollection)
                .document(id)
                .get()
                .await()

            snapshot.toObject(BarrilRemoteModel::class.java)
        }
    }

    override suspend fun deleteBarril(id: String) {
        mapearExecution {
            firestore.collection(barrilCollection)
                .document(id)
                .delete()
                .await()
        }
    }

    override suspend fun getAllBarris(): List<BarrilRemoteModel> {
        return mapearExecution {
            val snapshot = firestore.collection(barrilCollection)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(BarrilRemoteModel::class.java) }
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
                else -> ErroRemoteDBException(e)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}