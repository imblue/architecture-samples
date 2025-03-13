package com.example.android.architecture.blueprints.todoapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
