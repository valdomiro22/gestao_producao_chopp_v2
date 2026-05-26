package com.santos.valdomiro.gestaoproducaochopp.features.produto.data.produtodao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model.ProdutoLocalModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(produto: ProdutoLocalModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProdutos(produtos: List<ProdutoLocalModel>)

    @Update
    suspend fun update(produto: ProdutoLocalModel)

    @Delete
    suspend fun delete(produto: ProdutoLocalModel)

    @Query("SELECT * FROM produto ORDER BY criadoEm DESC")
    fun getAll(): Flow<List<ProdutoLocalModel>>

    @Query("SELECT * FROM produto WHERE id = :id LIMIT 1")
    fun getOneById(id: String): Flow<ProdutoLocalModel?>

    @Query(
        """
            UPDATE produto
            SET statusSincronizacao = :statusSincronizacao
            WHERE id = :id
        """
    )
    suspend fun updateStatusSincronizacao(
        id: String,
        statusSincronizacao: String
    ): Int
}