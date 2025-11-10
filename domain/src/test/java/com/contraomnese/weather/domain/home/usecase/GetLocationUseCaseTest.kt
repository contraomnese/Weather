package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.utils.assertIsFailure
import com.contraomnese.weather.domain.utils.assertIsSuccess
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import com.contraomnese.weather.domain.weatherByLocation.model.LocationCoordinates
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val LOCATION_LATITUDE = 11.0
private const val LOCATION_LONGITUDE = 22.0

class GetLocationUseCaseTest {

    private lateinit var useCase: GetLocationUseCase
    private val repository = mockk<LocationsRepository>()
    private val expectedData = Location.fromGeo(0, lat, lon)
    private val expectedException = IllegalStateException("Network error")

    @BeforeEach
    fun setUp() {
        useCase = GetLocationUseCaseImpl(repository)
    }

    @Test
    fun `given repository returns success when invoke is called then returns expected location`() = runTest {
        coEvery { repository.getLocationBy(lat = lat, lon = lon) } returns Result.success(expectedData)

        val actualResult = useCase(coordinates)

        val actualData = actualResult.assertIsSuccess()
        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.getLocationBy(lat = lat, lon = lon) }
        confirmVerified(repository)
    }

    @Test
    fun `given repository throws exception when invoke is called then returns failure result`() = runTest {
        coEvery { repository.getLocationBy(lat = lat, lon = lon) } returns Result.failure(expectedException)

        val actualResult = useCase(coordinates)

        val actualData = actualResult.assertIsFailure()
        assertNotNull(actualData)
        assertEquals(expectedException.message, actualData.message)
        coVerify(exactly = 1) { repository.getLocationBy(lat = lat, lon = lon) }
        confirmVerified(repository)
    }

    companion object {
        private val coordinates = LocationCoordinates.from(LOCATION_LATITUDE, LOCATION_LONGITUDE)
        private val lat = coordinates.latitude.value
        private val lon = coordinates.longitude.value
    }
}