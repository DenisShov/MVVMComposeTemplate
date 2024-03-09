package com.dshovhenia.mvvm.compose.template.domain.error

import com.dshovhenia.mvvm.compose.template.data.network.ApiException

sealed class AppError {
    object MissingNetworkConnection : AppError()

    data class ApiError(val exception: ApiException) : AppError()

    data class GeneralError(val exception: Throwable) : AppError()
}
