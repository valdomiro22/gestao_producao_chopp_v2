package com.santos.valdomiro.gestaoproducaochopp.features.producao.presentation.screens.simularfimproducao

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.entity.ProducaoEntity

data class SimularFimProducaoState(
    val qtProgramada: String = "",
    val qtProduzida: String = "",
    val nivelMaxTanque: String = "",
    val vlNecessario: String? = null,

    val producao: ProducaoEntity? = null,
    val barril: BarrilEntity? = null,

    val erroQtProgramada: String? = null,
    val erroQtProduzida: String? = null,
    val erroNivelMaxTanque: String? = null,
    val erro: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)