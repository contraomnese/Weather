package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.mappers.appSettings.AppSettingsMapper
import com.contraomnese.weather.data.storage.memory.api.AppSettingsStorage
import com.contraomnese.weather.data.storage.memory.models.AppSettingsEntity
import com.contraomnese.weather.data.stubs.AppSettingsStub
import com.contraomnese.weather.domain.app.model.PressureUnit
import com.contraomnese.weather.domain.app.model.TemperatureUnit
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.exceptions.DomainException
import com.contraomnese.weather.domain.utils.assertIsFailure
import com.contraomnese.weather.domain.utils.assertIsSuccess
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
    fun `given repository returns flow when observe is called then return correct item`() = runTest(dispatcher) {

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
    fun `given repository returns flow when observe settings is called then correctly maps multiple items`() =
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
    fun `given repository completes without emissions when observe settings is called`() = runTest(dispatcher) {

        coEvery { storage.observeSettings() } returns emptyFlow()

        val actualData = repository.observeSettings().toList()

        assertTrue(actualData.isEmpty())
        coVerify(exactly = 1) { storage.observeSettings() }
        confirmVerified(storage, mapper)
    }

    @Test
    fun `given repository returns flow when observe settings is called then throws exception`() = runTest(dispatcher) {

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
    fun `given repository returns flow when observe is called then throws mapping exception`() = runTest(dispatcher) {

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
    fun `given repository returns success result when get settings is called then returns correct settings`() =
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
    fun `given repository returns failure result when get settings is called then throws storage exception`() =
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
    fun `given repository returns failure result when get settings is called then throws mapping exception`() =
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
    fun `given repository returns success result when update settings is called then returns Unit`() =
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
    fun `given repository returns failure result when update settings is called then throws mapping exception`() =
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
    fun `given repository returns failure result when update setting is called then throws storage exception`() =
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
    fun `given repository rethrow errors when update is called`() = runTest(dispatcher) {
        every { mapper.toEntity(any()) } throws expectedError

        assertFailsWith<OutOfMemoryError> {
            repository.updateSettings(settings)
        }
    }

    companion object {
        private val settings = AppSettingsStub.generate()
        private val entity = AppSettingsStub.map(settings)
        private val mappingException = IllegalArgumentException()
        private val storageException = RuntimeException("Storage error")
        private val expectedError = OutOfMemoryError("boom")
        private val firstSettingsItem = AppSettingsStub.generate("ru", pressureUnit = PressureUnit.GPa)
        private val firstSettingsEntityItem = AppSettingsStub.map(firstSettingsItem)
        private val secondSettingsItem = AppSettingsStub.generate("fr", temperatureUnit = TemperatureUnit.Fahrenheit)
        private val secondSettingsEntityItem = AppSettingsStub.map(secondSettingsItem)
    }

}