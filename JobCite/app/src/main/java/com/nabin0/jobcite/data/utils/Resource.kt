package com.nabin0.jobcite.data.utils

sealed class Resource<out T>{
    data class Success<out T>(val result: T): Resource<T>()
    data class Failure<out T>(val exception: Exception): Resource<T>()
    object Loading: Resource<Nothing>()
}
