package com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao

@Entity(tableName = "produto")
data class ProdutoLocalModel(
    @PrimaryKey
    val id: String,

    val nome: String,
    val prazoValidade: Int,
    val criadoEm: Long,
    val editadoEm: Long?,
    val statusSincronizacao: StatusSincronizacao
)