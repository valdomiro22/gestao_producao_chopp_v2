package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.remotedatasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.AcessoNegadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.NaoEncontradoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ServicoIndisponivelException
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeRemoteModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GradeRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GradeRemoteDataSource {

    private val gradeCollection = "grade"

    override suspend fun insertGrade(grade: GradeRemoteModel) {
        mapearExecution {
            if (grade.id.isEmpty()) {
                throw IllegalArgumentException("Erro: Tentativa de salvar Grade sem ID")
            }

            firestore.collection(gradeCollection)
                .document(grade.id)
                .set(grade)
                .await()
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
            firestore.collection(gradeCollection)
                .document(id)
                .delete()
                .await()
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