package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentPurple,
    onPrimary = Color.White,
    primaryContainer = SurfaceContainerHigh,
    onPrimaryContainer = TextPrimary,
    secondary = AccentCyan,
    onSecondary = Color(0xFF060E20),
    secondaryContainer = Color(0xFF5C3187),
    onSecondaryContainer = Color(0xFFD0A1FF),
    tertiary = AccentPink,
    onTertiary = Color(0xFF570066),
    tertiaryContainer = Color(0xFFC76ED3),
    onTertiaryContainer = Color(0xFF4C005A),
    background = BgDark,
    onBackground = TextPrimary,
    surface = BgDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceContainerHigh,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    outlineVariant = BorderOutline,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    error = ErrorColor
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
