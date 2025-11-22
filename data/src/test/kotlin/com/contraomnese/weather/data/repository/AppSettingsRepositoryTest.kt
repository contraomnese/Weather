package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.AppSettingsDataFixtures
import com.contraomnese.weather.data.mappers.appSettings.AppSettingsMapper
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.domain.AppSettingsDomainFixtures
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.assertIsFailure
import com.contraomnese.weather.domain.assertIsSuccess
import com.contraomnese.weather.domain.exceptions.DomainException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class AppSettingsRepositoryTest {

    private lateinit var repository: AppSettingsRepository
    private val storage = mockk<AppSettingsStorage>()
    private val mapper = mockk<AppSettingsMapper>()
    private val dispatcher = UnconfinedTestDispatcher()
    private val request = settings

    @BeforeEach
    fun setUp() {
        repository = AppSettingsRepositoryImpl(storage, mapper, dispatcher)
    }

    @Test
    fun `should return flow with correct item when OBSERVE settings is called`() = runTest(dispatcher) {

        val expectedData = settings

        every { mapper.toDomain(entity) } returns settings
        coEvery { storage.observeSettings() } returns flowOf(entity)

        val actualData = repository.observeSettings().first()

        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { mapper.toDomain(entity) }
        coVerify(exactly = 1) { storage.observeSettings() }
        confirmVerified(storage, mapper)
    }

    @Test
    fun `should return flow with correct multiple items when OBSERVE settings is called`() =
        runTest(dispatcher) {

            val expectedData = listOf(firstSettingsItem, secondSettingsItem)

            every { mapper.toDomain(firstSettingsEntityItem) } returns firstSettingsItem
            every { mapper.toDomain(secondSettingsEntityItem) } returns secondSettingsItem
            coEvery { storage.observeSettings() } returns flowOf(
                firstSettingsEntityItem,
                secondSettingsEntityItem
            )

            val actualData = repository.observeSettings().toList()

            assertEquals(expectedData.size, actualData.size)
            assertEquals(expectedData[0], actualData[0])
            assertEquals(expectedData[1], actualData[1])
            coVerify(exactly = expectedData.size) { mapper.toDomain(any<AppSettingsEntity>()) }
            coVerify(exactly = 1) { storage.observeSettings() }
            confirmVerified(storage, mapper)
        }

    @Test
    fun `should completes without emissions when OBSERVE settings is called`() = runTest(dispatcher) {

        coEvery { storage.observeSettings() } returns emptyFlow()

        val actualData = repository.observeSettings().toList()

        assertTrue(actualData.isEmpty())
        coVerify(exactly = 1) { storage.observeSettings() }
        confirmVerified(storage, mapper)
    }

    @Test
    fun `should return flow with throw storage exception when OBSERVE settings is called`() = runTest(dispatcher) {

        val expectedException = storageException

        coEvery { storage.observeSettings() } returns flow {
            throw expectedException
        }

        val actualException = assertFailsWith<Exception> {
            repository.observeSettings().toList()
        }

        assertEquals(expectedException, actualException.cause ?: actualException)
        assertEquals(expectedException.message, actualException.cause?.message ?: actualException.message)

        verify(exactly = 0) { mapper.toDomain(entity) }
        coVerify(exactly = 1) { storage.observeSettings() }
        confirmVerified(storage, mapper)
    }

    @Test
    fun `should return flow with throw mapping exception when OBSERVE settings is called`() = runTest(dispatcher) {

        val expectedException = mappingException

        every { mapper.toDomain(entity) } throws expectedException
        coEvery { storage.observeSettings() } returns flow { emit(entity) }

        val actualException = assertFailsWith<Exception> {
            repository.observeSettings().toList()
        }

        assertEquals(expectedException, actualException.cause ?: actualException)
        assertEquals(expectedException.message, actualException.cause?.message ?: actualException.message)

        verify(exactly = 1) { mapper.toDomain(entity) }
        coVerify(exactly = 1) { storage.observeSettings() }
        confirmVerified(storage, mapper)
    }

    @Test
    fun `should return success result with correct item when GET settings is called`() =
        runTest(dispatcher) {

            val expectedData = settings
            val slot = slot<AppSettingsEntity>()

            coEvery { storage.getSettings() } returns entity
            every { mapper.toDomain(capture(slot)) } returns settings

            val actualResult = repository.getSettings()

            val actualData = actualResult.assertIsSuccess()
            assertEquals(expectedData, actualData)

            val captured = slot.captured
            assertEquals(entity.language, captured.language)
            assertEquals(entity.speedUnit, captured.speedUnit)
            assertEquals(entity.temperatureUnit, captured.temperatureUnit)
            assertEquals(entity.precipitationUnit, captured.precipitationUnit)
            assertEquals(entity.pressureUnit, captured.pressureUnit)

            coVerifyOrder {
                storage.getSettings()
                mapper.toDomain(any())
            }

            confirmVerified(storage, mapper)
        }

    @Test
    fun `should return failure result with storage exception when GET settings is called`() =
        runTest(dispatcher) {

            val expectedException = DomainException.StorageError(null, storageException)

            coEvery { storage.getSettings() } throws storageException

            val actualResult = repository.getSettings()
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            coVerify(exactly = 1) { storage.getSettings() }
            verify(exactly = 0) { mapper.toDomain(any()) }
            confirmVerified(storage, mapper)
        }

    @Test
    fun `should return failure result with mapping exception when GET settings is called`() =
        runTest(dispatcher) {

            val expectedException = DomainException.OperationFailed(null, mappingException)

            coEvery { storage.getSettings() } returns entity
            every { mapper.toDomain(entity) } throws mappingException

            val actualResult = repository.getSettings()
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            coVerify(exactly = 1) { storage.getSettings() }
            verify(exactly = 1) { mapper.toDomain(entity) }
            confirmVerified(storage, mapper)
        }

    @Test
    fun `should return success result with unit when GET settings is called`() =
        runTest(dispatcher) {

            val expectedData = Unit
            val slot = slot<AppSettingsEntity>()

            every { mapper.toEntity(request) } returns entity
            coEvery { storage.saveSettings(capture(slot)) } returns Unit

            val actualResult = repository.updateSettings(request)

            val actualData = actualResult.assertIsSuccess()
            assertEquals(expectedData, actualData)

            val captured = slot.captured
            assertEquals(entity.language, captured.language)
            assertEquals(entity.speedUnit, captured.speedUnit)
            assertEquals(entity.temperatureUnit, captured.temperatureUnit)
            assertEquals(entity.precipitationUnit, captured.precipitationUnit)
            assertEquals(entity.pressureUnit, captured.pressureUnit)

            coVerifyOrder {
                mapper.toEntity(request)
                storage.saveSettings(any())
            }

            confirmVerified(storage, mapper)
        }

    @Test
    fun `should return failure result with mapping exception when UPDATE settings is called`() =
        runTest(dispatcher) {

            val expectedException = DomainException.OperationFailed(null, mappingException)

            every { mapper.toEntity(request) } throws mappingException

            val actualResult = repository.updateSettings(request)
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            coVerify(exactly = 1) { mapper.toEntity(request) }
            coVerify(exactly = 0) { storage.saveSettings(any()) }
            confirmVerified(storage, mapper)
        }

    @Test
    fun `should return failure result with storage exception when UPDATE settings is called`() =
        runTest(dispatcher) {

            val expectedException = DomainException.StorageError(null, storageException)

            every { mapper.toEntity(request) } returns entity
            coEvery { storage.saveSettings(entity) } throws storageException

            val actualResult = repository.updateSettings(request)
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            verify(exactly = 1) { mapper.toEntity(request) }
            coVerify(exactly = 1) { storage.saveSettings(entity) }
            confirmVerified(storage, mapper)
        }

    @Test
    fun `should rethrow fatal errors when UPDATE settings is called`() = runTest(dispatcher) {
        every { mapper.toEntity(any()) } throws fatalError

        assertFailsWith<OutOfMemoryError> {
            repository.updateSettings(settings)
        }
    }

    companion object {
        private val settings = AppSettingsDomainFixtures.generate()
        private val entity = AppSettingsDataFixtures.map(settings)
        private val mappingException = IllegalArgumentException()
        private val storageException = RuntimeException("Storage error")
        private val fatalError = OutOfMemoryError("boom")
        private val firstSettingsItem = AppSettingsDomainFixtures.generate()
        private val firstSettingsEntityItem = AppSettingsDataFixtures.map(firstSettingsItem)
        private val secondSettingsItem = AppSettingsDomainFixtures.generate()
        private val secondSettingsEntityItem = AppSettingsDataFixtures.map(secondSettingsItem)
    }

}