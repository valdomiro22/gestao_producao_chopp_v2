package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.movimentacaodao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model.MovimentacaoLocalModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimentacaoDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(movimentacao: MovimentacaoLocalModel)

    @Update
    suspend fun update(movimentacao: MovimentacaoLocalModel)

    @Delete
    suspend fun delete(movimentacao: MovimentacaoLocalModel)

    @Query("SELECT * FROM movimentacao ORDER BY criadoEm DESC")
    fun getAll(): Flow<List<MovimentacaoLocalModel>>

    @Query("SELECT * FROM movimentacao WHERE producaoId = :producaoId ORDER BY criadoEm DESC")
    fun getAllOfProducao(producaoId: String): Flow<List<MovimentacaoLocalModel>>

    @Query("SELECT * FROM movimentacao WHERE horarioReferente = :horarioReferente AND producaoId = :producaoId ORDER BY criadoEm DESC")
    fun getAllMovimentacoesOfHorario(
        horarioReferente: String,
        producaoId: String
    ): Flow<List<MovimentacaoLocalModel>>

    @Query("SELECT * FROM movimentacao WHERE id = :movimentacaoId LIMIT 1")
    fun getOneById(movimentacaoId: String): Flow<MovimentacaoLocalModel?>

    @Query(
        """
            SELECT * FROM movimentacao
            WHERE statusSincronizacao = :status
        """
    )
    suspend fun getMovimentacoesPorStatus(
        status: String
    ): List<MovimentacaoLocalModel>

    @Query(
        """
            UPDATE movimentacao
            SET statusSincronizacao = :statusSincronizacao
            WHERE id = :movimentacaoId
        """
    )
    suspend fun updateStatusSincronizacao(
        movimentacaoId: String,
        statusSincronizacao: String
    ): Int
}