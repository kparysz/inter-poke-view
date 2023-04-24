package com.inter.poke.view.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
private fun DarkColorScheme() = AppTheme.colors.copy(
    primary = Color.Green,
    secondary = Color.Black,
    error = Color.Red,
    background = Color.Gray,
    onBackground = Color.White,
)

@Composable
private fun LightColorScheme() = AppTheme.colors.copy(
    primary = Color.Green,
    secondary = Color.Yellow,
    error = Color.Red,
    background = Color.White,
    onBackground = Color.Gray,
)

@Composable
fun InterPokeViewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme()
        else -> LightColorScheme()
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    ProvideAppColors(colors = colors) {
        MaterialTheme(
            colors = colorScheme,
            typography = PokeTypography,
            content = content
        )
    }
}

@Composable
fun ProvideAppColors(
    colors: Colors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors }
    CompositionLocalProvider(LocalAppColors provides colorPalette, content = content)
}

private val LightThemeColors = lightColors()

private val LocalAppColors = staticCompositionLocalOf {
    LightThemeColors
}

object AppTheme {
    val colors: Colors
        @Composable
        get() = LocalAppColors.current
}