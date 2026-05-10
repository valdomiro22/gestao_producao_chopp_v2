package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.editarbarril

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
class EditarUmBarrilViewModel @Inject constructor(
    private val getOneBarrilUseCase: GetOneBarrilUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<BarrilEntity>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getBarril(barrilId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = getOneBarrilUseCase(barrilId)

            result.onSuccess { barril ->
                _uiState.value = UiState.Success(barril)
            }.onFailure {
                val mensagem = it.message ?: "Erro ao carregar lista de barris"
                _uiState.value = UiState.Error(mensagem)
            }
        }

    }

}