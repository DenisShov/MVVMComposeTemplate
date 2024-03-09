package com.dshovhenia.mvvm.compose.template.ui.widget.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class YAxisValueFormatter : ValueFormatter() {
    override fun getAxisLabel(
    value: Float,
    axis: AxisBase,
    ): String {
        return "$$value"
    }
}
