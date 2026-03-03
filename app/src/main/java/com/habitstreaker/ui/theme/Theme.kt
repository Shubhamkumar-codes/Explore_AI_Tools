package com.habitstreaker.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NerdDarkScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFF39FF14),
    onPrimary = Color(0xFF001102),
    secondary = Color(0xFF7DF9FF),
    background = Color(0xFF090B12),
    surface = Color(0xFF151A2A),
    tertiary = Color(0xFFB967FF),
    onBackground = Color(0xFFE7F1FF),
    onSurface = Color(0xFFD9E8FF)
)

@Composable
fun HabitStreakerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NerdDarkScheme,
        typography = Typography,
        content = content
    )
}
