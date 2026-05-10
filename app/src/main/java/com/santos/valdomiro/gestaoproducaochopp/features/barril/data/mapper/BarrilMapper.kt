package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.dto.BarrilDto
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity

/** Converte DTO para Entity */
fun BarrilDto.toEntity() = BarrilEntity(
    id = this.id,
    nome = this.nome,
    volume = this.volume,
    descartavel = this.descartavel
)

/** Converte para Entity para DTO */
fun BarrilEntity.toDto() = BarrilDto(
    id = this.id,
    nome = this.nome,
    volume = this.volume,
    descartavel = this.descartavel
)
