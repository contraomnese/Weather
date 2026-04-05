package com.contraomnese.weather.data.mappers.forecast.openmeteo

import com.contraomnese.weather.data.mappers.utils.celsiusToFahrenheit
import com.contraomnese.weather.data.mappers.utils.kmToMiles
import com.contraomnese.weather.data.mappers.utils.kphToMph
import com.contraomnese.weather.data.mappers.utils.mmToInch
import com.contraomnese.weather.data.network.models.openmeteo.forecast.ForecastDaily
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastAstroEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDailyEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDayEntity
import com.contraomnese.weather.data.utils.getAmPmTime

fun ForecastDaily.toForecastDailyEntities(forecastLocationId: Int): List<ForecastDailyEntity> =
    time.indices.map { index ->
        ForecastDailyEntity(
            forecastLocationId = forecastLocationId,
            dateEpoch = time[index],
        )
    }

fun ForecastDaily.toForecastDayEntities(): List<ForecastDayEntity> =
    time.indices.map { index ->
        ForecastDayEntity(
            forecastDailyId = index,
            maxTempC = temperature2mMax[index],
            maxTempF = celsiusToFahrenheit(temperature2mMax[index]),
            minTempC = temperature2mMin[index],
            minTempF = celsiusToFahrenheit(temperature2mMin[index]),
            avgTempC = temperature2mMean[index],
            avgTempF = celsiusToFahrenheit(temperature2mMean[index]),
            maxWindKph = windSpeed10mMax[index],
            maxWindMph = kphToMph(windSpeed10mMax[index]),
            totalPrecipMm = precipitationSum[index],
            totalPrecipIn = mmToInch(precipitationSum[index]),
            totalSnowCm = snowfallSum[index],
            avgVisKm = visibilityMean[index],
            avgVisMiles = kmToMiles(visibilityMean[index]),
            avgHumidity = relativeHumidity2mMean[index].toInt(),
            conditionCode = weatherCode[index],
            uv = uvIndexMax[index],
            dayWillItRain = precipitationProbabilityMax[index],
            dayWillItSnow = precipitationProbabilityMax[index],
            dayChanceOfRain = precipitationProbabilityMax[index],
            dayChanceOfSnow = precipitationProbabilityMax[index]
        )
    }

fun ForecastDaily.toForecastAstroEntities(timeZone: String): List<ForecastAstroEntity> =
    time.indices.map { index ->
        ForecastAstroEntity(
            forecastDailyId = index,
            sunrise = getAmPmTime(sunrise[index], timeZone),
            sunset = getAmPmTime(sunset[index], timeZone),
            isSunUp = if (time[index] > sunrise[index]) 1 else 0
        )
    }