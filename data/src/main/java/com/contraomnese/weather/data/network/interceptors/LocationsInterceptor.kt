package com.contraomnese.weather.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

class LocationsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .header("User-Agent", "WeatherApp (contraomnese@gmail.com)")
            .header("Accept-Language", Locale.getDefault().language)
            .build()

        return chain.proceed(newRequest)
    }
}