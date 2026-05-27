package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.barrildao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilLocalModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BarrilDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(barril: BarrilLocalModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBarris(barris: List<BarrilLocalModel>)

    @Update
    suspend fun update(barril: BarrilLocalModel)

    @Delete
    suspend fun delete(barril: BarrilLocalModel)

    @Query("DELETE FROM barril WHERE id IN (:ids)")
    suspend fun deleteVariosBarris(ids: List<String>)

    @Query("SELECT * FROM barril ORDER BY criadoEm DESC")
    fun getAll(): Flow<List<BarrilLocalModel>>

    @Query("SELECT * FROM barril WHERE id = :id LIMIT 1")
    fun getOneById(id: String): Flow<BarrilLocalModel?>

    @Query(
        """
            UPDATE barril
            SET statusSincronizacao = :statusSincronizacao
            WHERE id = :id
        """
    )
    suspend fun updateStatusSincronizacao(
        id: String,
        statusSincronizacao: String
    ): Int
}