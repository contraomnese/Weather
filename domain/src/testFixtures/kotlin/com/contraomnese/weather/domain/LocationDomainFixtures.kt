package com.contraomnese.weather.domain

import com.contraomnese.weather.domain.weatherByLocation.model.Latitude
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import com.contraomnese.weather.domain.weatherByLocation.model.Longitude
import kotlin.random.Random

object LocationDomainFixtures {

    private val random = Random(1234)
    private val cities = listOf("Moscow", "London", "New York", null)
    private val states = listOf("State1", "State 2", "State Three", null)
    private val countries = listOf("country1", "Country 2", "country Three", null)

    fun generateLocationName() = cities.filterNotNull().random(random)
    fun generateLocationId() = random.nextInt()

    fun generateLocation() = Location(
        id = random.nextInt(),
        city = randomCity(),
        state = randomState(),
        country = randomCountry(),
        geo = generateLocationCoordinates()
    )

    fun generateLocations() = List(random.nextInt(2, 5)) { generateLocation() }

    private fun randomCity() = cities.random(random)
    private fun randomState() = states.random(random)
    private fun randomCountry() = countries.random(random)

    fun generateLocationCoordinates() = LocationCoordinates(
        latitude = randomLatitude(),
        longitude = randomLongitude()
    )

    private fun randomLatitude() = Latitude(random.nextDouble(from = -90.0, until = 90.0))
    private fun randomLongitude() = Longitude(random.nextDouble(from = -180.0, until = 180.0))
}