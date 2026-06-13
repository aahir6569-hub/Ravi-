package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LuxuryDarkColorScheme = darkColorScheme(
    primary = BurnishedGold,
    secondary = PolishedPlatinum,
    tertiary = GoldenChalice,
    background = MatteCharcoalBlack,
    surface = WarmCharcoalOnyx,
    onPrimary = MatteCharcoalBlack,
    onSecondary = MatteCharcoalBlack,
    onBackground = PureMutedWhite,
    onSurface = PureMutedWhite,
    surfaceVariant = LightSlateCharcoal,
    onSurfaceVariant = BrushedTitanium
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // We enforce our luxurious Matte Charcoal and Gold theme universally
    // to match the prestige interior mood.
    MaterialTheme(
        colorScheme = LuxuryDarkColorScheme,
        typography = Typography,
        content = content
    )
}
