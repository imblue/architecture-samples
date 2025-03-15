package com.example.android.architecture.blueprints.todoapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// This is a simple representation of a theme.
// Many more colors can be set here.
// There are different tools to help generate a theme, e.g.:
// https://material-foundation.github.io/material-theme-builder/

private val lightColorScheme = lightColorScheme(
    primary = Color(0xFF00658D),
    secondary = Color(0xFF4F616E),
    tertiary = Color(0xFF62597C),
    error = Color(0xFFBA1A1A)
)

private val darkColorScheme = darkColorScheme(
    primary = Color(0xFF82CFFF),
    secondary = Color(0xFFB6C9D8),
    tertiary = Color(0xFFCCC1E9),
    error = Color(0xFFFFB4AB)
)

/**
 * This sets the App-Theme based on the OS-level
 * settings for light/dark mode.
 *
 * Additional conditions based on app preferences
 * could be added here.
 *
 * Note:
 * Within UI-Code, any themed values should never be referenced directly.
 * Use [MaterialTheme.colorScheme] or [MaterialTheme.typography] instead.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    // Default typography and shapes can be adapted here.
    // If unset, system defaults for typography and material3
    // defaults for shapes are used.
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
