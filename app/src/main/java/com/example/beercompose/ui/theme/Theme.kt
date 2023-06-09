package com.example.beercompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.beercompose.util.AppGradient
import com.example.beercompose.util.LocalAppGradient
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = DarkThemeDark,
    primaryVariant = DarkThemeLight,
    secondary = Accent,
    onPrimary = Color.White,
    surface = DarkThemeLight,
    onSurface = Color.White,
    onBackground = Color.Black
)

private val LightColorPalette = lightColors(
    primary = AmberDark,
    primaryVariant = AmberLight,
    secondary = DarkThemeAccent,
    onPrimary = Color.Black,
    surface = AmberLight,
    onSurface = Color.Black,
    onBackground = Color.White
)


@Composable
fun BeerComposeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val appGradient = if (darkTheme) {
        AppGradient(Brush.verticalGradient(listOf(DarkThemeDark, DarkThemeLight)))
    } else {
        AppGradient(Brush.verticalGradient(listOf(AmberDark, AmberLight)))
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes
    ) {
        CompositionLocalProvider(LocalAppGradient provides appGradient, content = content)
    }
    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        systemUiController.setStatusBarColor(
            color = DarkThemeDark
        )
    } else {
        systemUiController.setStatusBarColor(
            color = AmberDark
        )
    }
}