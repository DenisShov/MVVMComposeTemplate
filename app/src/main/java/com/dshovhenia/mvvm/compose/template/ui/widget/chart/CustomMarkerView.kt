package com.dshovhenia.mvvm.compose.template.ui.widget.chart

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.dshovhenia.mvvm.compose.template.R
import com.dshovhenia.mvvm.compose.template.core.extension.formatDate
import com.dshovhenia.mvvm.compose.template.core.extension.formatPrice
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerView(context: Context) : MarkerView(context, R.layout.custom_marker_view) {
    private var mOffset: MPPointF? = null

    private var priceTextView: TextView

    init {
        val priceLayout = LayoutInflater.from(context).inflate(R.layout.custom_marker_view, this)
        priceTextView = priceLayout.findViewById(R.id.tvPrice)
    }

    override fun refreshContent(
    e: Entry,
    highlight: Highlight,
    ) {
        val priceAtPoint = "${e.y.formatPrice(maximumFractionDigits = 2)}\n${e.x.formatDate()}"
        priceTextView.text = priceAtPoint
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF? {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF(-(width / DIVIDE_TWO).toFloat(), (DIVIDE_MINUS_TWO * height).toFloat())
        }
        return mOffset
    }

    companion object {
        const val DIVIDE_MINUS_TWO = -2
        const val DIVIDE_TWO = 2
    }
}
