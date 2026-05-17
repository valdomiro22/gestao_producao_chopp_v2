package com.santos.valdomiro.gestaoproducaochopp.features.barril.presentation.screens.editarbarril

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.GetOneBarrilUseCase
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.UpdateBarrilParams
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.usecases.UpdateBarrilUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class EditarBarrilViewModel @Inject constructor(
    private val updateBarrilUseCase: UpdateBarrilUseCase,
    private val getOneBarrilUseCase: GetOneBarrilUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarBarrilState())
    val uiState = _uiState.asStateFlow()

    private var barrilBuscado: BarrilEntity? = null
    private var barrilIdAtual: String? = null

    fun buscarBarril(barrilId: String) {
        barrilIdAtual = barrilId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erroGeral = null) }

            getOneBarrilUseCase(barrilId).collect { result ->
                result.fold(
                    onSuccess = { barril ->
                        barrilBuscado = barril

                        _uiState.update {
                            it.copy(
                                nome = barril.nome,
                                volume = barril.volume.toString(),
                                descartavel = barril.descartavel,
                                isLoading = false,
                                erroGeral = null
                            )
                        }
                    },
                    onFailure = { erro ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                erroGeral = erro.message ?: "Erro ao buscar barril"
                            )
                        }
                    }
                )
            }
        }
    }

    fun editarBarril() {
        val currentState = _uiState.value
        val id = barrilIdAtual ?: return

        if (!validar(currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erroGeral = null) }

            val volumeInt = currentState.volume.toIntOrNull()
                ?: run {
                    _uiState.update {
                        it.copy(isLoading = false, erroVolume = "Volume inválido")
                    }
                    return@launch
                }

            if (volumeInt <= 0) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erroVolume = "Volume deve ser maior do que zero"
                    )
                }
                return@launch
            }

            val params = UpdateBarrilParams(
                id = id,
                nome = currentState.nome,
                volume = volumeInt,
                criadoEm = barrilBuscado?.criadoEm ?: Instant.now(),
                editadoEm = Instant.now(),
                statusSincronizacao = StatusSincronizacao.AGUARDANDO_ATUALIZACAO,
                descartavel = currentState.descartavel,
            )

            updateBarrilUseCase(params = params)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, erroGeral = null, isEditSuccess = true) }
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            erroGeral = erro.message ?: "Erro ao editar barril"
                        )
                    }
                }
        }
    }

    fun onNomeChanged(value: String) {
        _uiState.update { it.copy(nome = value, erroNome = null) }
    }

    fun onVolumeChanged(value: String) {
        val filtered = value.filter { it.isDigit() }
        _uiState.update { it.copy(volume = filtered, erroVolume = null) }
    }

    fun onDescartavelChanged(value: Boolean) {
        _uiState.update { it.copy(descartavel = value) }
    }


    private fun validar(state: EditarBarrilState): Boolean {
        var isValid = true
        var newState = state

        if (state.nome.isBlank()) {
            isValid = false
            newState = newState.copy(erroNome = "Digite o nome")
        }

        if (state.volume.isBlank()) {
            isValid = false
            newState = newState.copy(erroVolume = "Digite o volume")
        }

        _uiState.update { newState }
        return isValid;
    }

}