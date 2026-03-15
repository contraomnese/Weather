package com.contraomnese.weather.data.network.remotes.weather

import com.contraomnese.weather.data.network.api.OpenWeatherForecastApi
import com.contraomnese.weather.data.network.parsers.INetworkParser

class OpenWeatherRemote(
    private val api: OpenWeatherForecastApi,
    private val parser: INetworkParser,
)