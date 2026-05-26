package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.remotedatasource

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.AcessoNegadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.NaoEncontradoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ServicoIndisponivelException
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.util.TAG
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GradeRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GradeRemoteDataSource {

    private val gradeCollection = "grade"
    private val producaoCollection = "producao"

    override suspend fun insertGrade(grade: GradeRemoteModel) {
        try {
            if (grade.id.isEmpty()) {
                throw IllegalArgumentException("Erro: Tentativa de salvar Grade sem ID")
            }

            Log.d(TAG, "insertGrade: tentando salvar grade ${grade.id}")

            firestore.collection(gradeCollection)
                .document(grade.id)
                .set(grade.toMap()) // prefira toMap()
                .await()

            Log.d(TAG, "insertGrade: grade salva com sucesso ${grade.id}")

        } catch (e: Exception) {
            Log.e(TAG, "insertGrade: erro ao salvar grade no Firestore", e)
            throw e
        }
    }

    override suspend fun updateGrade(id: String, grade: GradeRemoteModel) {
        mapearExecution {
            firestore.collection(gradeCollection)
                .document(id)
                .set(grade, SetOptions.merge())
                .await()
        }
    }

    override suspend fun getGrade(id: String): GradeRemoteModel? {
        return mapearExecution {
            val snapshot = firestore.collection(gradeCollection)
                .document(id)
                .get()
                .await()

            snapshot.toObject(GradeRemoteModel::class.java)
        }
    }

    override suspend fun deleteGrade(id: String) {
        mapearExecution {
            val batch = firestore.batch()

            val producoesSnapshot = firestore
                .collection(producaoCollection)
                .whereEqualTo("gradeId", id)
                .get()
                .await()

            producoesSnapshot.documents.forEach { document ->
                batch.delete(document.reference)
            }

            val gradeRef = firestore
                .collection(gradeCollection)
                .document(id)

            batch.delete(gradeRef)

            batch.commit().await()
        }
    }

    override suspend fun getAllGrades(): List<GradeRemoteModel> {
        return mapearExecution {
            val snapshot = firestore.collection(gradeCollection)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(GradeRemoteModel::class.java) }
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