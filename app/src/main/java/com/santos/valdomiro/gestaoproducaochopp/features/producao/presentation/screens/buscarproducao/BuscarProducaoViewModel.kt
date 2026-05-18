package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.buscarproducao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoDetalhada
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.GetOneProducaoUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.GetProducoesDetalhadasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuscarProducaoViewModel @Inject constructor(
    private val getUmaProducaoUseCase: GetOneProducaoUseCase,
    private val getProducoesDetalhadasUseCase: GetProducoesDetalhadasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ProducaoDetalhada>>>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun buscarProducao(producaoId: String) {
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
}
