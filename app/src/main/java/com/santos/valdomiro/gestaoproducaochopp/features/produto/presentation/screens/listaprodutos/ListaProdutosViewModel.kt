package com.santos.valdomiro.gestaoproducaochopp.features.produto.presentation.screens.listaprodutos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase.DeleteProdutoUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase.GetAllProdutosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaProdutosViewModel @Inject constructor(
    private val getAllProdutosUseCase: GetAllProdutosUseCase,
    private val deleteProdutoUseCase: DeleteProdutoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ProdutoEntity>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getAll() {
        viewModelScope.launch {
            getAllProdutosUseCase()
                .onStart {
                    _uiState.value = UiState.Loading
                }
                .catch { erro ->
                    val mensagem = erro.message ?: "Erro ao carregar lista de produtos"
                    _uiState.value = UiState.Error(mensagem)
                }
                .collect { listaProdutos ->
                    _uiState.value = UiState.Success(listaProdutos)
                }
        }
    }

    fun deleteProduto(produto: ProdutoEntity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            deleteProdutoUseCase(produto = produto)
                .onSuccess { getAll() }
                .onFailure { exception ->
                    _uiState.value = UiState.Error(
                        exception.message ?: "Erro al deletar produto"
                    )
                }
        }
    }

}