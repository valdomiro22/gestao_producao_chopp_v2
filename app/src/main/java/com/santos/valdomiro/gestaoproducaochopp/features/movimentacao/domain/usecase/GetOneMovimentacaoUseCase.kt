package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOneMovimentacaoUseCase @Inject constructor(
    private val repository: MovimentacaoRepository
) {

    operator fun invoke(movimentacaoId: String): Flow<Result<MovimentacaoEntity>> {
        if (movimentacaoId.isBlank()) {
            return flowOf(Result.failure(IllegalArgumentException("ID da movimentação inválido")))
        }

        return repository.getOneById(movimentacaoId = movimentacaoId)
            .map { movimentacao ->
                if (movimentacao != null) {
                    Result.success(movimentacao)
                } else {
                    Result.failure(Exception("Movimentação não encontrada com o ID: $movimentacaoId"))
                }
            }
    }

}