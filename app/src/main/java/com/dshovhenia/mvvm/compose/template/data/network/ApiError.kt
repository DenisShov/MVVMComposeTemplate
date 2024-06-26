package com.dshovhenia.mvvm.compose.template.data.network

import java.io.IOException

data class ApiError(
    val errorId: String,
    val message: String,
)

class ApiException(val error: ApiError? = null) : IOException()
