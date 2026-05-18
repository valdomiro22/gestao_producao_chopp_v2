package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.listaproducoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoDetalhada
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.DeleteProducaoUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.GetProducoesDetalhadasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaProducaoViewModel @Inject constructor(
    private val getProducoesDetalhadasUseCase: GetProducoesDetalhadasUseCase,
    private val deleteProducaoUseCase: DeleteProducaoUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<ProducaoDetalhada>>>(UiState.Idle)

    val uiState = _uiState.asStateFlow()

    fun getAll() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getProducoesDetalhadasUseCase().collect { result ->
                result.fold(
                    onSuccess = { lista ->
                        _uiState.value = UiState.Success(lista)
                    },
                    onFailure = { erro ->
                        _uiState.value = UiState.Error(
                            erro.message ?: "Erro ao listar produções"
                        )
                    }
                )
            }
        }
    }

    fun deletarProducao(producao: ProducaoEntity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            deleteProducaoUseCase(producao = producao)
                .onSuccess { getAll() }
                .onFailure { exception ->
                    _uiState.value = UiState.Error(
                        exception.message ?: "Erro al deletar produção"
                    )
                }
        }
    }
}