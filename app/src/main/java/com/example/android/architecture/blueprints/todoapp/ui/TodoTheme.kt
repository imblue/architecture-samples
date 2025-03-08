package com.example.android.architecture.blueprints.todoapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TodoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}

private val primaryLight = Color(0xFF3F6836)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFC0EFB0)
private val onPrimaryContainerLight = Color(0xFF285021)
private val secondaryLight = Color(0xFF53634E)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFD7E8CD)
private val onSecondaryContainerLight = Color(0xFF3C4B37)
private val tertiaryLight = Color(0xFF386569)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFBCEBEF)
private val onTertiaryContainerLight = Color(0xFF1E4D51)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF93000A)
private val backgroundLight = Color(0xFFF8FBF1)
private val onBackgroundLight = Color(0xFF191D17)
private val surfaceLight = Color(0xFFF8FBF1)
private val onSurfaceLight = Color(0xFF191D17)
private val surfaceVariantLight = Color(0xFFDFE4D8)
private val onSurfaceVariantLight = Color(0xFF43483F)
private val outlineLight = Color(0xFF73796E)
private val outlineVariantLight = Color(0xFFC3C8BC)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF2E322B)
private val inverseOnSurfaceLight = Color(0xFFEFF2E8)
private val inversePrimaryLight = Color(0xFFA4D396)
private val surfaceDimLight = Color(0xFFD8DBD2)
private val surfaceBrightLight = Color(0xFFF8FBF1)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFF2F5EB)
private val surfaceContainerLight = Color(0xFFECEFE5)
private val surfaceContainerHighLight = Color(0xFFE6E9E0)
private val surfaceContainerHighestLight = Color(0xFFE1E4DA)

private val primaryDark = Color(0xFFA4D396)
private val onPrimaryDark = Color(0xFF10380C)
private val primaryContainerDark = Color(0xFF285021)
private val onPrimaryContainerDark = Color(0xFFC0EFB0)
private val secondaryDark = Color(0xFFBBCBB2)
private val onSecondaryDark = Color(0xFF263422)
private val secondaryContainerDark = Color(0xFF3C4B37)
private val onSecondaryContainerDark = Color(0xFFD7E8CD)
private val tertiaryDark = Color(0xFFA0CFD2)
private val onTertiaryDark = Color(0xFF00373A)
private val tertiaryContainerDark = Color(0xFF1E4D51)
private val onTertiaryContainerDark = Color(0xFFBCEBEF)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF11140F)
private val onBackgroundDark = Color(0xFFE1E4DA)
private val surfaceDark = Color(0xFF11140F)
private val onSurfaceDark = Color(0xFFE1E4DA)
private val surfaceVariantDark = Color(0xFF43483F)
private val onSurfaceVariantDark = Color(0xFFC3C8BC)
private val outlineDark = Color(0xFF8D9387)
private val outlineVariantDark = Color(0xFF43483F)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFE1E4DA)
private val inverseOnSurfaceDark = Color(0xFF2E322B)
private val inversePrimaryDark = Color(0xFF3F6836)
private val surfaceDimDark = Color(0xFF11140F)
private val surfaceBrightDark = Color(0xFF363A34)
private val surfaceContainerLowestDark = Color(0xFF0B0F0A)
private val surfaceContainerLowDark = Color(0xFF191D17)
private val surfaceContainerDark = Color(0xFF1D211B)
private val surfaceContainerHighDark = Color(0xFF272B25)
private val surfaceContainerHighestDark = Color(0xFF32362F)

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)
