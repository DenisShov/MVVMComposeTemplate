package com.dshovhenia.mvvm.compose.template.ui.widget.chart

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.dshovhenia.mvvm.compose.template.Constants
import com.dshovhenia.mvvm.compose.template.R
import com.dshovhenia.mvvm.compose.template.theme.ChartColor
import com.dshovhenia.mvvm.compose.template.theme.ChartFillColor
import com.dshovhenia.mvvm.compose.template.theme.ChartViewHighLightColor
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import java.math.BigDecimal

@Composable
fun CryptoChart(
modifier: Modifier = Modifier,
chartData: List<List<BigDecimal>>,
) {
    val currentChartData by rememberUpdatedState(newValue = chartData)

    AndroidView(
        modifier = modifier,
        factory = { context ->
            val lineChart = LineChart(context)
            lineChart.apply {
                setViewPortOffsets(0f, 0f, 0f, 0f)

                setNoDataText(context.getString(R.string.building_chart))
                // description text
                description.text = context.getString(R.string.price_usd_vs_time)
                description.textSize = Constants.DESCRIPTION_TEXT_SIZE
                description.textColor = Color.WHITE
                description.isEnabled = true

                // enable touch gestures
                setTouchEnabled(true)

                // enable scaling and dragging
                isDragEnabled = false
                setScaleEnabled(true)

                // if disabled, scaling can be done on x- and y-axis separately
                setPinchZoom(true)

                setDrawGridBackground(false)
                maxHighlightDistance = Constants.MAX_HIGHLIGHT_DISTANCE

                val x = xAxis
                x.textColor = Color.WHITE
                x.axisLineColor = Color.WHITE
                x.position = XAxis.XAxisPosition.BOTTOM
                x.setDrawLabels(false)
                x.setDrawGridLines(false)

                val y = axisLeft
                y.setLabelCount(Constants.LABEL_COUNTS, false)
                y.setDrawAxisLine(true)
                y.textColor = Color.WHITE
                y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
                y.setDrawGridLines(false)
                y.valueFormatter = YAxisValueFormatter()
                y.axisLineColor = Color.WHITE
                y.setDrawZeroLine(false)
                y.setDrawLimitLinesBehindData(false)

                axisRight.isEnabled = false
                legend.isEnabled = false
                animateX(Constants.X_ANIMATION_DURATION)

                // set marker
                val customMarker = CustomMarkerView(context)
                marker = customMarker
            }

            lineChart
        },
        update = { lineChart ->

            lineChart.apply {
                val values = ArrayList<Entry>()
                for (i in currentChartData.indices) {
                    val timestamp = currentChartData[i][0].toFloat()
                    val price = currentChartData[i][1].toFloat()

                    values.add(Entry(timestamp, price))
                }
                val set: LineDataSet
                if (data != null && data.dataSetCount > 0) {
                    set = data.getDataSetByIndex(0) as LineDataSet
                    set.values = values
                    data.notifyDataChanged()
                    notifyDataSetChanged()
                } else {
                    // create a data set and give it a type
                    set = LineDataSet(values, "DataSet")
                    set.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set.cubicIntensity = Constants.CUBIC_INTENSITY
                    set.setDrawFilled(true)
                    set.setDrawCircles(false)
                    set.lineWidth = Constants.LINE_WIDTH
                    set.circleRadius = Constants.CIRCLE_RADIUS
                    set.setCircleColor(Color.BLUE)
                    set.highLightColor = ChartViewHighLightColor
                    set.color = ChartColor.toArgb()
                    set.fillColor = ChartFillColor.toArgb()
                    set.setDrawHorizontalHighlightIndicator(true)
                    set.fillFormatter = IFillFormatter { _, _ -> axisLeft.axisMinimum }

                    // create a data object with the data sets
                    val lineData = LineData(set)
                    lineData.setValueTextSize(Constants.LINE_DATA_TEXT_SIZE)
                    lineData.setDrawValues(false)

                    data = lineData
                }
                invalidate()
            }
        },
    )
}
