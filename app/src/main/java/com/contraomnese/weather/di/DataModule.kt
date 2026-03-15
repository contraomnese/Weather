package com.contraomnese.weather.di

import com.contraomnese.weather.BuildConfig
import com.contraomnese.weather.data.network.api.LocationsIQApi
import com.contraomnese.weather.data.network.api.OpenWeatherAirQualityApi
import com.contraomnese.weather.data.network.api.OpenWeatherForecastApi
import com.contraomnese.weather.data.network.api.OpenWeatherGeoCodingApi
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.interceptors.LocationsInterceptor
import com.contraomnese.weather.data.network.interceptors.NetworkConnectionInterceptor
import com.contraomnese.weather.data.network.interceptors.WeatherInterceptor
import com.contraomnese.weather.data.network.models.errors.LocationsErrorResponse
import com.contraomnese.weather.data.network.models.errors.OpenWeatherErrorResponse
import com.contraomnese.weather.data.network.models.errors.WeatherErrorResponse
import com.contraomnese.weather.data.network.parsers.INetworkParser
import com.contraomnese.weather.data.network.parsers.LocationsApiParser
import com.contraomnese.weather.data.network.parsers.OpenWeatherParser
import com.contraomnese.weather.data.network.parsers.WeatherApiParser
import com.contraomnese.weather.data.network.remotes.locations.LocationsRemote
import com.contraomnese.weather.data.network.remotes.locations.OpenWeatherRemote
import com.contraomnese.weather.data.network.remotes.weather.WeatherApiRemote
import com.contraomnese.weather.data.network.remotes.weather.WeatherRemote
import com.contraomnese.weather.data.repository.AppSettingsRepositoryImpl
import com.contraomnese.weather.data.repository.ForecastRepositoryImpl
import com.contraomnese.weather.data.repository.LocationsRepositoryImpl
import com.contraomnese.weather.data.repository.RoomTransactionProvider
import com.contraomnese.weather.data.storage.db.WeatherAppDatabase
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.data.storage.memory.store.AppSettingsStorageImpl
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val W_API = "WeatherApi"
private const val LIQ_API = "LocationIQApi"
private const val OM_FORECAST = "OpenMeteoForecast"
private const val OM_AIR = "OpenMeteoAir"
private const val OM_GEO = "OpenMeteoGeo"

val dataModule = module {

    // --- Core Network Components ---
    single { GsonBuilder().create() }
    single { NetworkConnectionInterceptor(get()) }
    factory {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
        }
    }

    // Вспомогательная функция для сборки OkHttpClient
    fun Scope.provideOkHttp(customInterceptor: Interceptor? = null): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(get<NetworkConnectionInterceptor>())
            .apply { customInterceptor?.let { addInterceptor(it) } }
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    // Вспомогательная функция для Retrofit
    fun Scope.provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    // --- OkHttp Clients ---
    factory(named(W_API)) { provideOkHttp(get<WeatherInterceptor>()) }
    factory(named(LIQ_API)) { provideOkHttp(get<LocationsInterceptor>()) }
    factory(named("OpenMeteoClient")) { provideOkHttp() }

    // --- Retrofit Instances ---
    single(named(W_API)) { provideRetrofit(BuildConfig.WEATHER_API_BASE_URL, get(named(W_API))) }
    single(named(LIQ_API)) { provideRetrofit(BuildConfig.LOCATION_API_BASE_URL, get(named(LIQ_API))) }
    single(named(OM_FORECAST)) { provideRetrofit(BuildConfig.OPEN_WEATHER_FORECAST_URL, get(named("OpenMeteoClient"))) }
    single(named(OM_AIR)) { provideRetrofit(BuildConfig.OPEN_WEATHER_AIR_QUALITY_URL, get(named("OpenMeteoClient"))) }
    single(named(OM_GEO)) { provideRetrofit(BuildConfig.OPEN_WEATHER_GEOCODING_URL, get(named("OpenMeteoClient"))) }

    // --- Error Converters ---
    fun <T> Scope.provideErrorConverter(namedApi: String, clazz: Class<T>): Converter<ResponseBody, T> {
        return get<Retrofit>(named(namedApi)).responseBodyConverter(clazz, emptyArray())
    }

    factory<Converter<ResponseBody, WeatherErrorResponse>> {
        provideErrorConverter(
            W_API,
            WeatherErrorResponse::class.java
        )
    }
    factory<Converter<ResponseBody, LocationsErrorResponse>> {
        provideErrorConverter(
            LIQ_API,
            LocationsErrorResponse::class.java
        )
    }

    listOf(OM_FORECAST, OM_AIR, OM_GEO).forEach { apiName ->
        factory<Converter<ResponseBody, OpenWeatherErrorResponse>> {
            provideErrorConverter(
                apiName,
                OpenWeatherErrorResponse::class.java
            )
        }
    }

    single<INetworkParser>(named(W_API)) {
        WeatherApiParser(
            GsonConverterFactory.create(get()),
            get(named(W_API))
        )
    }
    single<INetworkParser>(named(LIQ_API)) {
        LocationsApiParser(
            GsonConverterFactory.create(get()),
            get(named(LIQ_API))
        )
    }

    single<INetworkParser>(named(OM_FORECAST)) {
        OpenWeatherParser(
            GsonConverterFactory.create(get()),
            get(named(OM_FORECAST))
        )
    }

    single<INetworkParser>(named(OM_AIR)) {
        OpenWeatherParser(
            GsonConverterFactory.create(get()),
            get(named(OM_AIR))
        )
    }

    single<INetworkParser>(named(OM_GEO)) {
        OpenWeatherParser(
            GsonConverterFactory.create(get()),
            get(named(OM_GEO))
        )
    }

    single<WeatherInterceptor> { WeatherInterceptor(apiKey = BuildConfig.WEATHER_API_KEY, appSettingsStorage = get()) }
    single<LocationsInterceptor> { LocationsInterceptor(apiKey = BuildConfig.LOCATION_API_KEY) }

    // --- Storage & DB ---
    single<WeatherAppDatabase> { WeatherAppDatabase.create(get()) }
    single<AppSettingsStorage> { AppSettingsStorageImpl(get(), Dispatchers.IO) }

    // --- API Services ---
    single<OpenWeatherForecastApi> { get<Retrofit>(named(OM_FORECAST)).create(OpenWeatherForecastApi::class.java) }
    single<OpenWeatherAirQualityApi> { get<Retrofit>(named(OM_AIR)).create(OpenWeatherAirQualityApi::class.java) }
    single<OpenWeatherGeoCodingApi> { get<Retrofit>(named(OM_GEO)).create(OpenWeatherGeoCodingApi::class.java) }
    single<WeatherApi> { get<Retrofit>(named(W_API)).create(WeatherApi::class.java) }
    single<LocationsIQApi> { get<Retrofit>(named(LIQ_API)).create(LocationsIQApi::class.java) }

    // --- Repositories & Remotes ---
    single<LocationsRemote> {
        OpenWeatherRemote(
            get(),
            get(named(OM_GEO))
        )
    }
    single<WeatherRemote> {
        WeatherApiRemote(
            get(),
            get(named(W_API))
        )
    }

    single<LocationsRepository> {
        LocationsRepositoryImpl(
            get(),
            get(),
            get(),
            get(named(OM_GEO)),
            Dispatchers.IO
        )
    }

    single<ForecastRepository> {
        ForecastRepositoryImpl(
            get(),
            get(named(W_API)),
            get(),
            get(),
            Dispatchers.IO,
            transactionProvider = RoomTransactionProvider(get<WeatherAppDatabase>())
        )
    }

    single<AppSettingsRepository> {
        AppSettingsRepositoryImpl(
            storage = get(),
            dispatcher = Dispatchers.IO
        )
    }
}