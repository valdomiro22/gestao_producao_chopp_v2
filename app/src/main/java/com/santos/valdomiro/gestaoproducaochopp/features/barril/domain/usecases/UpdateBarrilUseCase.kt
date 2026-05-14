package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class UpdateBarrilParams(
    val id: String,
    val criadoEm: Instant,
    val editadoEm: Instant? = null,
    val statusSincronizacao: StatusSincronizacao,
    val nome: String,
    val volume: Int = 0,
    val descartavel: Boolean = false,
)

class UpdateBarrilUseCase @Inject constructor(
    private val repository: BarrilRepository
) {

    suspend operator fun invoke(params: UpdateBarrilParams): Result<Unit> {
        val nome = params.nome.trim()
        val volume = params.volume

        if (nome.isBlank()) {
            return Result.failure(
                IllegalArgumentException("O nome do barril não pode estar vazio")
            )
        }

        if (volume <= 0) {
            return Result.failure(
                IllegalArgumentException("O volume do barril deve ser maior que zero")
            )
        }

        val editadoEm = Instant.now()

        val barril = BarrilEntity(
            id = params.id,
            nome = params.nome,
            volume = params.volume,
            criadoEm = params.criadoEm,
            descartavel = params.descartavel,
            editadoEm = editadoEm,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO,
        )

        repository.insertBarril(barril = barril)
            .getOrElse {
                return Result.failure(it)
            }

        return Result.success(Unit)
    }

}