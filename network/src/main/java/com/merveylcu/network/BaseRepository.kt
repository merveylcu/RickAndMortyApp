package com.merveylcu.network

import com.merveylcu.contract.INetworkError
import com.merveylcu.contract.RestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

open class BaseRepository {

    suspend inline fun <reified T : Any> request(
        crossinline call: suspend () -> Response<T>
    ): RestResult<T> = withContext(Dispatchers.IO) {
        try {
            call.invoke().asRestResult
        } catch (exception: Exception) {
            genericError(message = exception.message)
        }
    }

    inline val <reified T> Response<T>.asRestResult: RestResult<T>
        get() {
            return if (isSuccessful.not()) {
                parseError(this)
            } else {
                val body = body() ?: return genericError(message = message())
                RestResult.Success(body)
            }
        }

    fun <T> parseError(response: Response<T>): RestResult.Error {
        return when (response.code()) {
            //401 -> authorize error
            else -> genericError(message = response.message())
        }
    }

    fun genericError(code: Int? = null, message: String?) =
        RestResult.Error(object : INetworkError {
            override val code = code
            override val message = message
        })
}
