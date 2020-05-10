package io.viesure.hiring.datasource

sealed class Resource<out R> {

    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

}