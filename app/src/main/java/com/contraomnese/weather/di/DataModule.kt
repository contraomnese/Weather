package com.contraomnese.weather.di

import com.contraomnese.weather.BuildConfig
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.interceptors.ApiInterceptor
import com.contraomnese.weather.data.network.models.ErrorResponse
import com.contraomnese.weather.data.repository.CurrentWeatherRepositoryImpl
import com.contraomnese.weather.data.repository.ForecastWeatherRepositoryImpl
import com.contraomnese.weather.data.repository.LocationsRepositoryImpl
import com.contraomnese.weather.data.storage.db.locations.LocationsDatabase
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.repository.CurrentWeatherRepository
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {

    // network
    single<Gson> {
        GsonBuilder().create()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_API_BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    factory<Converter<ResponseBody, ErrorResponse>> {
        get<Retrofit>().responseBodyConverter(ErrorResponse::class.java, emptyArray())
    }

    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(get<ApiInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single<ApiInterceptor> {
        ApiInterceptor(apiKey = BuildConfig.WEATHER_API_KEY)
    }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }
    }

    single<LocationsDatabase> { LocationsDatabase.create(context = get()) }

    single<WeatherApi> { get<Retrofit>().create(WeatherApi::class.java) }

    single<LocationsRepository> { LocationsRepositoryImpl(database = get()) }
    single<CurrentWeatherRepository> { CurrentWeatherRepositoryImpl(api = get(), errorConverter = get()) }
    single<ForecastWeatherRepository> { ForecastWeatherRepositoryImpl(api = get(), errorConverter = get()) }
}