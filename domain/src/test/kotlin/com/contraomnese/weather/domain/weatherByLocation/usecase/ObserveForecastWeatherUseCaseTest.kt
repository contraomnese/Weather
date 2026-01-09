package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.ForecastDomainFixtures
import com.contraomnese.weather.domain.LocationDomainFixtures
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ObserveForecastWeatherUseCaseTest {

    private lateinit var useCase: ObserveForecastWeatherUseCase
    private val repository = mockk<ForecastRepository>()

    @BeforeEach
    fun setUp() {
        useCase = ObserveForecastWeatherUseCaseImpl(repository)
    }

    @Test
    fun `should return flow with correct item when invoke is called`() = runTest {

        val expectedData = forecast

        coEvery { repository.observeForecastByLocationId(locationId) } returns flowOf(expectedData)

        val actualData = useCase(locationId).first()

        assertNotNull(actualData)
        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.observeForecastByLocationId(locationId) }
        confirmVerified(repository)
    }

    @Test
    fun `should return flow with correct multiple items when invoke is called`() = runTest {

        val expectedData = listOf(firstItem, secondItem)

        coEvery { repository.observeForecastByLocationId(locationId) } returns flow {
            emit(firstItem)
            emit(secondItem)
        }
        val actualResult = useCase(locationId).toList()

        assertEquals(expectedData.size, actualResult.size)
        assertNotNull(actualResult[0])
        assertNotNull(actualResult[1])
        assertEquals(expectedData[0], actualResult[0])
        assertEquals(expectedData[1], actualResult[1])
        coVerify(exactly = 1) { repository.observeForecastByLocationId(locationId) }
        confirmVerified(repository)
    }

    @Test
    fun `should return flow with null items when invoke is called`() = runTest {

        val expectedData = listOf(null, null)

        coEvery { repository.observeForecastByLocationId(locationId) } returns flow {
            emit(null)
            emit(null)
        }
        val actualResult = useCase(locationId).toList()

        assertEquals(expectedData.size, actualResult.size)
        assertNull(actualResult[0])
        assertNull(actualResult[1])
        coVerify(exactly = 1) { repository.observeForecastByLocationId(locationId) }
        confirmVerified(repository)
    }

    @Test
    fun `should return failure result with exception invoke is called`() = runTest {

        val expectedException = repositoryException

        coEvery { repository.observeForecastByLocationId(locationId) } returns flow {
            throw expectedException
        }
        val actualException = assertFailsWith<RuntimeException> {
            useCase(locationId).toList()
        }

        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.observeForecastByLocationId(locationId) }
        confirmVerified(repository)
    }

    companion object {
        private val locationId = LocationDomainFixtures.generateLocationId()
        private val forecast = ForecastDomainFixtures.generateRandom()
        private val firstItem = ForecastDomainFixtures.generateRandom()
        private val secondItem = ForecastDomainFixtures.generateRandom()
        private val repositoryException = RuntimeException("Repository error")
    }
}