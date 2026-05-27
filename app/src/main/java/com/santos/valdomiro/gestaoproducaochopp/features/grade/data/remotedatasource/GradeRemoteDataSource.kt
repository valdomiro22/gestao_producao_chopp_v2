package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.remotedatasource

import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeRemoteModel

interface GradeRemoteDataSource {
    suspend fun insertGrade(grade: GradeRemoteModel)
    suspend fun updateGrade(grade: GradeRemoteModel)
    suspend fun getGrade(gradeId: String): GradeRemoteModel?
    suspend fun deleteGrade(gradeId: String)
    suspend fun getAllGrades(): List<GradeRemoteModel>
}