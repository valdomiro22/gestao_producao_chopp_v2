package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.helper.ProducaoHelper
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.GetOneBarrilUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.StatusProducao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class InsertProducaoParams(
    val gradeId: String,
    val barrilId: String,
    val produtoId: String,
    val quantidadeProgramada: Int,
    val quantidadeProduzida: Int = 0,
    val volumeBarril: Int,
)

class InsertProducaoUseCase @Inject constructor(
    private val repository: ProducaoRepository,
) {

    suspend operator fun invoke(params: InsertProducaoParams): Result<Unit> {
        val gradeId = params.gradeId
        val barrilId = params.barrilId
        val produtoId = params.produtoId
        val quantidadeProgramada = params.quantidadeProgramada

        if (gradeId.isBlank()) return Result.failure(IllegalArgumentException("Grade ID não pode ser vazio"))
        if (barrilId.isBlank()) return Result.failure(IllegalArgumentException("Barril ID não pode ser vazio"))
        if (produtoId.isBlank()) return Result.failure(IllegalArgumentException("Produto ID não pode ser vazio"))
        if (quantidadeProgramada <= 0) return Result.failure(IllegalArgumentException("Quantidade programada deve ser maior que zero"))

        val criadoEm = Instant.now()
        val idGerado = UUID.randomUUID().toString()
        val vlNecessario = ProducaoHelper.calcularVolumeNecessario(
            quantidadeProgramada = params.quantidadeProgramada,
            volumeBarril = params.volumeBarril,
            quantidadeProduzida = params.quantidadeProduzida
        )

        val producao = ProducaoEntity(
            id = idGerado,
            gradeId = gradeId,
            barrilId = barrilId,
            produtoId = produtoId,
            quantidadeProgramada = quantidadeProgramada,
            quantidadeProduzida = params.quantidadeProduzida,
            criadoEm = criadoEm,
            status = StatusProducao.PENDENTE,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO,
            volumeNecessario = vlNecessario,
        )

        repository.insertProducao(producao = producao)
            .getOrElse {
                return Result.failure(it)
            }

        return Result.success(Unit)
    }
}
