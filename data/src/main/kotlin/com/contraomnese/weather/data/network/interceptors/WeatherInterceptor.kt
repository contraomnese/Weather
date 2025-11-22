package com.contraomnese.weather.data.network.interceptors

import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import okhttp3.Interceptor
import okhttp3.Response

class WeatherInterceptor(
    private val apiKey: String,
    private val appSettingsStorage: AppSettingsStorage,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val language = appSettingsStorage.getSettings().language

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("key", apiKey)
            .addQueryParameter("lang", language)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}