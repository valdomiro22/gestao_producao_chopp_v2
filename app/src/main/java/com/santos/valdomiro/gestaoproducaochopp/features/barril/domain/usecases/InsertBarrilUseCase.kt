package com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases

import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class InsertBarrilParams(
    val nome: String,
    val volume: Int = 0,
    val descartavel: Boolean = false,
)

class InsertBarrilUseCase @Inject constructor(
    private val repository: BarrilRepository
) {

    suspend operator fun invoke(params: InsertBarrilParams): Result<Unit> {
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

        val criadoEm = Instant.now()
        val idGerado = UUID.randomUUID().toString()

        val barril = BarrilEntity(
            id = idGerado,
            nome = nome,
            volume = volume,
            criadoEm = criadoEm,
            descartavel = params.descartavel,
            statusSincronizacao = StatusSincronizacao.AGUARDANDO_ENVIO,
        )

        repository.insertBarril(barril = barril)
            .getOrElse {
                return Result.failure(it)
            }

        return Result.success(Unit)
    }

}