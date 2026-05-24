package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.TipoMovimentacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.StatusProducao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class InsertMovimentacaoParams(
    val producaoId: String,
    val quantidade: Int,
    val horarioReferente: String
)

class InsertMovimentacaoUseCase @Inject constructor(
    private val movimentacaoRepository: MovimentacaoRepository,
    private val producaoRepository: ProducaoRepository,
) {

    suspend operator fun invoke(params: InsertMovimentacaoParams): Result<Unit> {
        val producaoId = params.producaoId
        val turnoId = 1
        val quantidade = params.quantidade

        if (producaoId.isBlank()) return Result.failure(IllegalArgumentException("producaoId não pode ser vazio"))
        if (quantidade == 0) return Result.failure(IllegalArgumentException("Quantidade programada deve ser diferente de zero"))

        val producao = producaoRepository.getOneById(producaoId)
            .first()
            ?: return Result.failure(IllegalArgumentException("Produção não encontrada"))

        val novaQuantidadeProduzida = producao.quantidadeProduzida + quantidade
        if (novaQuantidadeProduzida < 0) return Result.failure(
            IllegalArgumentException("Quantidade produzida não pode ficar negativa")
        )

        val criadoEm = Instant.now()
        val idGerado = UUID.randomUUID().toString()
        val tipo = if (quantidade < 0) TipoMovimentacao.SUBTRACAO else TipoMovimentacao.SOMA

        val movimentacao = MovimentacaoEntity(
            id = idGerado,
            producaoId = producaoId,
            turnoId = turnoId,
            horarioReferente = params.horarioReferente,
            quantidade = quantidade,
            tipo = tipo,
            criadoEm = criadoEm,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO,
        )

        movimentacaoRepository.insertMovimentacao(movimentacao = movimentacao)
            .getOrElse {
                return Result.failure(it)
            }

        val novoStatus = if (producao.quantidadeProgramada == novaQuantidadeProduzida)
            StatusProducao.CONCLUIDA else StatusProducao.EM_PRODUCAO

        val producaoAtualizada = producao.copy(
            quantidadeProduzida = novaQuantidadeProduzida,
            status = novoStatus,
            editadaEm = Instant.now(),
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
        )
        producaoRepository.updateProducao(producao = producaoAtualizada)
            .getOrElse { return Result.failure(it) }

        return Result.success(Unit)
    }

}