package com.dshovhenia.mvvm.compose.template.data.entity

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CoinChartData(@SerializedName("prices") val prices: List<List<BigDecimal>>)
