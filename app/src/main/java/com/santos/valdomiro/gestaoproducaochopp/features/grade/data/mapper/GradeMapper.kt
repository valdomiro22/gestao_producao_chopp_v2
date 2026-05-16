package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.mapper

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.helper.toTimestamp
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import java.time.Instant

/** Converte RemoteModel para Entity */
fun GradeRemoteModel.toEntity() = GradeEntity(
    id = this.id,
    numero = this.numero,
    data = this.data?.toInstant() ?: Instant.now(),
    quantidadeBarris = this.quantidadeBarris,
    volumeHlNecessario = this.volumeHlNecessario,
    criadoEm = this.criadoEm?.toInstant() ?: Instant.now(),
    editadoEm = this.editadoEm?.toInstant() ?: Instant.now(),
    statusSincronizacao = StatusSincronizacao.SINCRONIZADO,
)


/** Converte Entity para RemoteModel */
fun GradeEntity.toRemoteModel() = GradeRemoteModel(
    id = this.id,
    numero = this.numero,
    data = this.data.toTimestamp(),
    quantidadeBarris = this.quantidadeBarris,
    volumeHlNecessario = this.volumeHlNecessario,
    criadoEm = this.criadoEm.toTimestamp(),
    editadoEm = this.editadoEm?.toTimestamp(),
)

/** Converte LocalModel para Entity */
fun GradeLocalModel.toEntity() = GradeEntity(
    id = this.id,
    numero = this.numero,
    data = Instant.ofEpochMilli(data),
    quantidadeBarris = this.quantidadeBarris,
    volumeHlNecessario = this.volumeHlNecessario,
    criadoEm = Instant.ofEpochMilli(criadoEm),
    editadoEm = editadoEm?.let { Instant.ofEpochMilli(it) },
    statusSincronizacao = StatusSincronizacao.SINCRONIZADO,
)

/** Converte Entity para LocalModel */
fun GradeEntity.toLocalModel() = GradeLocalModel(
    id = this.id,
    numero = this.numero,
    data = this.data.toEpochMilli(),
    quantidadeBarris = this.quantidadeBarris,
    volumeHlNecessario = this.volumeHlNecessario,
    criadoEm = this.criadoEm.toEpochMilli(),
    editadoEm = this.editadoEm?.toEpochMilli(),
    statusSincronizacao = StatusSincronizacao.SINCRONIZADO,
)