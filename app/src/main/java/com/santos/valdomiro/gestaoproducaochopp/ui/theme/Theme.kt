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

private val LightColorSchemeChopp = lightColorScheme(
    primary = PrimaryLightChopp,
    onPrimary = OnPrimaryLightChopp,

    primaryContainer = PrimaryContainerLightChopp,
    onPrimaryContainer = OnPrimaryContainerLightChopp,

    secondary = SecondaryLightChopp,
    onSecondary = OnSecondaryLightChopp,

    secondaryContainer = SecondaryContainerLightChopp,
    onSecondaryContainer = OnSecondaryContainerLightChopp,

    tertiary = TertiaryLightChopp,
    onTertiary = OnTertiaryLightChopp,

    tertiaryContainer = TertiaryContainerLightChopp,
    onTertiaryContainer = OnTertiaryContainerLightChopp,

    background = BackgroundLightChopp,
    onBackground = OnBackgroundLightChopp,

    surface = SurfaceLightChopp,
    onSurface = OnSurfaceLightChopp,

    surfaceVariant = SurfaceVariantLightChopp,
    onSurfaceVariant = OnSurfaceVariantLightChopp,

    outline = OutlineLightChopp,
    outlineVariant = OutlineVariantLightChopp,

    error = ErrorLightChopp,
    onError = OnErrorLightChopp
)

private val DarkColorSchemeChopp = darkColorScheme(
    primary = PrimaryDarkChopp,
    onPrimary = OnPrimaryDarkChopp,

    primaryContainer = PrimaryContainerDarkChopp,
    onPrimaryContainer = OnPrimaryContainerDarkChopp,

    secondary = SecondaryDarkChopp,
    onSecondary = OnSecondaryDarkChopp,

    secondaryContainer = SecondaryContainerDarkChopp,
    onSecondaryContainer = OnSecondaryContainerDarkChopp,

    tertiary = TertiaryDarkChopp,
    onTertiary = OnTertiaryDarkChopp,

    tertiaryContainer = TertiaryContainerDarkChopp,
    onTertiaryContainer = OnTertiaryContainerDarkChopp,

    background = BackgroundDarkChopp,
    onBackground = OnBackgroundDarkChopp,

    surface = SurfaceDarkChopp,
    onSurface = OnSurfaceDarkChopp,

    surfaceVariant = SurfaceVariantDarkChopp,
    onSurfaceVariant = OnSurfaceVariantDarkChopp,

    outline = OutlineDarkChopp,
    outlineVariant = OutlineVariantDarkChopp,

    error = ErrorDarkChopp,
    onError = OnErrorDarkChopp
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

        darkTheme -> DarkColorSchemeChopp
        else -> LightColorSchemeChopp
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}