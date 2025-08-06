package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.locationForecast.model.LatitudeDomainModel
import com.contraomnese.weather.domain.locationForecast.model.LocationDomainPointModel
import com.contraomnese.weather.domain.locationForecast.model.LocationForecastDomainModel
import com.contraomnese.weather.domain.locationForecast.model.LongitudeDomainModel
import com.contraomnese.weather.domain.locationForecast.usecase.GetLocationForecastUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

private const val LOCATION_FORECAST_ID = 1
private const val LOCATION_FORECAST_NAME = "name"
private const val LOCATION_FORECAST_LATITUDE = 123.123
private const val LOCATION_FORECAST_LONGITUDE = 321.321

private const val BAD_LOCATION_FORECAST_ID = 0

class GetLocationForecastUseCaseTest {

    private lateinit var useCase: GetLocationForecastUseCase
    private val repositoryMock = mockk<LocationsRepository>()
    private lateinit var coroutineContextProvider: CoroutineContextProvider
    private val expectedPoint = LocationDomainPointModel(
        latitude = LatitudeDomainModel(LOCATION_FORECAST_LATITUDE),
        longitude = LongitudeDomainModel(LOCATION_FORECAST_LONGITUDE)
    )
    private val expectedLocation =
        LocationForecastDomainModel(id = LOCATION_FORECAST_ID, name = LOCATION_FORECAST_NAME, point = expectedPoint)

    private val expectedException = Exception("Location not found")

    @BeforeEach
    fun setUp() {
        coroutineContextProvider = FakeCoroutineContextProvider
        useCase = GetLocationForecastUseCase(
            repository = repositoryMock,
            coroutineContextProvider = coroutineContextProvider
        )
    }

    @Test
    fun `test use case returns correct location`() = runTest {
        coEvery { repositoryMock.getLocationBy(id = LOCATION_FORECAST_ID) } returns expectedLocation
        val actualResult = useCase.executeInBackground(request = LOCATION_FORECAST_ID)
        assertEquals(expectedLocation, actualResult)
        verify { repositoryMock.getLocationBy(id = LOCATION_FORECAST_ID) }
    }

    @Test
    fun `test use case uses repository correctly`() = runTest {
        coEvery { repositoryMock.getLocationBy(id = LOCATION_FORECAST_ID) } returns expectedLocation
        useCase.executeInBackground(request = LOCATION_FORECAST_ID)
        coVerify(exactly = 1) { repositoryMock.getLocationBy(id = LOCATION_FORECAST_ID) }
        confirmVerified(repositoryMock)
    }

    @Test
    fun `test use case return throw when call repository with bad id`() = runTest {
        // Arrange
        coEvery { repositoryMock.getLocationBy(BAD_LOCATION_FORECAST_ID) } throws expectedException

        // Act & Assert
        val thrown = assertFailsWith<Exception> {
            useCase.executeInBackground(BAD_LOCATION_FORECAST_ID)
        }

        assertEquals(expectedException.message, thrown.message)
        coVerify { repositoryMock.getLocationBy(BAD_LOCATION_FORECAST_ID) }
        confirmVerified(repositoryMock)
    }
}