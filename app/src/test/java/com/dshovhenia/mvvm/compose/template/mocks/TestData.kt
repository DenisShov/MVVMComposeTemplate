package com.dshovhenia.mvvm.compose.template.mocks

import com.dshovhenia.mvvm.compose.template.data.entity.CoinChartData
import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import java.math.BigDecimal

val testResponseList =
    listOf(listOf(BigDecimal(1), BigDecimal(30000), BigDecimal(2), BigDecimal(60000)))

val testResponseListFiltered = listOf(BigDecimal(30000), BigDecimal(60000))

val testPriceChangePercentage = 100.0

val testCoinChartData = CoinChartData(testResponseList)

val testCoinMarkets = CoinMarkets("")

val testCoinMarketsList = listOf(testCoinMarkets)
