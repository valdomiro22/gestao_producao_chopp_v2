package com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.screens.adicionarproduto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase.InsertProdutoParams
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase.InsertProdutoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdicionarProdutoViewModel @Inject constructor(
    private val insertProdutoUseCase: InsertProdutoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdicionarProdutoState())
    val uiState = _uiState.asStateFlow()

    fun onNomeChanged(value: String) {
        _uiState.update { it.copy(nome = value, erroNome = null) }
    }

    fun onPrazoValidadeChanged(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.update { it.copy(prazoValidade = filtered, erroPrazoValidade = null) }
    }

    fun adicionarProduto() {
        val currentState = _uiState.value

        if (!validar(state = currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }

            val prazoValidade = currentState.prazoValidade.toIntOrNull()
                ?: run {
                    _uiState.update {
                        it.copy(isLoading = false, erroPrazoValidade = "Prazo de validade inválido")
                    }
                    return@launch
                }

            if (prazoValidade <= 0) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroPrazoValidade = "Prazo de validade deve ser maior do que zero"
                    )
                }
                return@launch
            }

            val params = InsertProdutoParams(
                nome = currentState.nome,
                prazoValidade = prazoValidade
            )

            insertProdutoUseCase(params = params)
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSuccess = true) } }
                .onFailure { erro ->
                    _uiState.update { it.copy(isLoading = false, erro = erro.message) }
                }
        }

    }

    private fun validar(state: AdicionarProdutoState): Boolean {
        var isValid = true
        var newState = state

        if (state.nome.isBlank()) {
            isValid = false
            newState = newState.copy(erroNome = "Digite o nome")
        }

        if (state.prazoValidade.isBlank()) {
            isValid = false
            newState = newState.copy(erroPrazoValidade = "Digite o prazo de validade")
        }

        _uiState.update { newState }
        return isValid;
    }

}