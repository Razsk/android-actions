package com.example.androidactions.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val KineticHudColorScheme = darkColorScheme(
    primary = KineticHudPrimary,
    primaryContainer = KineticHudPrimaryContainer,
    secondary = KineticHudSecondary,
    secondaryContainer = KineticHudSecondaryContainer,
    tertiary = KineticHudTertiaryCyan,
    background = KineticHudBackground,
    surface = KineticHudSurface,
    surfaceVariant = KineticHudSurfaceVariant,
    error = KineticHudError
)

@Composable
fun AndroidActionsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KineticHudColorScheme,
        content = content
    )
}
