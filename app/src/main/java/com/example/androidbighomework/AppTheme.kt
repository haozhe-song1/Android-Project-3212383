package com.example.androidbighomework

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Simple colors used by every screen.
object AppColors {
    val Green = Color(0xFF315944)
    val LightGreen = Color(0xFF457D66)
    val Cream = Color(0xFFFBF9F5)
    val Orange = Color(0xFFE8913A)
    val StarYellow = Color(0xFFF5A623)
    val GrayText = Color(0xFF7A7A7A)
}

private val DrinkColorScheme = lightColorScheme(
    primary = AppColors.Green,
    onPrimary = Color.White,
    background = AppColors.Cream,
    onBackground = AppColors.Green,
    surface = Color.White,
    onSurface = AppColors.Green
)

@Composable
fun DrinkGuideTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DrinkColorScheme,
        content = content
    )
}
