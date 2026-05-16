package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao

@Entity(tableName = "grade")
data class GradeLocalModel(
    @PrimaryKey
    val id: String,

    val numero: Int,
    val data: Long,
    val quantidadeBarris: Int,
    val volumeHlNecessario: Double,
    val criadoEm: Long,
    val editadoEm: Long? = null,
    val statusSincronizacao: StatusSincronizacao,
)
