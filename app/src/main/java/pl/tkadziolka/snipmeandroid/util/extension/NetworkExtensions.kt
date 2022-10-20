package pl.tkadziolka.snipmeandroid.util.extension

import retrofit2.HttpException

val HttpException.errorMessage: String? get() =
    try {
        response()?.errorBody()?.string()
    } catch (e: Exception) {
        null
    }
