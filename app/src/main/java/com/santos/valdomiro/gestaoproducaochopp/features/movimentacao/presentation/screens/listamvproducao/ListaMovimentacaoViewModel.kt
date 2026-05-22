package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.screens.listamvproducao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase.DeleteMovProducaoUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase.GetAllOfProducaoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaMovimentacaoViewModel @Inject constructor(
    private val getAllOfProducaoUseCase: GetAllOfProducaoUseCase,
    private val deleteMovProducaoUseCase: DeleteMovProducaoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<MovimentacaoEntity>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getAllOfProducao(producaoId: String) {
        viewModelScope.launch {
            getAllOfProducaoUseCase(producaoId = producaoId)
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

    fun deletarProducao(producao: MovimentacaoEntity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            deleteMovProducaoUseCase(movProducao = producao)
                .onSuccess { getAllOfProducao(producao.id) }
                .onFailure { exception ->
                    _uiState.value = UiState.Error(
                        exception.message ?: "Erro al deletar movimentação da produção"
                    )
                }
        }
    }
}
