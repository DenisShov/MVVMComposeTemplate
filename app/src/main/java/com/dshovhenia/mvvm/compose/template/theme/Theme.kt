package com.dshovhenia.mvvm.compose.template.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val StocksDarkColorScheme =
    darkColorScheme(
        background = StocksDarkBackground,
        onBackground = StocksDarkPrimaryText,
        surface = StocksDarkBackground,
        surfaceVariant = StocksDarkSelectedChip,
        onSurfaceVariant = StocksDarkPrimaryText,
        primary = StocksDarkBackground,
        secondary = StocksDarkBackground,
        tertiary = StocksDarkSelectedCard,
        onSurface = StocksDarkPrimaryText,
        onPrimaryContainer = StocksDarkPrimaryText,
        primaryContainer = StocksDarkSelectedCard,
        onSecondaryContainer = StocksDarkSecondaryText,
        onPrimary = StocksDarkSecondaryText,
    )

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = StocksDarkColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = StocksDarkColorScheme,
        typography = Typography,
        content = content,
    )
}
