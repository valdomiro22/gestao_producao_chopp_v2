package com.santos.valdomiro.gestaoproducaochopp.features.grade.presentation.screens.adicionargrade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase.InsertGradeParams
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase.InsertGradeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AdicionarGradeViewModel @Inject constructor(
    private val insertGradeUseCase: InsertGradeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdicionarGradeState())
    val uiState = _uiState.asStateFlow()

    fun onNumeroChanged(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.value = _uiState.value.copy(numero = filtered, erroNumero = null)
    }

    fun onDataChanged(value: LocalDate?) {
        _uiState.update { it.copy(data = value, erroData = null) }
    }

    fun inserirGrade() {
        val currentState = _uiState.value

        if (!validar(currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erroGeral = null) }

            val numeroInt = currentState.numero.toIntOrNull()
                ?: run {
                    _uiState.update {
                        it.copy(isLoading = false, erroNumero = "Número da grade inválido")
                    }
                    return@launch
                }

            if (numeroInt <= 0) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroNumero = "Número da grade deve ser maior do que zero"
                    )
                }
                return@launch
            }

            val params = InsertGradeParams(
                numero = numeroInt,
                data = currentState.data!!,
            )

            insertGradeUseCase(params = params)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(isLoading = false, erroGeral = erro.message) }
                }
        }
    }

    private fun validar(state: AdicionarGradeState): Boolean {
        var isValid = true
        var newState = state

        if (state.numero.isBlank()) {
            isValid = false
            newState = newState.copy(erroNumero = "Digite o número")
        }

        if (state.data == null) {
            isValid = false
            newState = newState.copy(erroData = "Selecione a data referente a grade")
        }

        _uiState.update { newState }
        return isValid;
    }

}
