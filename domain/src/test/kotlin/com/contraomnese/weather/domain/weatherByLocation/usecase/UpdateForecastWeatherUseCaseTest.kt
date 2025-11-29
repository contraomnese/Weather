package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.LocationDomainFixtures
import com.contraomnese.weather.domain.assertIsFailure
import com.contraomnese.weather.domain.assertIsSuccess
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateForecastWeatherUseCaseTest {

    private lateinit var useCase: UpdateForecastWeatherUseCase
    private val repository = mockk<ForecastRepository>()

    @BeforeEach
    fun setUp() {
        useCase = UpdateForecastWeatherUseCaseImpl(repository)
    }

    @Test
    fun `should return success result with updated location id when invoke is called`() = runTest {

        val expectedData = locationId

        coEvery { repository.refreshForecastByLocationId(locationId) } returns Result.success(expectedData)
        val actualResult = useCase(locationId)

        val actualData = actualResult.assertIsSuccess()
        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.refreshForecastByLocationId(locationId) }
        confirmVerified(repository)
    }

    @Test
    fun `should return failure result with exception invoke is called`() = runTest {

        val expectedException = repositoryException

        coEvery { repository.refreshForecastByLocationId(locationId) } returns Result.failure(expectedException)
        val actualResult = useCase(locationId)

        val actualException = actualResult.assertIsFailure()
        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.refreshForecastByLocationId(locationId) }
        confirmVerified(repository)
    }

    companion object {
        private val locationId = LocationDomainFixtures.generateLocationId()
        private val repositoryException = RuntimeException("Repository exception")
    }

}