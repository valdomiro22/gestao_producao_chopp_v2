package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.screens.adicionarmovimentacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase.InsertMovimentacaoParams
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase.InsertMovimentacaoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdicionarMovimentacaoViewModel @Inject constructor(
    private val insertMovimentacaoUseCase: InsertMovimentacaoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdicionarMovimentacaoState())
    val uiState = _uiState.asStateFlow()

    fun onQuantidadeChanged(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.value = _uiState.value.copy(quantidade = filtered, erroQuantidade = null)
    }

    fun inserirMovimentacal(producaoId: String, horarioTurno: String) {
        val currentState = _uiState.value

        if (!validar(state = currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erroGeral = null) }

            val quantidadeInt = currentState.quantidade.toIntOrNull()
                ?: run {
                    _uiState.update {
                        it.copy(isLoading = false, erroQuantidade = "Quantidade inválida")
                    }
                    return@launch
                }

            if (quantidadeInt <= 0) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroQuantidade = "Quantidade deve ser maior do que zero"
                    )
                }
                return@launch
            }

            val params = InsertMovimentacaoParams(
                producaoId = producaoId,
                quantidade = quantidadeInt,
                horarioReferente = horarioTurno
            )

            insertMovimentacaoUseCase(params = params)
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSuccess = true) } }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, erroGeral = error.toString()) }
                }
        }
    }

    fun resetState() {
        _uiState.update {
            it.copy(
                quantidade = "",
                isLoading = false,
                isSuccess = false,
                erroQuantidade = null,
                erroGeral = null
            )
        }
    }

    fun validar(state: AdicionarMovimentacaoState): Boolean {
        var isValid = true
        var newState = state

        if (state.quantidade.isEmpty()) {
            isValid = false
            newState = newState.copy(erroQuantidade = "Digite a quantidade")
        }

        _uiState.update { newState }
        return isValid
    }
}
