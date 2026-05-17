package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.localdatasource

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.RegistroDuplicadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.RegistroInvalidoException
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.gradedao.GradeDao
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeLocalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GradeLocalDataSourceImpl @Inject constructor(
    private val gradeDao: GradeDao
) : GradeLocalDataSource {

    override suspend fun insertGrade(grade: GradeLocalModel) {
        mapearExceptions { gradeDao.insert(grade = grade) }
    }

    override suspend fun updateGrade(grade: GradeLocalModel) {
        mapearExceptions { gradeDao.update(grade = grade) }
    }

    override suspend fun updateStatusSincronizacao(
        gradeId: String,
        statusSincronizacao: StatusSincronizacao
    ) {
        mapearExceptions {
            val linhasAfetadas = gradeDao.updateStatusSincronizacao(
                id = gradeId,
                statusSincronizacao = statusSincronizacao.name
            )

            if (linhasAfetadas == 0) {
                throw RegistroInvalidoException(IllegalStateException("Grade não encontrado para atualizar status. Verifique se o ID da grade existe."))
            }
        }
    }

    override suspend fun deleteGrade(grade: GradeLocalModel) {
        mapearExceptions { gradeDao.delete(grade = grade) }
    }

    override fun getOneById(gradeId: String): Flow<GradeLocalModel?> {
        return gradeDao.getOneById(id = gradeId)
            .catch { e ->
                throw mapearException(e)
            }
    }

    override fun getAllGrades(): Flow<List<GradeLocalModel>> {
        return gradeDao.getAll()
            .catch { e ->
                throw mapearException(e)
            }
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