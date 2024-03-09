package com.dshovhenia.mvvm.compose.template.data.network

import com.dshovhenia.mvvm.compose.template.Constants
import com.dshovhenia.mvvm.compose.template.data.entity.CoinChartData
import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {
    @GET("coins/markets")
    suspend fun getCoinsMarkets(
    @Query("vs_currency") currency: String = Constants.API_PARAM_VALUE_USD,
    @Query("page") page: Int = 1,
    @Query("per_page") numCoinsPerPage: Int = 20,
    @Query("order") order: String = "market_cap_desc",
    @Query("sparkline") includeSparkline7dData: Boolean = false,
    @Query("price_change_percentage") priceChangePercentageIntervals: String = "",
    @Query("ids") coinIds: String? = null,
    ): List<CoinMarkets>

    @GET("coins/{id}/market_chart")
    suspend fun getCoinChart(
    @Path("id") coinId: String,
    @Query("vs_currency") currency: String = Constants.API_PARAM_VALUE_USD,
    @Query("days") days: String = "1",
    ): CoinChartData
}
