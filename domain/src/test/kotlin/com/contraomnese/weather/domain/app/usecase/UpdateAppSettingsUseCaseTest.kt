package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.AppSettingsDomainFixtures
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.assertIsFailure
import com.contraomnese.weather.domain.assertIsSuccess
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

    @BeforeEach
    fun setUp() {
        useCase = UpdateAppSettingsUseCaseImpl(repository)
    }

    @Test
    fun `should return success result with unit when invoke is called`() = runTest {
        val expectedData = Unit

        coEvery { repository.updateSettings(settings) } returns Result.success(expectedData)
        val actualResult = useCase(settings)

        val actualData = actualResult.assertIsSuccess()
        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.updateSettings(settings) }
        confirmVerified(repository)
    }

    @Test
    fun `should return failure result with exception invoke is called`() = runTest {

        val expectedException = repositoryException

        coEvery { repository.updateSettings(settings) } returns Result.failure(expectedException)
        val actualResult = useCase(settings)

        val actualException = actualResult.assertIsFailure()
        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.updateSettings(settings) }
        confirmVerified(repository)
    }

    companion object {
        private val settings = AppSettingsDomainFixtures.generate()
        private val repositoryException = RuntimeException("Repository error")
    }
}