package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.buscarbarril

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.GetOneBarrilUseCase
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

            getOneBarrilUseCase(barrilId = barrilId).collect { result ->
                result.fold(
                    onSuccess = { barril ->
                        _uiState.value = UiState.Success(barril)
                    },
                    onFailure = { error ->
                        _uiState.value = UiState.Error(
                            error.message ?: "Erro ao listar produções"
                        )
                    }
                )
            }

        }
    }
}