package com.contraomnese.weather.domain.weatherByLocation.usecase

import android.database.sqlite.SQLiteException
import com.contraomnese.weather.domain.utils.assertIsFailure
import com.contraomnese.weather.domain.utils.assertIsSuccess
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
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
    private val repository = mockk<ForecastWeatherRepository>()
    private val request = 1
    private val expectedData = 1
    private val expectedException = SQLiteException("Database doesn't exist")

    @BeforeEach
    fun setUp() {
        useCase = UpdateForecastWeatherUseCaseImpl(repository)
    }

    @Test
    fun `given repository returns success result when invoke is called then returns updated location id`() = runTest {
        coEvery { repository.refreshForecastByLocationId(request) } returns Result.success(expectedData)
        val actualResult = useCase(request)

        val actualData = actualResult.assertIsSuccess()
        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.refreshForecastByLocationId(request) }
        confirmVerified(repository)
    }

    @Test
    fun `given repository returns failure result when invoke is called then throws exception`() = runTest {
        coEvery { repository.refreshForecastByLocationId(request) } returns Result.failure(expectedException)
        val actualResult = useCase(request)

        val actualException = actualResult.assertIsFailure()
        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.refreshForecastByLocationId(request) }
        confirmVerified(repository)
    }

}