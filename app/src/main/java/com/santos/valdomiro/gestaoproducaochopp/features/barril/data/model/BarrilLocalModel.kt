package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barril")
data class BarrilLocalModel(
    @PrimaryKey
    val id: String,

    val nome: String,
    val volume: Int,
    val criadoEm: Long,
    val atualizadoEm: Long,
    val descartavel: Boolean,
    val statusSincronizacao: StatusSincronizacao
)