package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.buscarproducao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoDetalhada
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.GetOneProducaoDetalhadaUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.GetOneProducaoUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.GetProducoesDetalhadasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuscarProducaoDetalhadaViewModel @Inject constructor(
    private val getOneProducaoDetalhadaUseCase: GetOneProducaoDetalhadaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ProducaoDetalhada>>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun buscarProducaoDatalhada(producaoId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getOneProducaoDetalhadaUseCase(producaoId = producaoId).collect { result ->
                result.fold(
                    onSuccess = { producao ->
                        _uiState.value = UiState.Success(producao)
                    },
                    onFailure = { erro ->
                        _uiState.value = UiState.Error(
                            erro.message ?: "Erro ao "
                        )
                    }
                )
            }
        }
    }
}
