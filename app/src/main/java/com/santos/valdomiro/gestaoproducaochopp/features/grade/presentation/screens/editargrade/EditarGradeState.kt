package com.santos.valdomiro.gestaoproducaochopp.features.grade.presentation.screens.editargrade

import java.time.LocalDate

data class EditarGradeState(
    val numero: String = "",
    val data: LocalDate? = null,

    val erroNumero: String? = null,
    val erroData: String? = null,
    val erroGeral: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isEditSuccess: Boolean = false,
)
