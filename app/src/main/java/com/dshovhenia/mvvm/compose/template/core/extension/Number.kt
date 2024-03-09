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
