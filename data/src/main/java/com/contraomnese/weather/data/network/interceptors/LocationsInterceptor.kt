package com.contraomnese.weather.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class LocationsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .header("User-Agent", "WeatherApp (contraomnese@gmail.com)")
            .build()

        return chain.proceed(newRequest)
    }
}