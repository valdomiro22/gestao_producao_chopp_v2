package com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository.ProdutoRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class InsertProdutoParams(
    val nome: String,
    val prazoValidade: Int,
)

class InsertProdutoUseCase @Inject constructor(
    private val repository: ProdutoRepository
) {

    suspend operator fun invoke(params: InsertProdutoParams): Result<Unit> {
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

        val criadoEm = Instant.now()
        val idGerado = UUID.randomUUID().toString()

        val produto = ProdutoEntity(
            id = idGerado,
            nome = nome,
            prazoValidade = prazoValidade,
            criadoEm = criadoEm,
            editadoEm = null,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO
        )

        repository.insertProduto(produto = produto)
            .getOrElse {
                return Result.failure(it)
            }

        return Result.success(Unit)
    }

}