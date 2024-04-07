package com.merveylcu.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.merveylcu.network.R
import com.merveylcu.network.exception.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NoConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isConnectionOn()) {
            throw NoInternetException(context.getString(R.string.error_message_no_internet))
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun isConnectionOn(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return postAndroidMInternetCheck(connectivityManager)
    }

    private fun postAndroidMInternetCheck(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val connection =
            connectivityManager.getNetworkCapabilities(network)

        return connection != null && (
            connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            )
    }
}
