package com.merveylcu.contract

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

sealed class RestResult<out T> {

    class Success<out T>(
        val result: T
    ) : RestResult<T>()

    class Error(
        val error: INetworkError
    ) : RestResult<Nothing>()

    class Loading(val loading: Boolean) : RestResult<Nothing>()
}

fun <T> Flow<RestResult<T>>.onSuccess(action: suspend (T) -> Unit): Flow<RestResult<T>> {
    return transform { restResult ->
        if (restResult is RestResult.Success) {
            action.invoke(restResult.result)
        }
        emit(restResult)
    }
}

fun <T> Flow<RestResult<T>>.onError(action: suspend (INetworkError) -> Unit): Flow<RestResult<T>> {
    return transform { restResult ->
        if (restResult is RestResult.Error) {
            action.invoke(restResult.error)
        }
        emit(restResult)
    }
}

fun <T> Flow<RestResult<T>>.onLoading(action: suspend (Boolean) -> Unit): Flow<RestResult<T>> {
    return transform { restResult ->
        if (restResult is RestResult.Loading) {
            action.invoke(restResult.loading)
        }
        emit(restResult)
    }
}

inline fun <T, R> RestResult<T>.mapOnSuccess(map: (T?) -> R): RestResult<R> = when (this) {
    is RestResult.Success -> RestResult.Success(map(result))
    is RestResult.Error -> this
    is RestResult.Loading -> this
}