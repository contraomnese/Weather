package com.contraomnese.weather.weatherByLocation.presentation.data

sealed interface WeatherSectionType {
    data class Solo(val section: WeatherSection) : WeatherSectionType
    data class InRow(val firstSection: WeatherSection, val secondSection: WeatherSection) : WeatherSectionType
}

sealed interface WeatherSection {

    operator fun component1(): Float = bodyHeight
    operator fun component2(): Float = bodyMaxHeight

    val bodyHeight: Float
    val bodyMaxHeight: Float

    fun copyWithBodyHeight(newHeight: Float): WeatherSection

    data class HourlyForecastSection(
        override val bodyHeight: Float,
        override val bodyMaxHeight: Float = bodyHeight,
    ) : WeatherSection {
        override fun copyWithBodyHeight(newHeight: Float): WeatherSection = copy(bodyHeight = newHeight)
    }

    data class DailyForecastSection(
        override val bodyHeight: Float,
        override val bodyMaxHeight: Float = bodyHeight,
    ) : WeatherSection {
        override fun copyWithBodyHeight(newHeight: Float): WeatherSection = copy(bodyHeight = newHeight)
    }

    data class UVIndexSection(
        override val bodyHeight: Float,
        override val bodyMaxHeight: Float = bodyHeight,
    ) : WeatherSection {
        override fun copyWithBodyHeight(newHeight: Float): WeatherSection = copy(bodyHeight = newHeight)
    }

    data class SunriseSection(
        override val bodyHeight: Float,
        override val bodyMaxHeight: Float = bodyHeight,
    ) : WeatherSection {
        override fun copyWithBodyHeight(newHeight: Float): WeatherSection = copy(bodyHeight = newHeight)
    }

    data class AqiSection(
        override val bodyHeight: Float,
        override val bodyMaxHeight: Float = bodyHeight,
    ) : WeatherSection {
        override fun copyWithBodyHeight(newHeight: Float): WeatherSection = copy(bodyHeight = newHeight)
    }
}

internal fun WeatherSectionType.sectionsList(): List<WeatherSection> =
    when (this) {
        is WeatherSectionType.Solo -> listOf(section)
        is WeatherSectionType.InRow -> listOf(firstSection, secondSection)
    }

internal fun List<WeatherSectionType>.mapSections(
    transform: (index: Int, WeatherSection) -> WeatherSection,
): List<WeatherSectionType> {
    var globalIndex = 0
    return map { type ->
        when (type) {
            is WeatherSectionType.Solo -> {
                val newSection = transform(globalIndex, type.section)
                globalIndex++
                WeatherSectionType.Solo(newSection)
            }

            is WeatherSectionType.InRow -> {
                val first = transform(globalIndex, type.firstSection)
                globalIndex++
                val second = transform(globalIndex, type.secondSection)
                globalIndex++
                WeatherSectionType.InRow(first, second)
            }
        }
    }
}