package com.dshovhenia.mvvm.compose.template.core.extension

import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeParseException

fun String.toLocalDateTime(): LocalDateTime? {
    return try {
        Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
    } catch (e: DateTimeParseException) {
        Timber.e(e)
        null
    }
}
