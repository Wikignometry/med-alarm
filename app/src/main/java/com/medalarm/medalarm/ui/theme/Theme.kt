package com.medalarm.medalarm.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext

@Composable
fun ColorScheme.isLight() = this.background.luminance() > 0.5

// Converted UI theme from previous default (should be changed to red or something nice :)
private val LightColorScheme = lightColorScheme(
    primary = Purple200,
    secondary = Purple700,
    tertiary = Teal200,
    // error, primaryContainer, onSecondary, etc.
)
private val DarkColorScheme = darkColorScheme(
    primary = Purple500,
    secondary = Purple700,
    tertiary = Teal200,
    // error, primaryContainer, onSecondary, etc.
)


@Composable
fun MedAlarmTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}