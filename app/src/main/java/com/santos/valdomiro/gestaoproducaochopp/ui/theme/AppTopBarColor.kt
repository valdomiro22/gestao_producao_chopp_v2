package com.santos.valdomiro.gestaoproducaochopp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppTopBarColors {
    @Composable
    fun titleColor(): Color {
        return if (isSystemInDarkTheme()) {
            MaterialTheme.colorScheme.onBackground
        } else {
            MaterialTheme.colorScheme.primary
        }
    }
}