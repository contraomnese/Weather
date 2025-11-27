package com.contraomnese.weather.domain.app.usecase

import com.contraomnese.weather.domain.AppSettingsDomainFixtures
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
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

class ObserveAppSettingsUseCaseTest {

    private lateinit var useCase: ObserveAppSettingsUseCase
    private val repository = mockk<AppSettingsRepository>()

    @BeforeEach
    fun setUp() {
        useCase = ObserveAppSettingsUseCaseImpl(repository)
    }

    @Test
    fun `should return flow with correct item when invoke is called`() = runTest {

        val expectedData = settings

        coEvery { repository.observeSettings() } returns flowOf(expectedData)

        val actualData = useCase().first()

        assertNotNull(actualData)
        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.observeSettings() }
        confirmVerified(repository)
    }

    @Test
    fun `should return flow with correct multiple items when invoke is called`() = runTest {

        val expectedData = listOf(firstSettingsItem, secondSettingsItem)

        coEvery { repository.observeSettings() } returns flow {
            emit(firstSettingsItem)
            emit(secondSettingsItem)
        }

        val actualData = useCase().toList()

        assertEquals(expectedData.size, actualData.size)
        assertNotNull(actualData[0])
        assertNotNull(actualData[1])
        assertEquals(expectedData[0], actualData[0])
        assertEquals(expectedData[1], actualData[1])
        coVerify(exactly = 1) { repository.observeSettings() }
        confirmVerified(repository)
    }

    @Test
    fun `should throw exception when invoke is called`() = runTest {
        val expectedException = repositoryException

        coEvery { repository.observeSettings() } returns flow {
            throw expectedException
        }
        val actualException = assertFailsWith<RuntimeException> {
            useCase().toList()
        }

        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.observeSettings() }
        confirmVerified(repository)
    }

    companion object {
        private val settings = AppSettingsDomainFixtures.generateRandom()
        private val firstSettingsItem = AppSettingsDomainFixtures.generateRandom()
        private val secondSettingsItem = AppSettingsDomainFixtures.generateRandom()
        private val repositoryException = RuntimeException("Repository error")
    }
}