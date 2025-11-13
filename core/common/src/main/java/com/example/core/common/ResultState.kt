package com.example.core.common

sealed interface ResultState<out T> {
    data object Loading : ResultState<Nothing>
    data object Empty : ResultState<Nothing>
    data class Success<T>(val data: T) : ResultState<T>
    data class Error(val message: String?, val cause: Throwable? = null) : ResultState<Nothing>

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(message: String?, cause: Throwable? = null) = Error(message, cause)
        fun loading() = Loading
    }
}
