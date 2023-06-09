package com.example.beercompose.util

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush

data class AppGradient(
    val gradient: Brush
)

val LocalAppGradient = staticCompositionLocalOf<AppGradient> {
    error("CompositionLocal LocalAppGradient not present")
}
