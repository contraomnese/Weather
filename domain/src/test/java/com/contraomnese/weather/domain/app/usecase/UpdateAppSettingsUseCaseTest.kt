package com.contraomnese.weather.domain.app.usecase

import android.database.sqlite.SQLiteException
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.utils.assertIsFailure
import com.contraomnese.weather.domain.utils.assertIsSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateAppSettingsUseCaseTest {

    private lateinit var useCase: UpdateAppSettingsUseCase
    private val repository = mockk<AppSettingsRepository>()
    private val settings = AppSettings(
        language = Language("ru")
    )
    private val expectedData = Unit
    private val expectedException = SQLiteException("Database doesn't exist")

    @BeforeEach
    fun setUp() {
        useCase = UpdateAppSettingsUseCaseImpl(repository)
    }

    @Test
    fun `given repository returns success result when invoke is called then returns Unit`() = runTest {
        coEvery { repository.updateSettings(settings) } returns Result.success(Unit)
        val actualResult = useCase(settings)

        val actualData = actualResult.assertIsSuccess()
        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.updateSettings(settings) }
        confirmVerified(repository)
    }

    @Test
    fun `given repository returns failure result when invoke is called then throws exception`() = runTest {
        coEvery { repository.updateSettings(settings) } returns Result.failure(expectedException)
        val actualResult = useCase(settings)

        val actualException = actualResult.assertIsFailure()
        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.updateSettings(settings) }
        confirmVerified(repository)
    }
}