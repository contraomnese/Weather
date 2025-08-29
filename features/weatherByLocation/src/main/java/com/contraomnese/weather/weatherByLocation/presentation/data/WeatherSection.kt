package com.contraomnese.weather.weatherByLocation.presentation.data


sealed interface WeatherSection {
    val bodyHeight: Float
    val bodyMaxHeight: Float
    fun copyWithBodyHeight(newHeight: Float): WeatherSection

    operator fun component1(): Float = bodyHeight
    operator fun component2(): Float = bodyMaxHeight
}

data class HourlyForecastSection(override val bodyHeight: Float, override val bodyMaxHeight: Float = bodyHeight) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) = copy(bodyHeight = newHeight)
}

data class DailyForecastSection(override val bodyHeight: Float, override val bodyMaxHeight: Float = bodyHeight) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) = copy(bodyHeight = newHeight)
}

data class UVIndexSection(override val bodyHeight: Float, override val bodyMaxHeight: Float = bodyHeight) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) = copy(bodyHeight = newHeight)
}

data class HumiditySection(override val bodyHeight: Float, override val bodyMaxHeight: Float = bodyHeight) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) = copy(bodyHeight = newHeight)
}

data class SunriseSection(override val bodyHeight: Float, override val bodyMaxHeight: Float = bodyHeight) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) = copy(bodyHeight = newHeight)
}

data class WindSection(override val bodyHeight: Float, override val bodyMaxHeight: Float = bodyHeight) :
    WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) = copy(bodyHeight = newHeight)
}

data class AqiSection(override val bodyHeight: Float, override val bodyMaxHeight: Float = bodyHeight) : WeatherSection {
    override fun copyWithBodyHeight(newHeight: Float) = copy(bodyHeight = newHeight)
}