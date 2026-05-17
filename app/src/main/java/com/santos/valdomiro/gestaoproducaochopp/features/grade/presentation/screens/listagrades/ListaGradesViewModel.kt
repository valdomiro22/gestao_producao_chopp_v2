package com.santos.valdomiro.gestaoproducaochopp.features.grade.presentation.screens.listagrades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.entity.GradeEntity
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase.DeleteGradeUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.usecase.GetAllGradesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaGradesViewModel @Inject constructor(
    private val getAllGradesUseCase: GetAllGradesUseCase,
    private val deleteGradeUseCase: DeleteGradeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<GradeEntity>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getAll() {
        viewModelScope.launch {
            getAllGradesUseCase()
                .onStart {
                    _uiState.value = UiState.Loading
                }
                .catch { erro ->
                    val mensagem = erro.message ?: "Erro ao carregar lista de grades"
                    _uiState.value = UiState.Error(mensagem)
                }
                .collect { listaGrades ->
                    _uiState.value = UiState.Success(listaGrades)
                }
        }
    }

    fun deletarGrade(grade: GradeEntity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            deleteGradeUseCase(grade = grade)
                .onSuccess { getAll() }
                .onFailure { exception ->
                    _uiState.value = UiState.Error(
                        exception.message ?: "Erro ao deletar grade"
                    )
                }
        }
    }
}