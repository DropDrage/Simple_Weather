package com.dropdrage.simpleweather.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(1, TimeUnit.HOURS)
            .onlyIfCached()
            .build()
        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}

class ForceCacheInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        if (isOffline()) {
            builder.cacheControl(CacheControl.FORCE_CACHE)
        }
        return chain.proceed(builder.build())
    }

    private fun isOffline(): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val activeNetwork = connectivityManager.activeNetwork
        return connectivityManager.getNetworkCapabilities(activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) != true
    }

}
