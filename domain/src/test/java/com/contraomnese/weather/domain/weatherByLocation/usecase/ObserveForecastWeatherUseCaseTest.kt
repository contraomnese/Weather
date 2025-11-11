package com.contraomnese.weather.domain.weatherByLocation.usecase

import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNull

private const val FORECAST_SIZE = 2

class ObserveForecastWeatherUseCaseTest {

    private lateinit var useCase: ObserveForecastWeatherUseCase
    private val repository = mockk<ForecastWeatherRepository>()
    private val request = 1
    private val expectedFirstItem = ForecastTestData.randomForecast()
    private val expectedSecondItem = ForecastTestData.randomForecast()
    private val expectedException = RuntimeException("DB access error")

    @BeforeEach
    fun setUp() {
        useCase = ObserveForecastWeatherUseCaseImpl(repository)
    }

    @Test
    fun `given repository returns flow with list of forecast weather`() = runTest {
        coEvery { repository.observeBy(request) } returns flow {
            delay(200)
            emit(expectedFirstItem)
            delay(500)
            emit(expectedSecondItem)
        }
        val actualResult = useCase(request).toList()

        assertEquals(FORECAST_SIZE, actualResult.size)
        assertNotNull(actualResult[0])
        assertNotNull(actualResult[1])
        assertEquals(expectedFirstItem, actualResult[0])
        assertEquals(expectedSecondItem, actualResult[1])
        coVerify(exactly = 1) { repository.observeBy(request) }
        confirmVerified(repository)
    }

    @Test
    fun `given repository returns flow with list of null`() = runTest {
        coEvery { repository.observeBy(request) } returns flow {
            delay(200)
            emit(null)
            delay(500)
            emit(null)
        }
        val actualResult = useCase(request).toList()

        assertEquals(FORECAST_SIZE, actualResult.size)
        assertNull(actualResult[0])
        assertNull(actualResult[1])
        coVerify(exactly = 1) { repository.observeBy(request) }
        confirmVerified(repository)
    }

    @Test
    fun `given repository throws exception when invoke is called then flow throws error`() = runTest {
        coEvery { repository.observeBy(request) } returns flow {
            throw expectedException
        }
        val actualException = assertThrows<RuntimeException> {
            useCase(request).toList()
        }

        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.observeBy(request) }
        confirmVerified(repository)
    }
}