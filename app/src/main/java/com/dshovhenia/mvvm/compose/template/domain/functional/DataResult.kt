package com.dshovhenia.mvvm.compose.template.domain.functional

import com.dshovhenia.mvvm.compose.template.data.network.ApiException
import com.dshovhenia.mvvm.compose.template.domain.error.AppError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.net.ConnectException
import java.net.UnknownHostException

sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>

    data class Failure(val error: AppError) : DataResult<Nothing>
}

fun <T> DataResult<T>.fold(
onSuccess: (T) -> Unit,
onFailure: (AppError) -> Unit,
) {
    when (this) {
        is DataResult.Success -> onSuccess(data)
        is DataResult.Failure -> onFailure(error)
    }
}

fun <T> Flow<T>.asDataResult(): Flow<DataResult<T>> {
    return this
        .map<T, DataResult<T>> {
            DataResult.Success(it)
        }
        .catch { emit(DataResult.Failure(it.toError)) }
}

val Throwable.toError: AppError
    get() =
        when (this) {
            is UnknownHostException -> AppError.MissingNetworkConnection
            is ConnectException -> AppError.MissingNetworkConnection
            is ApiException -> AppError.ApiError(this)
            else -> AppError.GeneralError(this)
        }
