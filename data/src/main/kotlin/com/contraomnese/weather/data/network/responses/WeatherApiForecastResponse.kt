package com.contraomnese.weather.data.network.responses

import com.contraomnese.weather.data.network.models.weatherapi.ForecastAlertsWeatherNetwork
import com.contraomnese.weather.data.network.models.weatherapi.ForecastCurrentNetwork
import com.contraomnese.weather.data.network.models.weatherapi.ForecastLocationNetwork
import com.contraomnese.weather.data.network.models.weatherapi.ForecastNetwork

data class WeatherApiForecastResponse(
    val location: ForecastLocationNetwork,
    val current: ForecastCurrentNetwork,
    val forecast: ForecastNetwork,
    val alerts: ForecastAlertsWeatherNetwork,
)