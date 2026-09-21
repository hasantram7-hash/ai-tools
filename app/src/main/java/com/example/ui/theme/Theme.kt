package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PremiumColorScheme = darkColorScheme(
    primary = AccentPink,
    onPrimary = Color.White,
    primaryContainer = SurfaceContainerHigh,
    onPrimaryContainer = TextPrimary,
    secondary = AccentRose,
    onSecondary = Color.White,
    secondaryContainer = SurfaceContainerHigh,
    onSecondaryContainer = TextPrimary,
    tertiary = AccentCyan,
    onTertiary = Color.White,
    tertiaryContainer = SurfaceContainerHigh,
    onTertiaryContainer = TextPrimary,
    background = BgDark,
    onBackground = TextPrimary,
    surface = BgDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceContainerLow,
    onSurfaceVariant = TextSecondary,
    outline = BorderOutline,
    outlineVariant = BorderOutline.copy(alpha = 0.5f),
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
        colorScheme = PremiumColorScheme,
        typography = Typography,
        content = content
    )
}
