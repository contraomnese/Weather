package com.contraomnese.weather.di

import com.contraomnese.weather.BuildConfig
import com.contraomnese.weather.data.network.api.LocationsIQApi
import com.contraomnese.weather.data.network.api.OpenMeteoAirQualityApi
import com.contraomnese.weather.data.network.api.OpenMeteoForecastApi
import com.contraomnese.weather.data.network.api.OpenMeteoGeoCodingApi
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.interceptors.LocationsInterceptor
import com.contraomnese.weather.data.network.interceptors.NetworkConnectionInterceptor
import com.contraomnese.weather.data.network.interceptors.OpenMeteoForecastInterceptor
import com.contraomnese.weather.data.network.interceptors.WeatherInterceptor
import com.contraomnese.weather.data.network.models.errors.LocationsErrorResponse
import com.contraomnese.weather.data.network.models.errors.OpenMeteoErrorResponse
import com.contraomnese.weather.data.network.models.errors.WeatherErrorResponse
import com.contraomnese.weather.data.network.parsers.INetworkParser
import com.contraomnese.weather.data.network.parsers.LocationsApiParser
import com.contraomnese.weather.data.network.parsers.OpenMeteoParser
import com.contraomnese.weather.data.network.parsers.WeatherApiParser
import com.contraomnese.weather.data.network.remotes.locations.LocationsRemote
import com.contraomnese.weather.data.network.remotes.locations.OpenMeteoGeoRemote
import com.contraomnese.weather.data.network.remotes.weather.ForecastRemote
import com.contraomnese.weather.data.network.remotes.weather.OpenMeteoRemote
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
    factory(named(OM_FORECAST)) { provideOkHttp(get<OpenMeteoForecastInterceptor>()) }
    factory(named(OM_AIR)) { provideOkHttp() }
    factory(named(OM_GEO)) { provideOkHttp() }

    // --- Retrofit Instances ---
    single(named(W_API)) { provideRetrofit(BuildConfig.WEATHER_API_BASE_URL, get(named(W_API))) }
    single(named(LIQ_API)) { provideRetrofit(BuildConfig.LOCATION_API_BASE_URL, get(named(LIQ_API))) }
    single(named(OM_FORECAST)) { provideRetrofit(BuildConfig.OPEN_WEATHER_FORECAST_URL, get(named(OM_FORECAST))) }
    single(named(OM_AIR)) { provideRetrofit(BuildConfig.OPEN_WEATHER_AIR_QUALITY_URL, get(named(OM_AIR))) }
    single(named(OM_GEO)) { provideRetrofit(BuildConfig.OPEN_WEATHER_GEOCODING_URL, get(named(OM_GEO))) }

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
        factory<Converter<ResponseBody, OpenMeteoErrorResponse>> {
            provideErrorConverter(
                apiName,
                OpenMeteoErrorResponse::class.java
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
        OpenMeteoParser(
            GsonConverterFactory.create(get()),
            get(named(OM_FORECAST))
        )
    }

    single<INetworkParser>(named(OM_AIR)) {
        OpenMeteoParser(
            GsonConverterFactory.create(get()),
            get(named(OM_AIR))
        )
    }

    single<INetworkParser>(named(OM_GEO)) {
        OpenMeteoParser(
            GsonConverterFactory.create(get()),
            get(named(OM_GEO))
        )
    }

    single<WeatherInterceptor> { WeatherInterceptor(apiKey = BuildConfig.WEATHER_API_KEY, appSettingsStorage = get()) }
    single<LocationsInterceptor> { LocationsInterceptor(apiKey = BuildConfig.LOCATION_API_KEY) }
    single<OpenMeteoForecastInterceptor> { OpenMeteoForecastInterceptor(appSettingsStorage = get()) }

    // --- Storage & DB ---
    single<WeatherAppDatabase> { WeatherAppDatabase.create(get()) }
    single<AppSettingsStorage> { AppSettingsStorageImpl(get(), Dispatchers.IO) }

    // --- API Services ---
    single<OpenMeteoForecastApi> { get<Retrofit>(named(OM_FORECAST)).create(OpenMeteoForecastApi::class.java) }
    single<OpenMeteoAirQualityApi> { get<Retrofit>(named(OM_AIR)).create(OpenMeteoAirQualityApi::class.java) }
    single<OpenMeteoGeoCodingApi> { get<Retrofit>(named(OM_GEO)).create(OpenMeteoGeoCodingApi::class.java) }
    single<WeatherApi> { get<Retrofit>(named(W_API)).create(WeatherApi::class.java) }
    single<LocationsIQApi> { get<Retrofit>(named(LIQ_API)).create(LocationsIQApi::class.java) }

    // --- Repositories & Remotes ---
    single<LocationsRemote> {
        OpenMeteoGeoRemote(
            get(),
            get(named(OM_GEO))
        )
    }
    single<ForecastRemote> {
        OpenMeteoRemote(
            forecastApi = get(),
            airQualityApi = get(),
            get(named(W_API))
        )
    }
//    single<ForecastRemote> {
//        WeatherApiRemote(
//            api = get(),
//            get(named(W_API))
//        )
//    }

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