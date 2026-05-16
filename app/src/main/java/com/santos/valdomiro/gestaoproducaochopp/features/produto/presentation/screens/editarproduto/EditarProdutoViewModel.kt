package com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.screens.editarproduto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase.EditarProdutoParams
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase.GetOneProdutoUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase.UpdateProdutoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class EditarProdutoViewModel @Inject constructor(
    private val updateProdutoUseCase: UpdateProdutoUseCase,
    private val getOneProdutoUseCase: GetOneProdutoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarProdutoState())
    val uiState = _uiState.asStateFlow()

    private var produtoBuscado: ProdutoEntity? = null
    private var produtoIdAtual: String? = null

    fun onNomeChanged(value: String) {
        _uiState.update { it.copy(nome = value, erroNome = null) }
    }

    fun onPrazoValidadeChanged(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.update { it.copy(prazoValidade = filtered, erroPrazoValidade = null) }
    }

    fun buscarProduto(produtoId: String) {
        produtoIdAtual = produtoId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }

            getOneProdutoUseCase(produtoId = produtoId).collect { result ->
                result.fold(
                    onSuccess = { produto ->
                        produtoBuscado = produto

                        _uiState.update {
                            it.copy(
                                nome = produto.nome,
                                prazoValidade = produto.prazoValidade.toString(),
                                isLoading = false,
                                erro = null
                            )
                        }
                    },
                    onFailure = { erro ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                erro = erro.message ?: "Erro ao buscar prodto"
                            )
                        }
                    }

                )
            }
        }
    }

    fun editarProduto() {
        val currentState = _uiState.value
        val id = produtoIdAtual ?: return

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

            val params = EditarProdutoParams(
                id = id,
                nome = currentState.nome,
                prazoValidade = prazoValidade,
                criadoEm = produtoBuscado?.criadoEm ?: Instant.now(),
                editadoEm = Instant.now(),
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO,
            )

            updateProdutoUseCase(params = params)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            erro = null,
                            isEditSuccess = true
                        )
                    }
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            erro = erro.message ?: "Erro ao editar produto"
                        )
                    }
                }
        }

    }

    private fun validar(state: EditarProdutoState): Boolean {
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