package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.mapper

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.helper.toTimestamp
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model.MovimentacaoLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model.MovimentacaoRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import java.time.Instant

/** Converte RemoteModel para Entity */
fun MovimentacaoRemoteModel.toEntity() = MovimentacaoEntity(
    id = this.id,
    producaoId = this.producaoId,
    turnoId = this.turnoId,
    horarioReferente = this.horarioReferente,
    quantidade = this.quantidade,
    tipo = this.tipo,
    criadoEm = this.criadoEm?.toInstant() ?: Instant.now(),
    statusSincronizacao = StatusSincronizacao.SINCRONIZADO
)

/** Converte Entity para RemoteModel */
fun MovimentacaoEntity.toRemoteModel() = MovimentacaoRemoteModel(
    id = this.id,
    producaoId = this.producaoId,
    turnoId = this.turnoId,
    horarioReferente = this.horarioReferente,
    quantidade = this.quantidade,
    tipo = this.tipo,
    criadoEm = this.criadoEm.toTimestamp(),
)

/** Converte LocalModel para Entity */
fun MovimentacaoLocalModel.toEntity() = MovimentacaoEntity(
    id = this.id,
    producaoId = this.producaoId,
    turnoId = this.turnoId,
    horarioReferente = this.horarioReferente,
    quantidade = this.quantidade,
    tipo = this.tipo,
    criadoEm = Instant.ofEpochMilli(this.criadoEm),
    statusSincronizacao = this.statusSincronizacao
)

/** Converte Entity para LocalModel */
fun MovimentacaoEntity.toLocalModel() = MovimentacaoLocalModel(
    id = this.id,
    producaoId = this.producaoId,
    turnoId = this.turnoId,
    horarioReferente = this.horarioReferente,
    quantidade = this.quantidade,
    tipo = this.tipo,
    criadoEm = this.criadoEm.toEpochMilli(),
    statusSincronizacao = this.statusSincronizacao
)

fun MovimentacaoRemoteModel.toLocalModel() = this
    .toEntity()
    .copy(statusSincronizacao = StatusSincronizacao.SINCRONIZADO)
    .toLocalModel()