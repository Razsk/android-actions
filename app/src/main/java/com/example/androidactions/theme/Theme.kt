package com.example.androidactions.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val KineticHudDarkColorScheme = darkColorScheme(
    primary = ActionBlue,
    onPrimary = OnActionBlue,
    primaryContainer = ActionBlueContainer,
    onPrimaryContainer = OnActionBlueContainer,
    secondary = ActionBlue,
    onSecondary = OnActionBlue,
    tertiary = CyberCyan,
    onTertiary = OnCyberCyan,
    tertiaryContainer = CyberCyanContainer,
    onTertiaryContainer = OnCyberCyanContainer,
    background = SurfaceDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = OnSurfaceVariantDark,
    surfaceContainer = SurfaceContainer,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    error = ErrorDark,
    onErrorContainer = ErrorContainerDark
)

@Composable
fun AndroidActionsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KineticHudDarkColorScheme,
        typography = Typography,
        content = content
    )
}
