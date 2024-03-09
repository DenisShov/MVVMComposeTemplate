package com.dshovhenia.mvvm.compose.template.domain.repository

import com.dshovhenia.mvvm.compose.template.data.entity.CoinChartData
import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.compose.template.data.store.GetCoinChartStore
import com.dshovhenia.mvvm.compose.template.data.store.GetCoinsMarketsStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinsMarketsRepository
@Inject
constructor(
    private val getCoinsMarketsStore: GetCoinsMarketsStore,
    private val getCoinChartStore: GetCoinChartStore,
) {
    suspend fun getCoinsMarkets(): List<CoinMarkets> {
        return getCoinsMarketsStore.getCoinsMarkets()
    }

    suspend fun getCoinChart(
    id: String,
    days: String,
    ): CoinChartData {
        return getCoinChartStore.getCoinChart(id, days)
    }
}
