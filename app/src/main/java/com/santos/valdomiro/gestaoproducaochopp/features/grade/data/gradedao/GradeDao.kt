package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.gradedao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeLocalModel
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(grade: GradeLocalModel)

    @Update
    suspend fun update(grade: GradeLocalModel)

    @Delete
    suspend fun delete(grade: GradeLocalModel)

    @Query("SELECT * FROM grade ORDER BY criadoEm DESC")
    fun getAll(): Flow<List<GradeLocalModel>>

    @Query("SELECT * FROM grade WHERE id = :id LIMIT 1")
    fun getOneById(id: String): Flow<GradeLocalModel?>

    @Query(
        """
            UPDATE grade
            SET statusSincronizacao = :statusSincronizacao
            WHERE id = :id
        """
    )
    suspend fun updateStatusSincronizacao(
        id: String,
        statusSincronizacao: String
    ): Int
}