package com.dshovhenia.mvvm.compose.template.ui.widget.chart

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.dshovhenia.mvvm.compose.template.core.extension.formatPrice
import com.dshovhenia.mvvm.compose.template.theme.StocksDarkPrimaryText
import timber.log.Timber
import java.math.BigDecimal

@Composable
fun CryptoChart(
modifier: Modifier = Modifier,
data: List<BigDecimal>,
graphColor: androidx.compose.ui.graphics.Color,
showDashedLine: Boolean,
) {
    Timber.d("CryptoChart recomposition")

    if (data.isEmpty()) {
        Timber.i("CryptoChart invoked with empty data list.")
        return
    }

    val spacing = 0f
    val transparentGraphColor =
    remember(key1 = graphColor) {
        graphColor.copy(alpha = 0.5f)
    }

    val (lowerValue, upperValue) =
    remember(key1 = data) {
        Pair(data.minBy { it }, data.maxBy { it })
    }

    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing) / data.size

        var lastX = 0f
        var firstY = 0f
        val strokePath =
        Path().apply {
            val height = size.height
            for (i in data.indices) {
                val info = data[i]
                val nextInfo = data.getOrNull(i + 1) ?: data.last()
                val leftRatio = (info - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio.toFloat() * height)

                if (i == 0) {
                    firstY = y1
                }

                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio.toFloat() * height)
                if (i == 0) {
                    moveTo(x1, y1)
                }
                lastX = (x1 + x2) / 2f
                quadraticBezierTo(
                    x1, y1, lastX, (y1 + y2) / 2f,
                )
            }
        }

        val fillPath =
        android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(lastX, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        drawPath(
            path = fillPath,
            brush =
            Brush.verticalGradient(
                colors =
                listOf(
                    transparentGraphColor, androidx.compose.ui.graphics.Color.Transparent,
                ),
                endY = size.height - spacing,
            ),
            )

        drawPath(
            path = strokePath, color = graphColor,
            style =
            Stroke(
                width = 2.dp.toPx(), cap = StrokeCap.Round,
            ),
        )

        if (showDashedLine) {
            val dottedPath =
            Path().apply {
                moveTo(0f, firstY)
                lineTo(lastX, firstY)
            }

            drawPath(
                path = dottedPath, color = graphColor.copy(alpha = .8f),
                style =
                Stroke(
                    width = 1.5.dp.toPx(),
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 20f), 0f),
                ),
            )
        }

        val textPaint =
        Paint().apply {
            color = StocksDarkPrimaryText.toArgb()
            textAlign = Paint.Align.RIGHT
            textSize = density.run { 12.dp.toPx() }
            typeface = setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
            alpha = 192
        }

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "MAX ${upperValue.formatPrice(maximumFractionDigits = 0)}",
                size.width - 16.dp.toPx(),
                0 + 8.dp.toPx(),
                textPaint,
            )
            drawText(
                "MIN ${lowerValue.formatPrice(maximumFractionDigits = 0)}",
                size.width - 16.dp.toPx(),
                size.height - 4.dp.toPx(),
                textPaint,
            )
        }
    }
}
