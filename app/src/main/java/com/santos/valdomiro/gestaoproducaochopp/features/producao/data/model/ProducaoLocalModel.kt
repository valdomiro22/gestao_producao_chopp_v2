package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.StatusProducao

@Entity(tableName = "producao")
data class ProducaoLocalModel(
    @PrimaryKey
    val id: String,

    val gradeId: String,
    val barrilId: String,
    val produtoId: String,
    val quantidadeProgramada: Int,
    val quantidadeProduzida: Int,
    val volumeNecessario: Double,
    val status: StatusProducao,
    val criadoEm: Long,
    val editadoEm: Long?,
    val dataFimDeProducao: Long?,
    val statusSincronizacao: StatusSincronizacao,
)