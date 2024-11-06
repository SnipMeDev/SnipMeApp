package dev.snipme.snipmeapp.util.extension

import retrofit2.HttpException

val HttpException.errorMessage: String? get() =
    try {
        response()?.errorBody()?.string()
    } catch (e: Exception) {
        null
    }
