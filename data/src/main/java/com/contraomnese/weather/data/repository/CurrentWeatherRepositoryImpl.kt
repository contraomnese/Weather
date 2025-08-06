package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.toDomain
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.models.ErrorResponse
import com.contraomnese.weather.data.network.parsers.parseOrThrowError
import com.contraomnese.weather.domain.locationForecast.model.CurrentWeatherDomainModel
import com.contraomnese.weather.domain.locationForecast.repository.CurrentWeatherRepository
import okhttp3.ResponseBody
import retrofit2.Converter

class CurrentWeatherRepositoryImpl(
    private val api: WeatherApi,
    private val errorConverter: Converter<ResponseBody, ErrorResponse>,
) : CurrentWeatherRepository {

    override suspend fun getWeatherBy(point: String): CurrentWeatherDomainModel {
        val response = api.getCurrentWeather(query = point)
        return response.parseOrThrowError(errorConverter).toDomain()
    }
}