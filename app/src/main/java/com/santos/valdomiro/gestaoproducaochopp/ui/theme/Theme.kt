package com.santos.valdomiro.gestaoproducaochopp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorSchemeSoft = lightColorScheme(
    primary = PrimaryLightSoft,
    onPrimary = OnPrimaryLightSoft,

    primaryContainer = PrimaryContainerLightSoft,
    onPrimaryContainer = OnPrimaryContainerLightSoft,

    secondary = SecondaryLightSoft,
    onSecondary = OnSecondaryLightSoft,

    secondaryContainer = SecondaryContainerLightSoft,
    onSecondaryContainer = OnSecondaryContainerLightSoft,

    tertiary = TertiaryLightSoft,
    onTertiary = OnTertiaryLightSoft,

    background = BackgroundLightSoft,
    onBackground = OnBackgroundLightSoft,

    surface = SurfaceLightSoft,
    onSurface = OnSurfaceLightSoft,

    surfaceVariant = SurfaceVariantLightSoft,
    onSurfaceVariant = OnSurfaceVariantLightSoft,

    outline = OutlineLightSoft,
    outlineVariant = OutlineVariantLightSoft,

    error = ErrorLightSoft,
    onError = OnErrorLightSoft
)

private val DarkColorSchemeSoft = darkColorScheme(
    primary = PrimaryDarkSoft,
    onPrimary = OnPrimaryDarkSoft,

    primaryContainer = PrimaryContainerDarkSoft,
    onPrimaryContainer = OnPrimaryContainerDarkSoft,

    secondary = SecondaryDarkSoft,
    onSecondary = OnSecondaryDarkSoft,

    secondaryContainer = SecondaryContainerDarkSoft,
    onSecondaryContainer = OnSecondaryContainerDarkSoft,

    tertiary = TertiaryDarkSoft,
    onTertiary = OnTertiaryDarkSoft,

    background = BackgroundDarkSoft,
    onBackground = OnBackgroundDarkSoft,

    surface = SurfaceDarkSoft,
    onSurface = OnSurfaceDarkSoft,

    surfaceVariant = SurfaceVariantDarkSoft,
    onSurfaceVariant = OnSurfaceVariantDarkSoft,

    outline = OutlineDarkSoft,
    outlineVariant = OutlineVariantDarkSoft,

    error = ErrorDarkSoft,
    onError = OnErrorDarkSoft
)

@Composable
fun GestaoProducaoChoppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorSchemeSoft
        else -> LightColorSchemeSoft
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}