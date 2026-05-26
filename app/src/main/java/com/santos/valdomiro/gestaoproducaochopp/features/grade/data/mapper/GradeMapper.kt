package com.santos.valdomiro.gestaoproducaochopp.features.grade.data.mapper

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.helper.toTimestamp
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/** Converte RemoteModel para Entity */
fun GradeRemoteModel.toEntity() = GradeEntity(
    id = this.id,
    numero = this.numero,
    data = this.data
        ?.toInstant()
        ?.atZone(ZoneOffset.UTC)
        ?.toLocalDate()
        ?: LocalDate.now(),
    quantidadeBarris = this.quantidadeBarris,
    volumeHlNecessario = this.volumeHlNecessario,
    criadoEm = this.criadoEm?.toInstant() ?: Instant.now(),
    editadoEm = this.editadoEm?.toInstant(),
    statusSincronizacao = StatusSincronizacao.SINCRONIZADO,
)

/** Converte Entity para RemoteModel */
fun GradeEntity.toRemoteModel() = GradeRemoteModel(
    id = this.id,
    numero = this.numero,
    data = this.data
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toTimestamp(),
    quantidadeBarris = this.quantidadeBarris,
    volumeHlNecessario = this.volumeHlNecessario,
    criadoEm = this.criadoEm.toTimestamp(),
    editadoEm = this.editadoEm?.toTimestamp(),
)

/** Converte LocalModel para Entity */
fun GradeLocalModel.toEntity() = GradeEntity(
    id = this.id,
    numero = this.numero,
    data = Instant.ofEpochMilli(this.data)
        .atZone(ZoneOffset.UTC)
        .toLocalDate(),
    quantidadeBarris = this.quantidadeBarris,
    volumeHlNecessario = this.volumeHlNecessario,
    criadoEm = Instant.ofEpochMilli(this.criadoEm),
    editadoEm = this.editadoEm?.let { Instant.ofEpochMilli(it) },
    statusSincronizacao = this.statusSincronizacao,
)

/** Converte Entity para LocalModel */
fun GradeEntity.toLocalModel() = GradeLocalModel(
    id = this.id,
    numero = this.numero,
    data = this.data
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli(),
    quantidadeBarris = this.quantidadeBarris,
    volumeHlNecessario = this.volumeHlNecessario,
    criadoEm = this.criadoEm.toEpochMilli(),
    editadoEm = this.editadoEm?.toEpochMilli(),
    statusSincronizacao = this.statusSincronizacao,
)

/** Converte RemoteModel para LocalModel */
fun GradeRemoteModel.toLocalModel() = this
    .toEntity()
    .copy(statusSincronizacao = StatusSincronizacao.SINCRONIZADO)
    .toLocalModel()