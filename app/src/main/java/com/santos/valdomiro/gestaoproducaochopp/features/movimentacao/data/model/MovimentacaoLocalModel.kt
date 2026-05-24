package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.TipoMovimentacao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model.ProducaoLocalModel

@Entity(
    tableName = "movimentacao",
    foreignKeys = [
        ForeignKey(
            entity = ProducaoLocalModel::class,
            parentColumns = ["id"],
            childColumns = ["producaoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["producaoId"])
    ]
)
data class MovimentacaoLocalModel(
    @PrimaryKey
    val id: String,

    val producaoId: String,
    val turnoId: Int,
    val horarioReferente: String,
    val quantidade: Int,
    val tipo: TipoMovimentacao,
    val criadoEm: Long,
    val statusSincronizacao: StatusSincronizacao
)