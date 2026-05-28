package com.santos.valdomiro.gestaoproducaochopp.common.screens.verificarvalidade

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class VerificarValidadeViewModel : ViewModel() {

    var dataInicio by mutableStateOf<LocalDate?>(null)
        private set

    var dataFim by mutableStateOf<LocalDate?>(null)
        private set

    var quantidadeDias by mutableStateOf<Long?>(null)
        private set

    fun atualizarDataInicio(novaDataInicio: LocalDate) {
        dataInicio = novaDataInicio
        calcularQuantidadeDias()
    }

    fun atualizarDataFim(novaDataFim: LocalDate) {
        dataFim = novaDataFim
        calcularQuantidadeDias()
    }

    private fun calcularQuantidadeDias() {
        val inicio = dataInicio ?: return
        val fim = dataFim ?: return

        quantidadeDias = ChronoUnit.DAYS.between(inicio, fim)
    }
}