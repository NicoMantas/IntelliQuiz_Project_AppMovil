package com.upb.intelliquiz.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ButtonPurple,
    secondary = ButtonCircleDark,
    tertiary = TitleWhite,
    background = BackgroundDark,
    surface = BackgroundDark,
    onPrimary = TitleWhite,
    onSecondary = TitleWhite,
    onBackground = TextGray,
    onSurface = TextGray
)

@Composable
fun IntelliQuizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}