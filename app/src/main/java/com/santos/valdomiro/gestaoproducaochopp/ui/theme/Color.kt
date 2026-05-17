package com.santos.valdomiro.gestaoproducaochopp.ui.theme

import androidx.compose.ui.graphics.Color

// Base da identidade visual (Blue Lagoon)
val LagoonDarkest = Color(0xFF0D1F23)
val LagoonDark = Color(0xFF132E35)
val LagoonMediumDark = Color(0xFF2D4A53)
val LagoonMedium = Color(0xFF69818D)
val LagoonLight = Color(0xFFAFB3B7)
val LagoonSlate = Color(0xFF5A636A)

// Novas cores neutras derivadas para fundos e superfícies
val BackgroundBlueLight = Color(0xFFF4F6F8) // Off-white frio
val SurfaceBlueLight = Color(0xFFFFFFFF)
val SurfaceVariantBlueLight = Color(0xFFDDE2E5)

val BackgroundBlueDark = Color(0xFF080D0F) // Mais escuro que o LagoonDarkest para profundidade
val SurfaceBlueDark = LagoonDarkest
val SurfaceVariantBlueDark = LagoonDark

// TEMA CLARO - Sofisticado e limpo, com azuis escuros em destaque
val PrimaryLightSoft = LagoonDark
val OnPrimaryLightSoft = Color.White

val PrimaryContainerLightSoft = LagoonMedium
val OnPrimaryContainerLightSoft = Color.White

val SecondaryLightSoft = LagoonMediumDark
val OnSecondaryLightSoft = Color.White

val SecondaryContainerLightSoft = LagoonLight
val OnSecondaryContainerLightSoft = LagoonDarkest

val TertiaryLightSoft = LagoonSlate
val OnTertiaryLightSoft = Color.White

val BackgroundLightSoft = BackgroundBlueLight
val OnBackgroundLightSoft = LagoonDarkest

val SurfaceLightSoft = SurfaceBlueLight
val OnSurfaceLightSoft = LagoonDarkest

val SurfaceVariantLightSoft = SurfaceVariantBlueLight
val OnSurfaceVariantLightSoft = LagoonMediumDark

val OutlineLightSoft = LagoonMedium
val OutlineVariantLightSoft = LagoonLight

val ErrorLightSoft = Color(0xFFBA1A1A)
val OnErrorLightSoft = Color.White

// TEMA ESCURO - Profundo e moderno, priorizando tons claros para leitura
val PrimaryDarkSoft = LagoonLight
val OnPrimaryDarkSoft = LagoonDarkest

val PrimaryContainerDarkSoft = LagoonMediumDark
val OnPrimaryContainerDarkSoft = LagoonLight

val SecondaryDarkSoft = LagoonMedium
val OnSecondaryDarkSoft = LagoonDarkest

val SecondaryContainerDarkSoft = LagoonDark
val OnSecondaryContainerDarkSoft = LagoonLight

val TertiaryDarkSoft = LagoonLight
val OnTertiaryDarkSoft = LagoonSlate

val BackgroundDarkSoft = BackgroundBlueDark
val OnBackgroundDarkSoft = Color(0xFFE2E7EA)

val SurfaceDarkSoft = SurfaceBlueDark
val OnSurfaceDarkSoft = Color(0xFFE2E7EA)

val SurfaceVariantDarkSoft = SurfaceVariantBlueDark
val OnSurfaceVariantDarkSoft = LagoonLight

val OutlineDarkSoft = LagoonMedium
val OutlineVariantDarkSoft = LagoonMediumDark

val ErrorDarkSoft = Color(0xFFFFB4AB)
val OnErrorDarkSoft = Color(0xFF690005)