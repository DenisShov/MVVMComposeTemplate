package com.dshovhenia.mvvm.compose.template.core.extension

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Number.formatPrice(
withoutSymbol: Boolean = false,
maximumFractionDigits: Int = 8,
): String {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.currency = Currency.getInstance(Locale.US)
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = maximumFractionDigits
    val price = numberFormat.format(this)
    return if (withoutSymbol) {
        price.replace("$", "")
    } else {
        price
    }
}

fun Number.formatToPercentage(): String {
    val numberFormat =
    NumberFormat.getPercentInstance(Locale.US).apply {
        maximumFractionDigits = 3
        minimumFractionDigits = 2
    }

    val valueToFormat: Double = toDouble() / 100
    var formattedValue: String = numberFormat.format(valueToFormat)

    formattedValue =
    if (formattedValue.startsWith("+") || formattedValue.startsWith("-")) {
        formattedValue
    } else {
        val sign = if (valueToFormat >= 0) "+" else "-"
        sign.plus(formattedValue)
    }

    return formattedValue
}
