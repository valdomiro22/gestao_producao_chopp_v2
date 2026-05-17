package com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.helper.ProducaoHelper
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.StatusProducao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import java.time.Instant
import javax.inject.Inject

data class UpdateProducaoParams(
    val id: String,
    val gradeId: String,
    val barrilId: String,
    val produtoId: String,
    val criadoEm: Instant,
    val volumeBarril: Int,
    val status: StatusProducao,
    val quantidadeProgramada: Int,
    val quantidadeProduzida: Int = 0,
    val dataFimDeProducao: Instant? = null
)

class UpdateProducaoUseCase @Inject constructor(
    private val repository: ProducaoRepository
) {

    suspend operator fun invoke(params: UpdateProducaoParams): Result<Unit> {
        val id = params.id
        val gradeId = params.gradeId
        val barrilId = params.barrilId
        val produtoId = params.produtoId
        val quantidadeProgramada = params.quantidadeProgramada

        if (id.isBlank()) return Result.failure(IllegalArgumentException("ID não pode ser vazio"))
        if (gradeId.isBlank()) return Result.failure(IllegalArgumentException("Grade ID não pode ser vazio"))
        if (barrilId.isBlank()) return Result.failure(IllegalArgumentException("Barril ID não pode ser vazio"))
        if (produtoId.isBlank()) return Result.failure(IllegalArgumentException("Produto ID não pode ser vazio"))
        if (quantidadeProgramada <= 0) return Result.failure(IllegalArgumentException("Quantidade programada deve ser maior que zero"))

        val editadaEm = Instant.now()
        val vlNecessario = ProducaoHelper.calcularVolumeNecessario(
            quantidadeProgramada = params.quantidadeProgramada,
            volumeBarril = params.volumeBarril,
            quantidadeProduzida = params.quantidadeProduzida
        )

        val producaoAtualizada = ProducaoEntity(
            id = id,
            gradeId = gradeId,
            barrilId = barrilId,
            produtoId = produtoId,
            quantidadeProgramada = quantidadeProgramada,
            quantidadeProduzida = params.quantidadeProduzida,
            criadoEm = params.criadoEm,
            editadaEm = editadaEm,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO,
            status = params.status,
            dataFimDeProducao = params.dataFimDeProducao,
            volumeNecessario = vlNecessario
        )

        repository.updateProducao(producao = producaoAtualizada)
            .getOrElse {
                return Result.failure(it)
            }

        return Result.success(Unit)
    }

}