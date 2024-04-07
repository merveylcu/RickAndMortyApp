package com.merveylcu.characters.domain.util

import com.merveylcu.contract.INetworkError
import com.merveylcu.contract.RestResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

inline fun <reified T> Flow<RestResult<T>>.buildDefaultFlow(
    dispatcher: CoroutineDispatcher,
    loading: Boolean = true
): Flow<RestResult<T>> {
    return this.onStart {
        emit(RestResult.Loading(loading))
    }.catch { e ->
        emit(
            RestResult.Error(
                object : INetworkError {
                    override var message: String? = e.message
                    override var code: Int? = null
                }
            )
        )
    }.onCompletion {
        emit(RestResult.Loading(false))
    }.flowOn(dispatcher)
}
