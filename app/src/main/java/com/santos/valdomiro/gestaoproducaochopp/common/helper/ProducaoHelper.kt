package com.santos.valdomiro.gestaoproducaochopp.common.helper

object ProducaoHelper {
    fun calcularVolumeNecessario(
        quantidadeProgramada: Int,
        quantidadeProduzida: Int,
        volumeBarril: Int
    ): Double {
        val quantidadePendente = quantidadeProgramada - quantidadeProduzida
        return (quantidadePendente * volumeBarril) / 100.0
    }

    fun calcularVolumeNecessarioParaGrade(
        quantidadeProgramada: Int,
        volumeBarril: Int
    ): Double {
        return (quantidadeProgramada * volumeBarril) / 100.0
    }
}