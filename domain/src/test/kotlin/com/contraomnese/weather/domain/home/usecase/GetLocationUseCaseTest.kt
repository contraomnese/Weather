package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.LocationDomainFixtures
import com.contraomnese.weather.domain.assertIsFailure
import com.contraomnese.weather.domain.assertIsSuccess
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetLocationUseCaseTest {

    private lateinit var useCase: GetLocationUseCase
    private val repository = mockk<LocationsRepository>()

    @BeforeEach
    fun setUp() {
        useCase = GetLocationUseCaseImpl(repository)
    }

    @Test
    fun `should return success result with correct item when invoke is called`() = runTest {

        val expectedData = location

        coEvery { repository.getLocationByCoordinates(lat = lat, lon = lon) } returns Result.success(expectedData)

        val actualResult = useCase(coordinates)

        val actualData = actualResult.assertIsSuccess()
        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.getLocationByCoordinates(lat = lat, lon = lon) }
        confirmVerified(repository)
    }

    @Test
    fun `should return failure result with exception when invoke is called`() = runTest {

        val expectedException = repositoryException

        coEvery { repository.getLocationByCoordinates(lat = lat, lon = lon) } returns Result.failure(expectedException)

        val actualResult = useCase(coordinates)

        val actualData = actualResult.assertIsFailure()
        assertNotNull(actualData)
        assertEquals(expectedException.message, actualData.message)
        coVerify(exactly = 1) { repository.getLocationByCoordinates(lat = lat, lon = lon) }
        confirmVerified(repository)
    }

    companion object {
        private val location = LocationDomainFixtures.generateLocation()
        private val coordinates = LocationDomainFixtures.generateLocationCoordinates()
        private val lat = coordinates.latitude.value
        private val lon = coordinates.longitude.value
        private val repositoryException = RuntimeException("Repository error")
    }
}