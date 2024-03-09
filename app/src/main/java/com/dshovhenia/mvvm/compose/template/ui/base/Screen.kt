package com.dshovhenia.mvvm.compose.template.ui.base

import android.os.Parcelable
import com.dshovhenia.mvvm.compose.template.data.entity.CoinMarkets
import kotlinx.parcelize.Parcelize

sealed class Screen : Parcelable {
    @Parcelize
    object CoinsList : Screen()

    @Parcelize
    data class CoinDetail(val coinMarkets: CoinMarkets) : Screen()
}
