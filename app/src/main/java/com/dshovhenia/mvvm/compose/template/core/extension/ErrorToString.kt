package com.dshovhenia.mvvm.compose.template.core.extension

import com.dshovhenia.mvvm.compose.template.domain.error.AppError

fun AppError.toErrorMessage(): String {
    return when (this) {
        is AppError.MissingNetworkConnection -> "⚠️ You aren’t connected to the internet."
        is AppError.ApiError -> "⚠️ The CoinGecko service is now at capacity. Please try again in a minute."
        else -> "⚠️ Oops! Something went wrong."
    }
}
