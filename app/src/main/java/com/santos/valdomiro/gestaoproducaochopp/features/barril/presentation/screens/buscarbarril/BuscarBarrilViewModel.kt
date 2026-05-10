package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.buscarbarril

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.useCase.GetOneBarrilUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuscarBarrilViewModel @Inject constructor(
    private val getOneBarrilUseCase: GetOneBarrilUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<BarrilEntity>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun buscarBarril(barrilId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getOneBarrilUseCase(id = barrilId).fold(
                onSuccess = { barril ->
                    _uiState.value = UiState.Success(barril)
                },
                onFailure = { erro ->
                    _uiState.value = UiState.Error(erro.message ?: "Erro ao buscar Barril")
                }
            )
        }
    }
}