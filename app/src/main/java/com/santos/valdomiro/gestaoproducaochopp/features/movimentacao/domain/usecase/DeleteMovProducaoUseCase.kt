package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import javax.inject.Inject

class DeleteMovProducaoUseCase @Inject constructor(
    private val repository: MovimentacaoRepository
) {

    suspend operator fun invoke(movProducao: MovimentacaoEntity): Result<Unit> {
        if (movProducao.id.isBlank()) {
            return Result.failure(
                IllegalArgumentException("ID da movimentação inválido")
            )
        }

        return repository.deleteMovimentacao(movimentacao = movProducao)
    }

}