package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.editarbarril

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.useCase.GetOneBarrilUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.useCase.UpdateBarrilUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarBarrilViewModel @Inject constructor(
    private val getBarrilByIdUseCase: GetOneBarrilUseCase,
    private val updateBarrilUseCase: UpdateBarrilUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarBarrilState())
    val uiState = _uiState.asStateFlow()

    private var barrilIdAtual: String? = null

    fun carregarBarril(barrilId: String) {
        barrilIdAtual = barrilId // Salva o ID

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }

            getBarrilByIdUseCase(barrilId).fold(
                onSuccess = { barril ->
                    _uiState.update {
                        it.copy(
                            nome = barril.nome,
                            volume = barril.volume.toString(),
                            descartavel = barril.descartavel,
                            isLoading = false
                        )
                    }
                },
                onFailure = { erro ->
                    _uiState.update {
                        it.copy(isLoading = false, erro = erro.message)
                    }
                }
            )
        }
    }

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

    fun atualizar() {
        val currentState = _uiState.value
        val id = barrilIdAtual ?: return // Proteção contra crash

        if (!validar(currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }

            val volumeInt = currentState.volume.toIntOrNull() ?: return@launch

            val barrilAtualizado = BarrilEntity(
                id = id,
                nome = currentState.nome,
                volume = volumeInt,
                descartavel = currentState.descartavel
            )

            updateBarrilUseCase(id, barrilAtualizado).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { erro ->
                    _uiState.update { it.copy(isLoading = false, erro = erro.message) }
                }
            )
        }
    }

    private fun validar(state: EditarBarrilState): Boolean {
        var isValid = true
        var newState = state

        if (state.nome.isBlank()) {
            isValid = false
            newState = newState.copy(erroNome = "Digite o nome")
        }

        if (state.volume.isBlank()) {
            isValid = false
            newState = newState.copy(erroVolume = "Digite o sobrenome")
        }

        _uiState.update { newState }

        return isValid;
    }
}