package com.santos.valdomiro.gestaoproducaochopp.features.grade.presentation.screens.editargrade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase.GetOneGradeUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase.UpdateGradeParams
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase.UpdateGradeUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.grade.presentation.screens.adicionargrade.AdicionarGradeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditarGradeViewModel @Inject constructor(
    private val updateGradeUseCase: UpdateGradeUseCase,
    private val getOnGradeUseCase: GetOneGradeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarGradeState())
    val uiState = _uiState.asStateFlow()

    private var gradeRecuperada: GradeEntity? = null
    private var gradeIdAtual: String? = null

    fun onNumeroChanged(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.value = _uiState.value.copy(numero = filtered, erroNumero = null)
    }

    fun onDataChanged(value: LocalDate?) {
        _uiState.update { it.copy(data = value, erroData = null) }
    }

    fun buscarGrade(gradeId: String) {
        gradeIdAtual = gradeId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erroGeral = null) }

            getOnGradeUseCase(gradeId = gradeId).collect { result ->
                result.fold(
                    onSuccess = { grade ->
                        gradeRecuperada = grade

                        _uiState.update {
                            it.copy(
                                numero = grade.numero.toString(),
                                data = grade.data,
                                isLoading = false,
                                erroGeral = null
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                erroGeral = "Erro ao buscar a grade: ${error.message}"
                            )
                        }
                    }
                )
            }
        }
    }

    fun editarGrade() {
        val currentState = _uiState.value
        val id = gradeIdAtual ?: return

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

            val params = UpdateGradeParams(
                id = id,
                numero = numeroInt,
                data = currentState.data!!,
                criadoEm = gradeRecuperada?.criadoEm ?: Instant.now(),
            )

            updateGradeUseCase(params = params)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, erroGeral = null, isEditSuccess = true) }
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            erroGeral = erro.message ?: "Erro ao editar grade"
                        )
                    }
                }
        }
    }

    private fun validar(state: EditarGradeState): Boolean {
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
