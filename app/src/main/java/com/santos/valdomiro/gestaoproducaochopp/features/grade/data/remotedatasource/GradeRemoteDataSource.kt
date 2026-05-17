package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.remotedatasource

import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeRemoteModel

interface GradeRemoteDataSource {
    suspend fun insertGrade(grade: GradeRemoteModel)
    suspend fun updateGrade(id: String, grade: GradeRemoteModel)
    suspend fun getGrade(id: String): GradeRemoteModel?
    suspend fun deleteGrade(id: String)
    suspend fun getAllGrades(): List<GradeRemoteModel>
}