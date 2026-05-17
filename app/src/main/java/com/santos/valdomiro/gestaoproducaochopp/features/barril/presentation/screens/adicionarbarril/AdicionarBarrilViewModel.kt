package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.adicionarbarril

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.InsertBarrilParams
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.InsertBarrilUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdicionarBarrilViewModel @Inject constructor(
    private val insertBarrilUseCase: InsertBarrilUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdicionarBarrilState())
    val uiState = _uiState.asStateFlow()

    fun onNomeChanged(value: String) {
        _uiState.update { it.copy(nome = value, erroNome = null) }
    }

    fun onVolumeChanged(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.update { it.copy(volume = filtered, erroVolume = null) }
    }

    fun onDescartavelChanged(value: Boolean) {
        _uiState.update { it.copy(descartavel = value) }
    }

    fun inserirBarril() {
        val currentState = _uiState.value

        if (!validar(state = currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erroGeral = null) }

            val volumeInt = currentState.volume.toIntOrNull()
                ?: run {
                    _uiState.update {
                        it.copy(isLoading = false, erroVolume = "Volume inválido")
                    }
                    return@launch
                }

            if (volumeInt <= 0) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroVolume = "Volume deve ser maior do que zero"
                    )
                }
                return@launch
            }

            val params = InsertBarrilParams(
                nome = currentState.nome,
                volume = volumeInt,
                descartavel = currentState.descartavel
            )

            insertBarrilUseCase(params = params)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(isLoading = false, erroGeral = erro.message) }
                }
        }
    }

    private fun validar(state: AdicionarBarrilState): Boolean {
        var isValid = true
        var newState = state

        if (state.nome.isBlank()) {
            isValid = false
            newState = newState.copy(erroNome = "Digite o nome")
        }

        if (state.volume.isBlank()) {
            isValid = false
            newState = newState.copy(erroVolume = "Digite o volume")
        }

        _uiState.update { newState }
        return isValid;
    }

}