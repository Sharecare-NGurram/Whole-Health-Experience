package com.carelon.whe.network

import com.carelon.whe.domain.model.ErrorItem

sealed class Resource<out R> private constructor() {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: ErrorItem) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}