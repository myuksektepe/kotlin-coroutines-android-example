package com.muratlakodla.kotlin_coroutines.model

sealed class Result<out R> {
    class Loading<T> : Result<T>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}