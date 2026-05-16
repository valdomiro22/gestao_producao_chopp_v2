package com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository.ProdutoRepository
import java.time.Instant
import javax.inject.Inject

data class EditarProdutoParams(
    val id: String,
    val nome: String,
    val prazoValidade: Int = 0,
    val criadoEm: Instant,
    val editadoEm: Instant? = null,
    val statusSincronizacao: StatusSincronizacao
)

class UpdateProdutoUseCase @Inject constructor(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(params: EditarProdutoParams): Result<Unit> {
        val nome = params.nome.trim()
        val prazoValidade = params.prazoValidade

        if (nome.isBlank()) {
            return Result.failure(
                IllegalArgumentException("O nome do produto não pode estar vazio")
            )
        }

        if (prazoValidade <= 0) {
            return Result.failure(
                IllegalArgumentException("O prazo de validade do produto deve ser maior que zero")
            )
        }

        val editadoEm = Instant.now()

        val produto = ProdutoEntity(
            id = params.id,
            nome = params.nome,
            prazoValidade = params.prazoValidade,
            criadoEm = params.criadoEm,
            editadoEm = editadoEm,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO
        )

        repository.updateProduto(produto = produto)
            .getOrElse {
                return Result.failure(it)
            }

        return Result.success(Unit)
    }

}