package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.helper.toTimestamp
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import java.time.Instant

/** Converte RemoteModel para Entity */
fun BarrilRemoteModel.toEntity() = BarrilEntity(
    id = this.id,
    nome = this.nome,
    volume = this.volume,
    editadoEm = this.editadoEm?.toInstant() ?: Instant.now(),
    statusSincronizacao = StatusSincronizacao.SINCRONIZADO,
    criadoEm = this.criadoEm?.toInstant() ?: Instant.now(),
    descartavel = this.descartavel
)

/** Converte Entity para RemoteModel */
fun BarrilEntity.toRemoteModel() = BarrilRemoteModel(
    id = this.id,
    nome = this.nome,
    volume = this.volume,
    criadoEm = this.criadoEm.toTimestamp(),
    editadoEm = this.editadoEm?.toTimestamp(),
    descartavel = this.descartavel
)

/** Converte LocalModel para Entity */
fun BarrilLocalModel.toEntity() = BarrilEntity(
    id = this.id,
    nome = this.nome,
    volume = this.volume,
    statusSincronizacao = this.statusSincronizacao,
    criadoEm = Instant.ofEpochMilli(criadoEm),
    descartavel = this.descartavel,
    editadoEm = editadoEm?.let { Instant.ofEpochMilli(it) },
)

/** Converte Entity para LocalModel */
fun BarrilEntity.toLocalModel() = BarrilLocalModel(
    id = this.id,
    nome = this.nome,
    volume = this.volume,
    criadoEm = this.criadoEm.toEpochMilli(),
    statusSincronizacao = this.statusSincronizacao,
    editadoEm = this.editadoEm?.toEpochMilli(),
    descartavel = this.descartavel
)

/** Converte RemoteModel para LocalModel */
fun BarrilRemoteModel.toLocalModel() = this
    .toEntity()
    .copy(statusSincronizacao = StatusSincronizacao.SINCRONIZADO)
    .toLocalModel()