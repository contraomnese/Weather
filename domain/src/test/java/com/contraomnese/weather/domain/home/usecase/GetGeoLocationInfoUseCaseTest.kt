package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.CoordinatesDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.DetailsLocationDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LatitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.model.LongitudeDomainModel
import com.contraomnese.weather.domain.weatherByLocation.usecase.GetDetailsLocationUseCase
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

private const val GEO_LOCATION_ID = 1
private const val GEO_LOCATION_NAME = "name"
private const val GEO_LOCATION_LATITUDE = 123.123
private const val GEO_LOCATION_LONGITUDE = 321.321

private const val BAD_GEO_LOCATION_ID = 0

class GetGeoLocationInfoUseCaseTest {

    private lateinit var useCase: GetDetailsLocationUseCase
    private val repositoryMock = mockk<LocationsRepository>()
    private lateinit var coroutineContextProvider: CoroutineContextProvider
    private val expectedPoint = CoordinatesDomainModel(
        latitude = LatitudeDomainModel(GEO_LOCATION_LATITUDE),
        longitude = LongitudeDomainModel(GEO_LOCATION_LONGITUDE)
    )
    private val expectedLocation =
        DetailsLocationDomainModel(id = GEO_LOCATION_ID, name = GEO_LOCATION_NAME, point = expectedPoint)

    private val expectedException = Exception("Location not found")

    @BeforeEach
    fun setUp() {
        coroutineContextProvider = FakeCoroutineContextProvider
        useCase = GetDetailsLocationUseCase(
            repository = repositoryMock,
            coroutineContextProvider = coroutineContextProvider
        )
    }

    @Test
    fun `test use case returns correct location`() = runTest {
        coEvery { repositoryMock.getLocationBy(id = GEO_LOCATION_ID) } returns expectedLocation
        val actualResult = useCase.executeInBackground(request = GEO_LOCATION_ID)
        assertEquals(expectedLocation, actualResult)
        verify { repositoryMock.getLocationBy(id = GEO_LOCATION_ID) }
    }

    @Test
    fun `test use case uses repository correctly`() = runTest {
        coEvery { repositoryMock.getLocationBy(id = GEO_LOCATION_ID) } returns expectedLocation
        useCase.executeInBackground(request = GEO_LOCATION_ID)
        coVerify(exactly = 1) { repositoryMock.getLocationBy(id = GEO_LOCATION_ID) }
        confirmVerified(repositoryMock)
    }

    @Test
    fun `test use case return throw when call repository with bad id`() = runTest {
        // Arrange
        coEvery { repositoryMock.getLocationBy(BAD_GEO_LOCATION_ID) } throws expectedException

        // Act & Assert
        val thrown = assertFailsWith<Exception> {
            useCase.executeInBackground(BAD_GEO_LOCATION_ID)
        }

        assertEquals(expectedException.message, thrown.message)
        coVerify { repositoryMock.getLocationBy(BAD_GEO_LOCATION_ID) }
        confirmVerified(repositoryMock)
    }
}