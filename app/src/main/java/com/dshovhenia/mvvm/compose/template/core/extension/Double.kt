package com.dshovhenia.mvvm.compose.template.core.extension

import androidx.compose.ui.graphics.Color
import com.dshovhenia.mvvm.compose.template.theme.NegativeTrend
import com.dshovhenia.mvvm.compose.template.theme.PositiveTrend

fun Double.correspondingTrendColor(): Color {
    return if (this >= 0) PositiveTrend else NegativeTrend
}
