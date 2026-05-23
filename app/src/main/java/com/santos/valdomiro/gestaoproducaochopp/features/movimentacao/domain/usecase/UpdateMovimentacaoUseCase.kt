package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.TipoMovimentacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import java.time.Instant
import javax.inject.Inject

data class UpdateMovimentacaoParams(
    val id: String,
    val producaoId: String,
    val turnoId: Int,
    val horarioReferente: String,
    val quantidade: Int,
    val tipo: TipoMovimentacao,
    val criadoEm: Instant,
    val statusSincronizacao: StatusSincronizacao
)

class UpdateMovProducaoUseCase @Inject constructor(
    private val repository: MovimentacaoRepository
) {

    suspend operator fun invoke(params: UpdateMovimentacaoParams): Result<Unit> {
        val id = params.id
        val producaoId = params.producaoId
        val turnoId = params.turnoId
        val quantidade = params.quantidade
        val horarioReferente = params.horarioReferente

        if (id.isBlank()) return Result.failure(IllegalArgumentException("ID não pode ser vazio"))
        if (producaoId.isBlank()) return Result.failure(IllegalArgumentException("producaoId não pode ser vazio"))
        if (turnoId !in 1..3) return Result.failure(IllegalArgumentException("turnoId não corresponde a um turno válido"))
        if (quantidade == 0) return Result.failure(IllegalArgumentException("Quantidade programada deve ser diferente de zero"))

        val editadaEm = Instant.now()

        val movimentacao = MovimentacaoEntity(
            id = id,
            producaoId = producaoId,
            turnoId = turnoId,
            horarioReferente = horarioReferente,
            quantidade = quantidade,
            tipo = params.tipo,
            criadoEm = params.criadoEm,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
        )

        repository.updateMovimentacao(movimentacao = movimentacao)
            .getOrElse {
                return Result.failure(it)
            }
        return Result.success(Unit)
    }
}
