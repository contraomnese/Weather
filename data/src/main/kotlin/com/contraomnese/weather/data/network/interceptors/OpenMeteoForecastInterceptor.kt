package com.contraomnese.weather.data.network.interceptors

import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import okhttp3.Interceptor
import okhttp3.Response

class OpenMeteoForecastInterceptor(
    private val appSettingsStorage: AppSettingsStorage,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}