package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.repository

import android.util.Log
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.localdatasource.GradeLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.mapper.toEntity
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.mapper.toLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.mapper.toRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.remotedatasource.GradeRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository.GradeRepository
import com.santos.valdomiro.gestaoproducaochopp.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.collections.toSet

class GradeRepositoryImpl @Inject constructor(
    private val localDataSource: GradeLocalDataSource,
    private val remoteDataSource: GradeRemoteDataSource
) : GradeRepository {
    override suspend fun insertGrade(grade: GradeEntity): Result<Unit> {
        return try {
            val gradePendente =
                grade.copy(statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO)

            localDataSource.insertGrade(grade = gradePendente.toLocalModel())

            try {
                remoteDataSource.insertGrade(grade = gradePendente.toRemoteModel())

                localDataSource.updateStatusSincronizacao(
                    gradeId = gradePendente.id,
                    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                )
            } catch (e: Exception) {
                Log.d(
                    TAG,
                    "insertGrade: Erro ao enviar grade para o servidor. Grade salva localmente com status AGUARDANDO_ENVIO. Detalhes do erro: ${e.message}"
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateGrade(grade: GradeEntity): Result<Unit> {
        return try {
            val gradePendente =
                grade.copy(statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO)

            localDataSource.updateGrade(grade = gradePendente.toLocalModel())

            try {
                remoteDataSource.updateGrade(
                    grade = gradePendente.toRemoteModel()
                )

                localDataSource.updateStatusSincronizacao(
                    gradeId = gradePendente.id,
                    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
                )
            } catch (e: Exception) {
                Log.d(
                    TAG,
                    "updateGrade: Erro ao enviar atualização da grade para o servidor. Grade atualizada localmente com status AGUARDANDO_ENVIO. Detalhes do erro: ${e.message}"
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStatusSincronizacao(
        gradeId: String,
        statusSincronizacao: StatusSincronizacao
    ): Result<Unit> {
        return try {
            localDataSource.updateStatusSincronizacao(
                gradeId = gradeId,
                statusSincronizacao = statusSincronizacao
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteGrade(grade: GradeEntity): Result<Unit> {
        return try {
            val gradeParaExcluir =
                grade.copy(statusSincronizacao = StatusSincronizacao.AGUARDANDO_EXCLUSAO)

            localDataSource.deleteGrade(grade = gradeParaExcluir.toLocalModel())

            try {
                remoteDataSource.deleteGrade(gradeId = grade.id)
            } catch (e: Exception) {
                Log.d(
                    TAG,
                    "deleteGrade: Erro ao deletar grade no servidor. Grade deletada localmente. Detalhes do erro: ${e.message}"
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOneById(gradeId: String): Flow<GradeEntity?> {
        return localDataSource.getOneById(gradeId = gradeId)
            .map { grade ->
                if (grade == null) {
                    null
                } else if (grade.statusSincronizacao == StatusSincronizacao.AGUARDANDO_EXCLUSAO) {
                    null
                } else {
                    grade.toEntity()
                }
            }
    }

    override fun getAllGrades(): Flow<List<GradeEntity>> {
        return localDataSource.getAllGrades()
            .map { listaGrades ->
                listaGrades.filter { grade ->
                    grade.statusSincronizacao != StatusSincronizacao.AGUARDANDO_EXCLUSAO
                }.map { grade ->
                    grade.toEntity()
                }
            }

    }

    override suspend fun sincronizarGradesDoRemoto(): Result<Unit> {
        return try {
            val gradesRemotos = remoteDataSource.getAllGrades()

            val gradesLocais = localDataSource
                .getAllGrades()
                .first()

            val idsRemotos = gradesRemotos
                .map { it.id }
                .toSet()

            val idsLocais = gradesLocais
                .map { it.id }
                .toSet()

            val gradesParaSalvar = gradesRemotos
                .filter { gradeRemote ->
                    gradeRemote.id !in idsLocais
                }
                .map { it.toLocalModel() }

            if (gradesParaSalvar.isNotEmpty()) {
                localDataSource.insertAllGrades(gradesParaSalvar)
            }

            val idsGradesParaExcluirDoLocal = gradesLocais
                .filter { gradeLocal ->
                    gradeLocal.id !in idsRemotos &&
                            gradeLocal.statusSincronizacao == StatusSincronizacao.SINCRONIZADO
                }
                .map { gradeLocal ->
                    gradeLocal.id
                }

            if (idsGradesParaExcluirDoLocal.isNotEmpty()) {
                localDataSource.deleteVariasGrades(idsGradesParaExcluirDoLocal)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
