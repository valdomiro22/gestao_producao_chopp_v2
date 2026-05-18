package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.editarproducao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.GetOneBarrilUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.GetOneProducaoUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.UpdateProducaoParams
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.usecase.UpdateProducaoUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.entity.ProdutoEntity
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.usecase.GetOneProdutoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarProducaoViewModel @Inject constructor(
    private val updateProducaoUseCase: UpdateProducaoUseCase,
    private val getOneBarrilUseCase: GetOneBarrilUseCase,
    private val getOneProducaoUseCase: GetOneProducaoUseCase,
    private val getOneProdutoUseCase: GetOneProdutoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarProducaoState())
    val uiState = _uiState.asStateFlow()

    private var producaoBuscada: ProducaoEntity? = null
    private var producaoIdAtual: String? = null

    fun buscarProducao(producaoId: String) {
        producaoIdAtual = producaoId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erroGeral = null) }

            getOneProducaoUseCase(producaoId = producaoId).collect { result ->
                result.fold(
                    onSuccess = { producao ->
                        producaoBuscada = producao

                        val barrilResult = getOneBarrilUseCase(producao.barrilId).first()
                        val barril = barrilResult.getOrElse { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    erroGeral = error.message ?: "Erro ao buscar barril"
                                )
                            }
                            return@collect
                        }

                        val produtoResult = getOneProdutoUseCase(producao.produtoId).first()
                        val produto = produtoResult.getOrElse { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    erroGeral = error.message ?: "Erro ao buscar produto"
                                )
                            }
                            return@collect
                        }

                        _uiState.update {
                            it.copy(
                                quantidadeProgramada = producao.quantidadeProgramada.toString(),
                                quantidadeProduzida = producao.quantidadeProduzida.toString(),
                                barrilId = producao.barrilId,
                                produtoId = producao.produtoId,
                                produtoNome = produto.nome,
                                barrilNome = barril.nome,
                                isLoading = false,
                                erroGeral = null
                            )
                        }
                    },
                    onFailure = { erro ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                erroGeral = erro.message ?: "Erro ao buscar produção"
                            )
                        }
                    }
                )
            }
        }
    }

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

    fun onProdutoChanged(produto: ProdutoEntity?) {
        _uiState.update {
            it.copy(
                produtoId = produto?.id,
                produtoNome = produto?.nome,
                erroProduto = null
            )
        }
    }

    fun onBarrilChanged(barril: BarrilEntity?) {
        _uiState.update {
            it.copy(
                barrilId = barril?.id,
                barrilNome = barril?.nome,
                erroBarril = null
            )
        }
    }

    fun atualizarProducao(producaoId: String) {
        val currentState = _uiState.value

        if (!validar(currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erroGeral = null) }

            val producaoAtual = producaoBuscada ?: run {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroGeral = "Produção ainda não foi carregada"
                    )
                }
                return@launch
            }

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

            val params = UpdateProducaoParams(
                id = producaoId,
                gradeId = producaoAtual.gradeId,
                barrilId = barrilId,
                produtoId = produtoId,
                quantidadeProgramada = qtProgramadaInt,
                quantidadeProduzida = qtProduzida,
                volumeBarril = barril.volume,
                criadoEm = producaoAtual.criadoEm,
                status = producaoAtual.status,
                dataFimDeProducao = producaoAtual.dataFimDeProducao
            )

            updateProducaoUseCase(params = params)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            isEditSuccess = true
                        )
                    }
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            erroGeral = erro.message ?: "Erro ao atualizar produção"
                        )
                    }
                }
        }
    }

    private fun validar(state: EditarProducaoState): Boolean {
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