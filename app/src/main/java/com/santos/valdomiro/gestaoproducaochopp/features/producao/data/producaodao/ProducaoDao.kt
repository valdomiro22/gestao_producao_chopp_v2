package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.producaodao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model.ProducaoLocalModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ProducaoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(producao: ProducaoLocalModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducoes(producoes: List<ProducaoLocalModel>)

    @Update
    suspend fun update(producao: ProducaoLocalModel)

    @Delete
    suspend fun delete(producao: ProducaoLocalModel)

    @Query("SELECT * FROM producao ORDER BY criadoEm DESC")
    fun getAll(): Flow<List<ProducaoLocalModel>>

    @Query("SELECT * FROM producao WHERE id = :producaoId LIMIT 1")
    fun getOneById(producaoId: String): Flow<ProducaoLocalModel?>

    @Query("SELECT * FROM producao WHERE gradeId = :gradeId ORDER BY criadoEm DESC")
    fun getAllDaGrade(gradeId: String): Flow<List<ProducaoLocalModel>>

    @Query(
        """
            UPDATE producao
            SET statusSincronizacao = :statusSincronizacao
            WHERE id = :id
        """
    )
    suspend fun updateStatusSincronizacao(
        id: String,
        statusSincronizacao: String
    ): Int

}