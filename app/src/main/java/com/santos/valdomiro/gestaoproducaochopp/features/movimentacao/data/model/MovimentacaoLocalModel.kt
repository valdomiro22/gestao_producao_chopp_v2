package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.TipoMovimentacao

@Entity(tableName = "movimentacao")
data class MovimentacaoLocalModel(
    @PrimaryKey
    val id: String,

    val producaoId: String,
    val turnoId: Int,
    val horarioReferente: Int,
    val quantidade: Int,
    val tipo: TipoMovimentacao,
    val criadoEm: Long,
    val statusSincronizacao: StatusSincronizacao
)