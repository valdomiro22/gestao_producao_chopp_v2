package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.enums.Turno
import com.santos.valdomiro.gestaoproducaochopp.common.helper.formatarHorario
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.TipoMovimentacao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class InsertMovimentacaoParams(
    val producaoId: String,
    val quantidade: Int
)

class InsertMovimentacaoUseCase @Inject constructor(
    private val repository: MovimentacaoRepository
) {

    suspend operator fun invoke(params: InsertMovimentacaoParams): Result<Unit> {
        val producaoId = params.producaoId
//        val turnoId = params.turno.id
        val turnoId = 1
        val quantidade = params.quantidade

        if (producaoId.isBlank()) return Result.failure(IllegalArgumentException("producaoId não pode ser vazio"))
        if (turnoId !in 1..3) return Result.failure(IllegalArgumentException("turnoId não corresponde a um turno válido"))
        if (quantidade == 0) return Result.failure(IllegalArgumentException("Quantidade programada deve ser diferente de zero"))

        val criadoEm = Instant.now()
        val idGerado = UUID.randomUUID().toString()
        val horarioFormatado = Instant.now().formatarHorario().replace(":","").toIntOrNull()
        val horarioReferente = horarioFormatado ?: -1
        val tipo = if (quantidade < 0) TipoMovimentacao.SUBTRACAO else TipoMovimentacao.SOMA

        val movimentacao = MovimentacaoEntity(
            id = idGerado,
            producaoId = producaoId,
            turnoId = turnoId,
            horarioReferente = horarioReferente,
            quantidade = quantidade,
            tipo = tipo,
            criadoEm = criadoEm,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO,
        )

        repository.insertMovimentacao(movimentacao = movimentacao)
            .getOrElse {
                return Result.failure(it)
            }
        return Result.success(Unit)
    }

}