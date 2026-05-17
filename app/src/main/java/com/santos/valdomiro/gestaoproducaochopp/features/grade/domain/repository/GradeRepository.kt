package com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import kotlinx.coroutines.flow.Flow

interface GradeRepository {
    suspend fun insertGrade(grade: GradeEntity): Result<Unit>
    suspend fun updateGrade(grade: GradeEntity): Result<Unit>
    suspend fun updateStatusSincronizacao(
        gradeId: String,
        statusSincronizacao: StatusSincronizacao
    ): Result<Unit>

    suspend fun deleteGrade(grade: GradeEntity): Result<Unit>
    fun getOneById(gradeId: String): Flow<GradeEntity?>
    fun getAllGrades(): Flow<List<GradeEntity>>
}