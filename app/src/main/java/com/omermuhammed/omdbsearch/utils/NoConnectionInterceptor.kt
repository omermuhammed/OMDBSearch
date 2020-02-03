package com.omermuhammed.omdbsearch.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.omermuhammed.omdbsearch.MainApp
import com.omermuhammed.omdbsearch.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

// OkHttp intercepter to detect connection failure errors
class NoConnectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        return if (!isConnectionOn()) {
            throw NoConnectivityException()

        } else if(!isInternetAvailable()) {
            throw NoInternetException()

        } else {
            chain.proceed(chain.request())
        }
    }

    @Suppress("DEPRECATION")
    private fun isConnectionOn(): Boolean {
        val context: Context = MainApp.appContext
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)

            return connection != null && (
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))

        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null) {
                return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            }
            return false
        }
    }

    // For speedy response of no internet check, we could perform a quick connection
    // to Google Public DNS (i.e. 8.8.8.8) to check
    private fun isInternetAvailable(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)

            sock.connect(sockaddr, timeoutMs)
            sock.close()

            true

        } catch (e: IOException) {
            false
        }

    }

    class NoConnectivityException : IOException() {
        override val message: String
            get() = MainApp.appContext.resources.getString(R.string.no_network_error)
    }

    class NoInternetException : IOException() {
        override val message: String
            get() = MainApp.appContext.resources.getString(R.string.no_internet_error)
    }
}