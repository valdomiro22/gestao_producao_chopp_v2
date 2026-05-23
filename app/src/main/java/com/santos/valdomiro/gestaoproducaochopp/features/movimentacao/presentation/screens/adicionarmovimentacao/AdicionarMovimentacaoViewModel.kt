package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.presentation.screens.adicionarmovimentacao

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase.GetSaldoMovimentacaoHorarioUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase.InsertMovimentacaoParams
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.usecase.InsertMovimentacaoUseCase
import com.santos.valdomiro.gestaoproducaochopp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdicionarMovimentacaoViewModel @Inject constructor(
    private val insertMovimentacaoUseCase: InsertMovimentacaoUseCase,
    private val getSaldoMovimentacaoHorarioUseCase: GetSaldoMovimentacaoHorarioUseCase,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow(AdicionarMovimentacaoState())
    val uiState = _uiState.asStateFlow()

    fun onQuantidadeChanged(value: String) {
        // Permite apenas dígitos e o sinal de menos se ele estiver na primeira posição
        val filtered = value.filterIndexed { index, char ->
            char.isDigit() || (index == 0 && char == '-')
        }

        Log.d(TAG, "onQuantidadeChanged: Qt Digitada: $value | Filtrada: $filtered")
        _uiState.update { it.copy(quantidade = filtered, erroQuantidade = null) }
    }

    fun inserirMovimentacao(producaoId: String, horarioTurno: String) {
        val currentState = _uiState.value

        if (!validar(state = currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erroGeral = null) }

            val quantidadeInt = currentState.quantidade.toIntOrNull()
                ?: run {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            erroQuantidade = "Quantidade inválida"
                        )
                    }
                    return@launch
                }

            if (quantidadeInt == 0) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroQuantidade = "Quantidade não pode ser igual a zero"
                    )
                }
                return@launch
            }

            getSaldoMovimentacaoHorarioUseCase(
                producaoId = producaoId,
                horarioReferente = horarioTurno
            )
                .first()
                .let { movimentacoes ->
                    val saldoAtualDoHorario = movimentacoes.sumOf { it.quantidade }
                    val saldoFinalDoHorario = saldoAtualDoHorario + quantidadeInt

                    if (saldoFinalDoHorario < 0) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                erroQuantidade = "Saldo insuficiente neste horário. Atual: $saldoAtualDoHorario"
                            )
                        }
                        return@launch
                    }

                    val params = InsertMovimentacaoParams(
                        producaoId = producaoId,
                        quantidade = quantidadeInt,
                        horarioReferente = horarioTurno
                    )

                    insertMovimentacaoUseCase(params)
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = true
                                )
                            }
                        }
                        .onFailure { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    erroGeral = error.message ?: "Erro ao inserir movimentação"
                                )
                            }
                        }
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
