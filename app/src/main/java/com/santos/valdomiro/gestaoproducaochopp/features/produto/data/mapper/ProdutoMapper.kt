package com.santos.valdomiro.gestaoproducaochopp.features.produto.data.mapper

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.helper.toTimestamp
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model.ProdutoLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model.ProdutoRemoteModel
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import java.time.Instant

/** Converte RemoteModel para Entity */
fun ProdutoRemoteModel.toEntity() = ProdutoEntity(
    id = this.id,
    nome = this.nome,
    editadoEm = this.editadoEm?.toInstant() ?: Instant.now(),
    statusSincronizacao = StatusSincronizacao.SINCRONIZADO,
    criadoEm = this.criadoEm?.toInstant() ?: Instant.now(),
    prazoValidade = this.prazoValidade
)

/** Converte Entity para RemoteModel */
fun ProdutoEntity.toRemoteModel() = ProdutoRemoteModel(
    id = this.id,
    nome = this.nome,
    criadoEm = this.criadoEm.toTimestamp(),
    editadoEm = this.editadoEm?.toTimestamp(),
    prazoValidade = this.prazoValidade,
    statusSincronizacao = this.statusSincronizacao,
)

/** Converte LocalModel para Entity */
fun ProdutoLocalModel.toEntity() = ProdutoEntity(
    id = this.id,
    nome = this.nome,
    statusSincronizacao = this.statusSincronizacao,
    criadoEm = Instant.ofEpochMilli(criadoEm),
    editadoEm = Instant.ofEpochMilli(criadoEm),
    prazoValidade = this.prazoValidade,
)

/** Converte Entity para LocalModel */
fun ProdutoEntity.toLocalModel() = ProdutoLocalModel(
    id = this.id,
    nome = this.nome,
    criadoEm = this.criadoEm.toEpochMilli(),
    statusSincronizacao = this.statusSincronizacao,
    editadoEm = this.editadoEm?.toEpochMilli(),
    prazoValidade = this.prazoValidade,
)

/** Converte RemoteModel para LocalModel */
fun ProdutoRemoteModel.toLocalModel() = this
    .toEntity()
    .copy(statusSincronizacao = StatusSincronizacao.SINCRONIZADO)
    .toLocalModel()