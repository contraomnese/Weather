package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.forecast.internal.toEntity
import com.contraomnese.weather.data.mappers.forecast.internal.toForecastDayEntity
import com.contraomnese.weather.data.mappers.forecast.toDomain
import com.contraomnese.weather.data.mappers.locations.toEntity
import com.contraomnese.weather.data.network.models.AlertNetwork
import com.contraomnese.weather.data.network.models.AstroNetwork
import com.contraomnese.weather.data.network.models.ForecastCurrentNetwork
import com.contraomnese.weather.data.network.models.ForecastDayNetwork
import com.contraomnese.weather.data.network.models.ForecastLocationNetwork
import com.contraomnese.weather.data.network.models.HourNetwork
import com.contraomnese.weather.data.storage.db.locations.dto.ForecastData
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class MapperMockerExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        mockkStatic(ForecastData::toDomain)
        mockkStatic(ForecastLocationNetwork::toEntity)
        mockkStatic(AlertNetwork::toEntity)
        mockkStatic(AstroNetwork::toEntity)
        mockkStatic(ForecastCurrentNetwork::toEntity)
        mockkStatic(ForecastDayNetwork::toForecastDayEntity)
        mockkStatic(ForecastDayNetwork::toEntity)
        mockkStatic(HourNetwork::toEntity)
    }

    override fun afterEach(context: ExtensionContext?) {
        unmockkStatic(ForecastData::toDomain)
        unmockkStatic(ForecastLocationNetwork::toEntity)
        unmockkStatic(AlertNetwork::toEntity)
        unmockkStatic(AstroNetwork::toEntity)
        unmockkStatic(ForecastCurrentNetwork::toEntity)
        unmockkStatic(ForecastDayNetwork::toForecastDayEntity)
        unmockkStatic(ForecastDayNetwork::toEntity)
        unmockkStatic(HourNetwork::toEntity)
    }
}