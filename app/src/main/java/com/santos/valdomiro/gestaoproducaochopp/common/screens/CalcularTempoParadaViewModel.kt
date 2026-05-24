package com.santos.valdomiro.gestaodeproducaodechoppandroidstudio.common.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.Duration
import java.time.LocalTime

class CalcularTempoParadaViewModel : androidx.lifecycle.ViewModel() {

    var inicio by mutableStateOf<LocalTime?>(null)
        private set

    var fim by mutableStateOf<LocalTime?>(null)
        private set

    var diferenca by mutableStateOf<Duration?>(null)
        private set

    fun atualizarInicio(novoInicio: LocalTime) {
        inicio = novoInicio
        calcularDiferenca()
    }

    fun atualizarFim(novoFim: LocalTime) {
        fim = novoFim
        calcularDiferenca()
    }

    private fun calcularDiferenca() {
        val i = inicio ?: return
        val f = fim ?: return

        var duration = Duration.between(i, f)

        // Se o fim for "antes" do início (passou da meia-noite)
        if (duration.isNegative) {
            duration = duration.plus(Duration.ofDays(1))
        }

        diferenca = duration
    }
}
