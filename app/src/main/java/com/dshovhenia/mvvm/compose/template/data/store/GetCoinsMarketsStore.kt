package com.dshovhenia.mvvm.compose.template.data.store

import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import com.dshovhenia.mvvm.compose.template.data.network.CoinGeckoApi
import javax.inject.Inject

class GetCoinsMarketsStore
@Inject
constructor(private val coinGeckoApi: CoinGeckoApi) {
    suspend fun getCoinsMarkets(): List<CoinMarkets> {
        return coinGeckoApi.getCoinsMarkets()
    }
}
