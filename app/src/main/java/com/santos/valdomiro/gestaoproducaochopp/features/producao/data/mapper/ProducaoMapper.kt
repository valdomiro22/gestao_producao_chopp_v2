package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.mapper

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.helper.toTimestamp
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model.ProducaoLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model.ProducaoRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import java.time.Instant

/** Converte RemoteModel para Entity */
fun ProducaoRemoteModel.toEntity() = ProducaoEntity(
    id = this.id,
    gradeId = this.gradeId,
    barrilId = this.barrilId,
    produtoId = this.produtoId,
    quantidadeProgramada = this.quantidadeProgramada,
    quantidadeProduzida = this.quantidadeProduzida,
    status = this.status,
    criadoEm = this.criadoEm?.toInstant() ?: Instant.now(),
    editadaEm = this.editadoEm?.toInstant(),
    dataFimDeProducao = this.dataFimDeProducao?.toInstant(),
    statusSincronizacao = StatusSincronizacao.SINCRONIZADO,
    volumeNecessario = this.volumeNecessario
)

/** Converte Entity para RemoteModel */
fun ProducaoEntity.toRemoteModel() = ProducaoRemoteModel(
    id = this.id,
    gradeId = this.gradeId,
    barrilId = this.barrilId,
    produtoId = this.produtoId,
    quantidadeProgramada = this.quantidadeProgramada,
    quantidadeProduzida = this.quantidadeProduzida,
    status = this.status,
    criadoEm = this.criadoEm.toTimestamp(),
    editadoEm = this.editadaEm?.toTimestamp(),
    dataFimDeProducao = this.dataFimDeProducao?.toTimestamp(),
    statusSincronizacao = this.statusSincronizacao,
    volumeNecessario = this.volumeNecessario
)

/** Converte LocalModel para Entity */
fun ProducaoLocalModel.toEntity() = ProducaoEntity(
    id = this.id,
    gradeId = this.gradeId,
    barrilId = this.barrilId,
    produtoId = this.produtoId,
    quantidadeProgramada = this.quantidadeProgramada,
    quantidadeProduzida = this.quantidadeProduzida,
    status = this.status,
    criadoEm = Instant.ofEpochMilli(this.criadoEm),
    editadaEm = this.editadoEm?.let { Instant.ofEpochMilli(it) },
    dataFimDeProducao = this.dataFimDeProducao?.let { Instant.ofEpochMilli(it) },
    statusSincronizacao = this.statusSincronizacao,
    volumeNecessario = this.volumeNecessario
)

/** Converte Entity para LocalModel */
fun ProducaoEntity.toLocalModel() = ProducaoLocalModel(
    id = this.id,
    gradeId = this.gradeId,
    barrilId = this.barrilId,
    produtoId = this.produtoId,
    quantidadeProgramada = this.quantidadeProgramada,
    quantidadeProduzida = this.quantidadeProduzida,
    status = this.status,
    criadoEm = this.criadoEm.toEpochMilli(),
    editadoEm = this.editadaEm?.toEpochMilli(),
    dataFimDeProducao = this.dataFimDeProducao?.toEpochMilli(),
    statusSincronizacao = this.statusSincronizacao,
    volumeNecessario = this.volumeNecessario
)

/** Converte RemoteModel para LocalModel */
fun ProducaoRemoteModel.toLocalModel() = this
    .toEntity()
    .copy(statusSincronizacao = StatusSincronizacao.SINCRONIZADO)
    .toLocalModel()