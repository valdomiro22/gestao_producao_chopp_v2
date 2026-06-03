package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.screens.listamvproducao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.state.UiState
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.entity.MovimentacaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase.GetAllOfProducaoUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase.GetMovimentacoesPorHorarioUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase.SincronizarMovimentacoesRealtimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapMovimentacoesDaProducaoViewModel @Inject constructor(
    private val getMovimentacoesDoHorarioUseCase: GetMovimentacoesPorHorarioUseCase,
    private val getAllOfProducaoUseCase: GetAllOfProducaoUseCase,
    private val sincronizarMovimentacoesRealtimeUseCase: SincronizarMovimentacoesRealtimeUseCase,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<Map<String, MovimentacaoEntity>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var realtimeIniciado = false

    fun iniciarSincronizacaoRealtime() {
        if (realtimeIniciado) return

        realtimeIniciado = true

        viewModelScope.launch {
            sincronizarMovimentacoesRealtimeUseCase()
                .catch { erro ->
                    println("Erro no realtime de movimentações: ${erro.message}")
                }
                .collect()
        }
    }

    fun getMovimentacoesDaProducao(producaoId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            getAllOfProducaoUseCase(producaoId = producaoId)
                .onStart {
                    _uiState.value = UiState.Loading
                }
                .catch { error ->
                    val mensagem = error.message ?: "Erro ao carregar lista de movimentações"
                    _uiState.value = UiState.Error(mensagem)
                }
                .collect { listaMovimentacoes ->
                    val mapaAgrupadoPrHorarioReferente =
                        listaMovimentacoes.groupBy { it.horarioReferente }

                    val mapaSomatorio = mapaAgrupadoPrHorarioReferente.mapValues { itemDoMapa ->
                        val listaHorarios = itemDoMapa.value

                        listaHorarios.first().copy(
                            quantidade = listaHorarios.sumOf { it.quantidade }
                        )
                    }

                    _uiState.value = UiState.Success(mapaSomatorio)
                }
        }
    }
}