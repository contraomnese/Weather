package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.toDomain
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.models.ErrorResponse
import com.contraomnese.weather.data.network.parsers.parseOrThrowError
import com.contraomnese.weather.domain.app.model.Language.Companion.toLocalCode
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.ForecastWeatherDomainModel
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import kotlinx.coroutines.flow.first
import okhttp3.ResponseBody
import retrofit2.Converter

class ForecastWeatherRepositoryImpl(
    private val api: WeatherApi,
    private val appSettingsRepository: AppSettingsRepository,
    private val errorConverter: Converter<ResponseBody, ErrorResponse>,
) : ForecastWeatherRepository {


    override suspend fun getBy(point: String): ForecastWeatherDomainModel {
        val settings = appSettingsRepository.settings.first()
        val response = api.getForecastWeather(query = point, lang = settings.language.toLocalCode())
        return response.parseOrThrowError(errorConverter).toDomain(settings)
    }
}