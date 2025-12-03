package com.contraomnese.weather.data.repository

import app.cash.turbine.test
import com.contraomnese.weather.data.FakeTransactionProvider
import com.contraomnese.weather.data.ForecastDataFixtures
import com.contraomnese.weather.data.ForecastLocationDataFixtures
import com.contraomnese.weather.data.MatchingLocationDataFixtures
import com.contraomnese.weather.data.mappers.forecast.toDomain
import com.contraomnese.weather.data.mappers.locations.toEntity
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.models.ForecastLocationNetwork
import com.contraomnese.weather.data.network.models.ForecastResponse
import com.contraomnese.weather.data.network.parsers.ApiParser
import com.contraomnese.weather.data.storage.db.WeatherAppDatabase
import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastDao
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastDailyEntity
import com.contraomnese.weather.data.storage.db.forecast.entities.ForecastLocationEntity
import com.contraomnese.weather.data.storage.db.locations.dao.LocationsDao
import com.contraomnese.weather.data.storage.db.locations.dto.ForecastData
import com.contraomnese.weather.domain.AppSettingsDomainFixtures
import com.contraomnese.weather.domain.ForecastDomainFixtures
import com.contraomnese.weather.domain.LocationDomainFixtures
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.assertIsFailure
import com.contraomnese.weather.domain.assertIsSuccess
import com.contraomnese.weather.domain.exceptions.DomainException
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastRepository
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Response
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(LogMockerExtension::class)
@ExtendWith(MapperMockerExtension::class)
class ForecastRepositoryTest {

    private lateinit var originalLocale: Locale
    private lateinit var repository: ForecastRepository
    private val refreshForecastLocks: MutableMap<Int, Mutex> = ConcurrentHashMap()

    private val network = mockk<WeatherApi>()
    private val appSettingsRepository = mockk<AppSettingsRepository>()
    private val storage = mockk<WeatherAppDatabase>(relaxed = true)
    private val locationsDao = mockk<LocationsDao>(relaxed = true)
    private val forecastDao = mockk<ForecastDao>(relaxed = true)
    private val apiParser = mockk<ApiParser>()
    private val dispatcher = UnconfinedTestDispatcher()
    private val transactionProvider = FakeTransactionProvider()

    @BeforeEach
    fun setUp() {
        every { storage.locationsDao() } returns locationsDao
        every { storage.forecastDao() } returns forecastDao

        repository = ForecastRepositoryImpl(
            api = network,
            appSettingsRepository = appSettingsRepository,
            database = storage,
            apiParser = apiParser,
            dispatcher = dispatcher,
            refreshForecastLocks = refreshForecastLocks,
            transactionProvider = transactionProvider
        )
    }

    @Test
    fun `should returns flow with real forecast when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedData = ForecastDomainFixtures.generateReal()
            val realForecastData = ForecastDataFixtures.generateReal()
            val realAppSettings = AppSettingsDomainFixtures.generateReal()

            coEvery { forecastDao.observeForecastBy(locationId) } returns flowOf(realForecastData)
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(realAppSettings)

            originalLocale = Locale.getDefault()

            try {
                Locale.setDefault(Locale(realAppSettings.language.value))
                repository.getForecastByLocationId(locationId).test {
                    val item = awaitItem()
                    assertEquals(expectedData, item)
                    awaitComplete()
                }
            } finally {
                Locale.setDefault(originalLocale)
            }
        }

    @Test
    fun `should returns flow with correct item when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedData = forecastFirstItem
            val expectedForecast = forecastDataFirstItem
            val expectedSettings = appSettingsFirstItem

            coEvery { forecastDao.observeForecastBy(locationId) } returns flowOf(expectedForecast)
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(expectedSettings)
            every { any<ForecastData>().toDomain(any<AppSettings>()) } returns expectedData

            repository.getForecastByLocationId(locationId).test {
                val item = awaitItem()
                assertEquals(expectedData, item)
                awaitComplete()
            }

            coVerify(exactly = 1) { forecastDao.observeForecastBy(locationId) }
            coVerify(exactly = 1) { appSettingsRepository.observeSettings() }

            coVerifyOrder {
                storage.forecastDao()
                forecastDao.observeForecastBy(locationId)
                appSettingsRepository.observeSettings()
            }

            confirmVerified(storage, appSettingsRepository)
        }

    @Test
    fun `should returns flow with correct null item when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedData = null

            coEvery { forecastDao.observeForecastBy(locationId) } returns flowOf(null)
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(appSettingsFirstItem)

            repository.getForecastByLocationId(locationId).test {
                val item = awaitItem()
                assertEquals(expectedData, item)
                awaitComplete()
            }

            verify(exactly = 0) { any<ForecastData>().toDomain(any<AppSettings>()) }
        }

    @Test
    fun `should returns flow with multiple correct item when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            coEvery { forecastDao.observeForecastBy(locationId) } returns flow {
                emit(forecastDataFirstItem)
                emit(forecastDataSecondItem)
            }
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(appSettingsFirstItem)
            every { forecastDataFirstItem.toDomain(appSettingsFirstItem) } returns forecastFirstItem
            every { forecastDataSecondItem.toDomain(appSettingsFirstItem) } returns forecastSecondItem

            repository.getForecastByLocationId(locationId).test {
                val item1 = awaitItem()
                assertEquals(forecastFirstItem, item1)

                val item2 = awaitItem()
                assertEquals(forecastSecondItem, item2)

                awaitComplete()
            }

            coVerify(exactly = 1) { forecastDao.observeForecastBy(locationId) }
            coVerify(exactly = 1) { appSettingsRepository.observeSettings() }
            verify(exactly = 2) { any<ForecastData>().toDomain(any<AppSettings>()) }

            coVerifyOrder {
                storage.forecastDao()
                forecastDao.observeForecastBy(locationId)
                appSettingsRepository.observeSettings()
                forecastDataFirstItem.toDomain(appSettingsFirstItem)
                forecastDataSecondItem.toDomain(appSettingsFirstItem)
            }

            confirmVerified(storage, appSettingsRepository)
        }

    @Test
    fun `should throw exception when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedException = mappingException

            coEvery { forecastDao.observeForecastBy(locationId) } returns flow { emit(forecastDataFirstItem) }
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(appSettingsFirstItem)
            every { any<ForecastData>().toDomain(any<AppSettings>()) } throws mappingException

            repository.getForecastByLocationId(locationId).test {
                val actualException = awaitError()
                assertEquals(expectedException.message, actualException.message)
            }

            verify(exactly = 1) { any<ForecastData>().toDomain(any<AppSettings>()) }
        }

    @Test
    fun `should returns success result with updated id when refreshForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedData = locationId
            val locationNameFallback = "Fallback City"
            val locationWithCity = matchingLocation.copy(city = locationNameFallback)
            val response = Response.success(forecastResponse)
            val forecastLocationId = 321L
            val forecastDailyId = 123L

            coEvery { locationsDao.getMatchingLocation(locationId) } returns locationWithCity
            coEvery { network.getForecastWeather(matchingLocation.toPoint()) } returns response
            every { apiParser.parseOrThrowError(response) } returns forecastResponse
            coEvery { locationsDao.insertForecastLocation(any<ForecastLocationEntity>()) } returns forecastLocationId
            coEvery { forecastDao.insertForecastDay(any<ForecastDailyEntity>()) } returns forecastDailyId

            val actualResult = repository.refreshForecastByLocationId(locationId)

            val actualData = actualResult.assertIsSuccess()

            assertEquals(expectedData, actualData)

            coVerify { locationsDao.getMatchingLocation(locationId) }
            coVerify(exactly = 1) { network.getForecastWeather(matchingLocation.toPoint()) }
            verify(exactly = 1) { apiParser.parseOrThrowError(response) }
            coVerify { locationsDao.deleteForecastLocation(locationWithCity.networkId) }
            coVerify { locationsDao.insertForecastLocation(match { it.name == "Fallback City" }) }
            coVerify { forecastDao.insertForecastCurrent(match { it.forecastLocationId == forecastLocationId.toInt() }) }
            coVerify { forecastDao.insertAlerts(match { it.first().forecastLocationId == forecastLocationId.toInt() }) }
            coVerify { forecastDao.insertForecastDay(match { it.forecastLocationId == forecastLocationId.toInt() }) }
            coVerify { forecastDao.insertDay(match { it.forecastDailyId == forecastDailyId.toInt() }) }
            coVerify { forecastDao.insertAstro(match { it.forecastDailyId == forecastDailyId.toInt() }) }
            coVerify { forecastDao.insertHourlyForecast(match { it.first().forecastDailyId == forecastDailyId.toInt() }) }

            coVerifyOrder {
                locationsDao.getMatchingLocation(locationId)
                network.getForecastWeather(matchingLocation.toPoint())
                apiParser.parseOrThrowError(response)
                locationsDao.deleteForecastLocation(locationWithCity.networkId)
                locationsDao.insertForecastLocation(match { it.name == "Fallback City" })
                forecastDao.insertForecastDay(match { it.forecastLocationId == forecastLocationId.toInt() })
                forecastDao.insertDay(match { it.forecastDailyId == forecastDailyId.toInt() })
            }
        }

    @Test
    fun `should use location name when city is null during update`() =
        runTest(dispatcher) {

            val locationNameFallback = "Fallback Name"
            val locationWithNullCity = matchingLocation.copy(city = null, name = locationNameFallback)
            val response = Response.success(forecastResponse)

            coEvery { locationsDao.getMatchingLocation(locationId) } returns locationWithNullCity
            coEvery { network.getForecastWeather(any()) } returns response
            every { apiParser.parseOrThrowError(response) } returns forecastResponse
            every { any<ForecastLocationNetwork>().toEntity(locationId) } returns forecastLocation

            repository.refreshForecastByLocationId(locationId)

            coVerify(exactly = 1) { locationsDao.insertForecastLocation(match { it.name == "Fallback Name" }) }
        }

    @Test
    fun `should mutex is lock when refreshForecastByLocationId is called`() =
        runTest {

            val expectedException = updateForecastMutexBlockedException

            val mutex = Mutex(true)
            refreshForecastLocks[locationId] = mutex

            val actualResult = repository.refreshForecastByLocationId(locationId)

            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            coVerify(exactly = 0) { locationsDao.getMatchingLocation(locationId) }

            confirmVerified(storage)
        }

    @Test
    fun `should update forecast not call when refreshForecastByLocationId is called and mutex is locked`() =
        runTest {

            val mutex = Mutex(true)
            refreshForecastLocks[locationId] = mutex

            repository.refreshForecastByLocationId(locationId)

            verify { network wasNot Called }
            verify { apiParser wasNot Called }
            verify { storage wasNot Called }
        }

    @Test
    fun `should mutex is unlock when refreshForecastByLocationId is called`() =
        runTest {

            coEvery { locationsDao.getMatchingLocation(locationId) } throws RuntimeException()

            repository.refreshForecastByLocationId(locationId)

            val mutex = refreshForecastLocks[locationId]
            assertNotNull(mutex)
            assertFalse(mutex!!.isLocked)
        }

    @Test
    fun `should return failure result with storage exception when matching location not found`() =
        runTest(dispatcher) {

            val expectedException = matchingLocationNotFoundException

            coEvery { locationsDao.getMatchingLocation(locationId) } throws storageException

            val actualResult = repository.refreshForecastByLocationId(locationId)
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            coVerify(exactly = 1) { locationsDao.getMatchingLocation(locationId) }
        }

    @Test
    fun `should return failure result with exception when forecast not found in network`() =
        runTest(dispatcher) {

            val expectedException = forecastNotFoundException

            coEvery { locationsDao.getMatchingLocation(locationId) } returns matchingLocation
            coEvery { network.getForecastWeather(matchingLocation.toPoint()) } throws storageException

            val actualResult = repository.refreshForecastByLocationId(locationId)
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            coVerify(exactly = 1) { network.getForecastWeather(matchingLocation.toPoint()) }
        }

    @Test
    fun `should return failure result with exception when forecast response can't parse`() =
        runTest(dispatcher) {

            val expectedException = mappingException
            val response = Response.success(forecastResponse)

            coEvery { locationsDao.getMatchingLocation(locationId) } returns matchingLocation
            coEvery { network.getForecastWeather(matchingLocation.toPoint()) } returns response
            every { apiParser.parseOrThrowError(response) } throws expectedException

            val actualResult = repository.refreshForecastByLocationId(locationId)
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)

            verify(exactly = 1) { apiParser.parseOrThrowError(response) }
        }

    @Test
    fun `should return failure result with mapper exception when forecast can't update in storage`() =
        runTest(dispatcher) {

            val expectedException = forecastNotMapException
            val response = Response.success(forecastResponse)

            coEvery { locationsDao.getMatchingLocation(any()) } returns matchingLocation
            coEvery { network.getForecastWeather(any()) } returns response
            every { apiParser.parseOrThrowError(any<Response<ForecastResponse>>()) } returns forecastResponse
            every { any<ForecastLocationNetwork>().toEntity(matchingLocation.networkId) } throws mappingException

            val actualResult = repository.refreshForecastByLocationId(locationId)
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            verify(exactly = 1) { any<ForecastLocationNetwork>().toEntity(matchingLocation.networkId) }
        }

    @Test
    fun `should return failure result with storage exception when forecast can't update in storage`() =
        runTest(dispatcher) {

            val expectedException = forecastNotUpdatedException
            val response = Response.success(forecastResponse)

            coEvery { locationsDao.getMatchingLocation(any()) } returns matchingLocation
            coEvery { network.getForecastWeather(any()) } returns response
            every { apiParser.parseOrThrowError(any<Response<ForecastResponse>>()) } returns forecastResponse
            coEvery { storage.forecastDao() } throws storageException

            val actualResult = repository.refreshForecastByLocationId(locationId)
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            coVerify(exactly = 1) { storage.forecastDao() }
        }


    companion object {
        private val locationId = LocationDomainFixtures.generateLocationId()
        private val forecastDataFirstItem = ForecastDataFixtures.generateRandomForecastData()
        private val forecastDataSecondItem = ForecastDataFixtures.generateRandomForecastData()
        private val appSettingsFirstItem = AppSettingsDomainFixtures.generateRandom()

        private val forecastFirstItem = ForecastDomainFixtures.generateRandom()
        private val forecastSecondItem = ForecastDomainFixtures.generateRandom()

        private val matchingLocation = MatchingLocationDataFixtures.generateRandom()
        private val forecastLocation = ForecastLocationDataFixtures.generateRandom()
        private val forecastResponse = ForecastDataFixtures.generateRandomForecastNetwork()

        private val mappingException = IllegalArgumentException("Mapping error")
        private val storageException = RuntimeException("Storage error")
        private val matchingLocationNotFoundException = DomainException.StorageError(null, storageException)
        private val forecastNotFoundException = DomainException.BadRequest(null, storageException)
        private val forecastNotMapException = DomainException.OperationFailed(null, mappingException)
        private val forecastNotUpdatedException = DomainException.OperationFailed(null, storageException)
        private val updateForecastMutexBlockedException = DomainException.OperationFailed(null, null)

    }
}