package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.StatusProducao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import java.time.Instant
import javax.inject.Inject

class AtualizarQuantidadeProduzidaUseCase @Inject constructor(
    private val repository: ProducaoRepository
) {
    suspend operator fun invoke(
        producao: ProducaoEntity,
        novaQuantidadeProduzida: Int
    ): Result<Unit> {
        if (novaQuantidadeProduzida < 0) {
            return Result.failure(
                IllegalArgumentException("Quantidade produzida não pode ser negativa")
            )
        }

        if (novaQuantidadeProduzida > producao.quantidadeProgramada) {
            return Result.failure(
                IllegalArgumentException("Quantidade produzida não pode ser maior que a programada")
            )
        }

        val agora = Instant.now()

        val novoStatus = when {
            novaQuantidadeProduzida >= producao.quantidadeProgramada -> StatusProducao.CONCLUIDA
            novaQuantidadeProduzida > 0 -> StatusProducao.EM_PRODUCAO
            else -> StatusProducao.PENDENTE
        }

        val producaoAtualizada = producao.copy(
            quantidadeProduzida = novaQuantidadeProduzida,
            status = novoStatus,
            editadaEm = agora,
            dataFimDeProducao = if (novoStatus == StatusProducao.CONCLUIDA) agora else null,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
        )

        return repository.updateProducao(producaoAtualizada)
    }
}