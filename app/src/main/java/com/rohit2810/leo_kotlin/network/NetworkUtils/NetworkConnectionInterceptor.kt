package com.rohit2810.leo_kotlin.NetworkUtils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkConnectionInterceptor(context: Context) : Interceptor {
    val mcontext = context
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isConnected()) {
            throw NoConnectivityException()
        }
        val builder  = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    fun isConnected() : Boolean {
        val connectivityManager: ConnectivityManager = mcontext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo : NetworkInfo? = connectivityManager.activeNetworkInfo
        return (networkInfo!= null && networkInfo?.isConnected())

    }
}