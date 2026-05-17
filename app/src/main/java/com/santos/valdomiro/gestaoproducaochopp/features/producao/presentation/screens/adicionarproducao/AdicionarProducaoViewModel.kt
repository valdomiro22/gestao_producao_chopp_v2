package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.adicionarproducao

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.GetOneBarrilUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.InsertProducaoParams
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.InsertProducaoUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdicionarProducaoViewModel @Inject constructor(
    private val insertProducaoUseCase: InsertProducaoUseCase,
    private val getOneBarrilUseCase: GetOneBarrilUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdicionarProducaoState())
    val uiState = _uiState.asStateFlow()

    fun onQtProgramadaChanged(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                quantidadeProgramada = filtered,
                erroQuantidadeProgramada = null
            )
        }
    }

    fun onQtProduzidaChanged(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.update { it.copy(quantidadeProduzida = filtered, erroQuantidadeProduzida = null) }
    }

    fun onProdutoChanged(value: ProdutoEntity?) {
        _uiState.update {
            it.copy(
                produtoId = value?.id,
                produtoNome = value?.nome,
                erroProduto = null
            )
        }
    }

    fun onBarrilChanged(value: BarrilEntity?) {
        _uiState.update {
            it.copy(
                barrilId = value?.id,
                barrilNome = value?.nome,
                erroBarril = null
            )
        }
    }

    fun inserirProducao(gradeId: String) {
        Log.d(TAG, "inserirProducao: Iniciando inserção de produção para gradeId: $gradeId")
        val currentState = _uiState.value

        Log.d(TAG, "inserirProducao: Antes da validação - State: $currentState")
        if (!validar(currentState)) return
        Log.d(TAG, "inserirProducao: depois da validação - State: ${_uiState.value}")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erroGeral = null) }

            val qtProduzida = currentState.quantidadeProduzida.toIntOrNull() ?: 0

            val qtProgramadaInt = currentState.quantidadeProgramada.toIntOrNull()
                ?: run {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            erroQuantidadeProgramada = "Quantidade programada inválida"
                        )
                    }
                    return@launch
                }

            if (qtProgramadaInt <= 0) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroQuantidadeProgramada = "Quantidade programada deve ser maior do que zero"
                    )
                }
                return@launch
            }

            val barrilId = currentState.barrilId ?: run {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroBarril = "Selecione um barril"
                    )
                }
                return@launch
            }

            val produtoId = currentState.produtoId ?: run {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroProduto = "Selecione um produto"
                    )
                }
                return@launch
            }

            val barrilResult = getOneBarrilUseCase(barrilId).first()

            val barril = barrilResult.getOrElse { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroGeral = error.message ?: "Erro ao buscar barril"
                    )
                }
                return@launch
            }

            val params = InsertProducaoParams(
                gradeId = gradeId,
                barrilId = barrilId,
                produtoId = produtoId,
                quantidadeProgramada = qtProgramadaInt,
                quantidadeProduzida = qtProduzida,
                volumeBarril = barril.volume
            )

            insertProducaoUseCase(params = params)
                .onSuccess {
                    Log.d(TAG, "inserirProducao: Adicionado com sucesso")
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { erro ->
                    Log.d(TAG, "inserirProducao: Erro ao adicionar produção: ${erro.message}")
                    _uiState.update { it.copy(isLoading = false, erroGeral = erro.message) }
                }

        }

    }

    private fun validar(state: AdicionarProducaoState): Boolean {
        var isValid = true
        var newState = state

        if (state.quantidadeProgramada.isBlank()) {
            isValid = false
            newState = newState.copy(erroQuantidadeProgramada = "Digite a quantidade programada")
        }

        if (state.produtoId.isNullOrEmpty()) {
            isValid = false
            newState = newState.copy(erroProduto = "Selecione um produto")
        }

        if (state.barrilId.isNullOrEmpty()) {
            isValid = false
            newState = newState.copy(erroBarril = "Selecione um barril")
        }

        _uiState.update { newState }
        return isValid;
    }

}