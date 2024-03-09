package com.dshovhenia.mvvm.compose.template.data.store

import com.dshovhenia.mvvm.compose.template.data.entity.CoinChartData
import com.dshovhenia.mvvm.compose.template.data.network.CoinGeckoApi
import javax.inject.Inject

class GetCoinChartStore
@Inject
constructor(private val coinGeckoApi: CoinGeckoApi) {
    suspend fun getCoinChart(
    id: String,
    days: String,
    ): CoinChartData {
        return coinGeckoApi.getCoinChart(coinId = id, days = days)
    }
}
