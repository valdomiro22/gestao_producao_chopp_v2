package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.localdatasource

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeLocalModel
import kotlinx.coroutines.flow.Flow

interface GradeLocalDataSource {
    suspend fun insertGrade(grade: GradeLocalModel)
    suspend fun updateGrade(grade: GradeLocalModel)
    suspend fun updateStatusSincronizacao(gradeId: String, statusSincronizacao: StatusSincronizacao)
    suspend fun deleteGrade(grade: GradeLocalModel)
    fun getOneById(gradeId: String): Flow<GradeLocalModel?>
    fun getAllGrades(): Flow<List<GradeLocalModel>>
    suspend fun insertAllGrades(grades: List<GradeLocalModel>)
}