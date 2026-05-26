package com.santos.valdomiro.gestaoproducaochopp.common.usecase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SincronizacaoInicialViewModel @Inject constructor(
    private val sincronizarDadosIniciaisUseCase: SincronizarDadosIniciaisUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SincronizacaoInicialState())
    val uiState = _uiState.asStateFlow()

    fun sincronizarAoAbrirApp() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    erro = null
                )
            }

            val resultado = sincronizarDadosIniciaisUseCase()

            resultado.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sincronizado = true,
                            erro = null
                        )
                    }
                },
                onFailure = { erro ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sincronizado = false,
                            erro = erro.message ?: "Erro ao sincronizar dados"
                        )
                    }
                }
            )
        }
    }
}