package com.contraomnese.weather.di

import com.contraomnese.weather.BuildConfig
import com.contraomnese.weather.data.network.api.LocationsApi
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.interceptors.LocationsInterceptor
import com.contraomnese.weather.data.network.interceptors.WeatherInterceptor
import com.contraomnese.weather.data.network.models.LocationsErrorResponse
import com.contraomnese.weather.data.network.models.WeatherErrorResponse
import com.contraomnese.weather.data.network.parsers.ApiParser
import com.contraomnese.weather.data.network.parsers.ApiParserImpl
import com.contraomnese.weather.data.repository.AppSettingsRepositoryImpl
import com.contraomnese.weather.data.repository.ForecastRepositoryImpl
import com.contraomnese.weather.data.repository.LocationsRepositoryImpl
import com.contraomnese.weather.data.repository.RoomTransactionProvider
import com.contraomnese.weather.data.repository.TransactionProvider
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.data.storage.memory.store.AppSettingsStorageImpl
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val WEATHER = "WeatherApi"
private const val LOCATIONS = "LocationsApi"

val dataModule = module {

    // network
    single<Gson> {
        GsonBuilder().create()
    }

    factory<OkHttpClient>(named(WEATHER)) {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(get<WeatherInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    factory<OkHttpClient>(named(LOCATIONS)) {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(get<LocationsInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single<Retrofit>(named(WEATHER)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_API_BASE_URL)
            .client(get<OkHttpClient>(named(WEATHER)))
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<Retrofit>(named(LOCATIONS)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.LOCATION_API_BASE_URL)
            .client(get<OkHttpClient>(named(LOCATIONS)))
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    factory<Converter<ResponseBody, WeatherErrorResponse>> {
        get<Retrofit>(named(WEATHER)).responseBodyConverter(WeatherErrorResponse::class.java, emptyArray())
    }

    factory<Converter<ResponseBody, LocationsErrorResponse>> {
        get<Retrofit>(named(LOCATIONS)).responseBodyConverter(LocationsErrorResponse::class.java, emptyArray())
    }

    single<ApiParser>(named(WEATHER)) {
        ApiParserImpl(
            converterFactory = GsonConverterFactory.create(get()),
            retrofit = get<Retrofit>(named(WEATHER))
        )
    }

    single<ApiParser>(named(LOCATIONS)) {
        ApiParserImpl(
            converterFactory = GsonConverterFactory.create(get()),
            retrofit = get<Retrofit>(named(LOCATIONS))
        )
    }

    single<WeatherInterceptor> {
        WeatherInterceptor(apiKey = BuildConfig.WEATHER_API_KEY, appSettingsStorage = get())
    }

    single<LocationsInterceptor> { LocationsInterceptor(apiKey = BuildConfig.LOCATION_API_KEY) }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }
    }

    single<WeatherDatabase> { WeatherDatabase.create(context = get()) }

    single<AppSettingsStorage> { AppSettingsStorageImpl(context = get(), dispatcher = Dispatchers.IO) }

    single<WeatherApi> { get<Retrofit>(named(WEATHER)).create(WeatherApi::class.java) }
    single<LocationsApi> { get<Retrofit>(named(LOCATIONS)).create(LocationsApi::class.java) }

    single<LocationsRepository> {
        LocationsRepositoryImpl(
            database = get(),
            apiParser = get(named(LOCATIONS)),
            locationsApi = get(),
            dispatcher = Dispatchers.IO
        )
    }
    single<ForecastRepository> {
        ForecastRepositoryImpl(
            api = get(),
            apiParser = get(named(WEATHER)),
            appSettingsRepository = get(),
            database = get(),
            dispatcher = Dispatchers.IO,
            transactionProvider = get(named(WEATHER))
        )
    }
    single<AppSettingsRepository> {
        AppSettingsRepositoryImpl(
            storage = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<TransactionProvider>(named(WEATHER)) { RoomTransactionProvider(db = get<WeatherDatabase>()) }
}