package com.santos.valdomiro.gestaoproducaochopp.features.homescreen.screen

import androidx.lifecycle.ViewModel
import com.santos.valdomiro.gestaoproducaochopp.common.enums.Turno
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
//    private val deslogarUseCase: DeslogarUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _turnoSelecionado = MutableStateFlow(Turno.TURNO_A)
    val turnoSelecionado = _turnoSelecionado.asStateFlow()

    fun alterarTurno(novoTurno: Turno) {
        _turnoSelecionado.value = novoTurno
    }

//    fun deslogar() {
//        viewModelScope.launch {
//            _uiState.value = UiState.Loading
//
//            val result = deslogarUseCase()
//
//            result.onSuccess { _uiState.value = UiState.Success(Unit) }
//                .onFailure { _uiState.value = UiState.Error(it.message ?: "Erro desconhecido") }
//        }
//    }

    fun resetState() {
        _uiState.value = UiState.Loading
    }

}