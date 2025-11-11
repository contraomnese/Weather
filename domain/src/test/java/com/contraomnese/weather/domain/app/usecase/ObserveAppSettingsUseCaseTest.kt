package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
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

private const val SETTINGS_SIZE = 2

class ObserveAppSettingsUseCaseTest {

    private lateinit var useCase: ObserveAppSettingsUseCase
    private val repository = mockk<AppSettingsRepository>()
    private val expectedFirstItem = AppSettings(
        language = Language("fr"),
        temperatureUnit = TemperatureUnit.Fahrenheit
    )
    private val expectedSecondItem = AppSettings(
        language = Language("de"),
        pressureUnit = PressureUnit.GPa
    )
    private val expectedException = RuntimeException("DB access error")

    @BeforeEach
    fun setUp() {
        useCase = ObserveAppSettingsUseCaseImpl(repository)
    }

    @Test
    fun `given repository returns flow with app settings`() = runTest {
        coEvery { repository.settings } returns flow {
            delay(200)
            emit(expectedFirstItem)
            delay(500)
            emit(expectedSecondItem)
        }

        val actualResult = useCase().toList()

        assertEquals(SETTINGS_SIZE, actualResult.size)
        assertNotNull(actualResult[0])
        assertNotNull(actualResult[1])
        assertEquals(expectedFirstItem, actualResult[0])
        assertEquals(expectedSecondItem, actualResult[1])
        coVerify(exactly = 1) { repository.settings }
        confirmVerified(repository)
    }

    @Test
    fun `given repository throws exception when invoke is called then flow throws error`() = runTest {
        coEvery { repository.settings } returns flow {
            throw expectedException
        }
        val actualException = assertThrows<RuntimeException> {
            useCase().toList()
        }

        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.settings }
        confirmVerified(repository)
    }
}