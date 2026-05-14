package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.listabarris

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.DeleteBarrilUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.GetAllBarrisUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaBarrisViewModel @Inject constructor(
    private val getAllBarrisUseCase: GetAllBarrisUseCase,
    private val deletarBarrilUseCase: DeleteBarrilUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BarrilEntity>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getAll() {
        viewModelScope.launch {
            getAllBarrisUseCase()
                .onStart {
                    _uiState.value = UiState.Loading
                }
                .catch { erro ->
                    val mensagem = erro.message ?: "Erro ao carregar lista de barris"
                    _uiState.value = UiState.Error(mensagem)
                }
                .collect { listaBarris ->
                    _uiState.value = UiState.Success(listaBarris)
                }
        }
    }

    fun deletarBarril(barril: BarrilEntity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            deletarBarrilUseCase(barril = barril)
                .onSuccess { getAll() }
                .onFailure { exception ->
                    _uiState.value = UiState.Error(
                        exception.message ?: "Erro al deletar barril"
                    )
                }
        }
    }
}