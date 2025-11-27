package com.contraomnese.weather.data.repository

import app.cash.turbine.test
import com.contraomnese.weather.data.ForecastDataFixtures
import com.contraomnese.weather.data.MatchingLocationDataFixtures
import com.contraomnese.weather.data.mappers.forecast.ForecastWeatherMapper
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.parsers.ApiParser
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastData
import com.contraomnese.weather.domain.AppSettingsDomainFixtures
import com.contraomnese.weather.domain.ForecastDomainFixtures
import com.contraomnese.weather.domain.LocationDomainFixtures
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.assertIsFailure
import com.contraomnese.weather.domain.assertIsSuccess
import com.contraomnese.weather.domain.exceptions.DomainException
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
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
class ForecastWeatherRepositoryTest {

    private lateinit var originalLocale: Locale
    private lateinit var repository: ForecastWeatherRepository
    private val updateMutex: MutableMap<Int, Mutex> = ConcurrentHashMap()

    private val network = mockk<WeatherApi>()
    private val appSettingsRepository = mockk<AppSettingsRepository>()
    private val storage = mockk<WeatherDatabase>()
    private val apiParser = mockk<ApiParser>()
    private val dispatcher = UnconfinedTestDispatcher()
    private val mapper = mockk<ForecastWeatherMapper>()
    private val realMapper = spyk(ForecastWeatherMapper())

    @BeforeEach
    fun setUp() {
        repository = ForecastWeatherRepositoryImpl(
            api = network,
            appSettingsRepository = appSettingsRepository,
            weatherDatabase = storage,
            apiParser = apiParser,
            dispatcher = dispatcher,
            mapper = mapper,
            updateMutex = updateMutex
        )
    }

    @Test
    fun `should returns flow with real forecast when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedData = ForecastDomainFixtures.generateReal()
            val realForecastData = ForecastDataFixtures.generateReal()
            val realAppSettings = AppSettingsDomainFixtures.generateReal()

            val repoWithRealMapper = ForecastWeatherRepositoryImpl(
                api = network,
                appSettingsRepository = appSettingsRepository,
                weatherDatabase = storage,
                apiParser = apiParser,
                dispatcher = dispatcher,
                mapper = realMapper,
                updateMutex = updateMutex
            )

            coEvery { storage.forecastDao().observeForecastBy(locationId) } returns flowOf(realForecastData)
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(realAppSettings)

            originalLocale = Locale.getDefault()

            try {
                Locale.setDefault(Locale(realAppSettings.language.value))
                repoWithRealMapper.getForecastByLocationId(locationId).test {
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

            coEvery { storage.forecastDao().observeForecastBy(locationId) } returns flowOf(forecastDataFirstItem)
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(appSettingsFirstItem)
            every { mapper.toDomain(forecastDataFirstItem, appSettingsFirstItem) } returns expectedData

            repository.getForecastByLocationId(locationId).test {
                val item = awaitItem()
                assertEquals(expectedData, item)
                awaitComplete()
            }

            coVerify(exactly = 1) { storage.forecastDao().observeForecastBy(locationId) }
            coVerify(exactly = 1) { appSettingsRepository.observeSettings() }
            verify(exactly = 1) { mapper.toDomain(forecastDataFirstItem, appSettingsFirstItem) }

            coVerifyOrder {
                storage.forecastDao().observeForecastBy(locationId)
                appSettingsRepository.observeSettings()
                mapper.toDomain(forecastDataFirstItem, appSettingsFirstItem)
            }

            confirmVerified(storage, appSettingsRepository)
        }

    @Test
    fun `should returns flow with correct null item when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedData = null

            coEvery { storage.forecastDao().observeForecastBy(locationId) } returns flowOf(null)
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(appSettingsFirstItem)

            repository.getForecastByLocationId(locationId).test {
                val item = awaitItem()
                assertEquals(expectedData, item)
                awaitComplete()
            }

            verify(exactly = 0) { mapper.toDomain(any<ForecastData>(), appSettingsFirstItem) }
        }

    @Test
    fun `should returns flow with multiple correct item when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            coEvery { storage.forecastDao().observeForecastBy(locationId) } returns flow {
                emit(forecastDataFirstItem)
                emit(forecastDataSecondItem)
            }
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(appSettingsFirstItem)
            every { mapper.toDomain(forecastDataFirstItem, appSettingsFirstItem) } returns forecastFirstItem
            every { mapper.toDomain(forecastDataSecondItem, appSettingsFirstItem) } returns forecastSecondItem

            repository.getForecastByLocationId(locationId).test {
                val item1 = awaitItem()
                assertEquals(forecastFirstItem, item1)

                val item2 = awaitItem()
                assertEquals(forecastSecondItem, item2)

                awaitComplete()
            }

            coVerify(exactly = 1) { storage.forecastDao().observeForecastBy(locationId) }
            coVerify(exactly = 1) { appSettingsRepository.observeSettings() }
            verify(exactly = 2) { mapper.toDomain(any<ForecastData>(), appSettingsFirstItem) }

            coVerifyOrder {
                storage.forecastDao().observeForecastBy(locationId)
                appSettingsRepository.observeSettings()
                mapper.toDomain(forecastDataFirstItem, appSettingsFirstItem)
                mapper.toDomain(forecastDataSecondItem, appSettingsFirstItem)
            }

            confirmVerified(storage, appSettingsRepository)
        }

    @Test
    fun `should throw exception when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedException = mappingException

            coEvery { storage.forecastDao().observeForecastBy(locationId) } returns flow { emit(forecastDataFirstItem) }
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(appSettingsFirstItem)
            every { mapper.toDomain(forecastDataFirstItem, appSettingsFirstItem) } throws expectedException

            repository.getForecastByLocationId(locationId).test {
                val actualException = awaitError()
                assertEquals(expectedException.message, actualException.message)
            }

            verify(exactly = 1) { mapper.toDomain(forecastDataFirstItem, appSettingsFirstItem) }
        }

    @Test
    fun `should returns success result with updated id when refreshForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedData = locationId
            val locationNameFallback = "Fallback City"
            val locationWithCity = matchingLocation.copy(city = locationNameFallback)
            val response = Response.success(forecastResponse)

            coEvery { storage.matchingLocationsDao().getLocation(locationId) } returns locationWithCity
            coEvery { network.getForecastWeather(matchingLocation.toPoint()) } returns response
            every { apiParser.parseOrThrowError(response) } returns forecastResponse
            coEvery {
                storage.forecastDao().updateForecastForLocation(
                    locationId = matchingLocation.networkId,
                    locationName = locationNameFallback,
                    forecastResponse
                )
            } returns Unit

            val actualResult = repository.refreshForecastByLocationId(locationId)

            val actualData = actualResult.assertIsSuccess()

            assertEquals(expectedData, actualData)

            coVerify(exactly = 1) { storage.matchingLocationsDao().getLocation(locationId) }
            coVerify(exactly = 1) { network.getForecastWeather(matchingLocation.toPoint()) }
            verify(exactly = 1) { apiParser.parseOrThrowError(response) }
            coVerify(exactly = 1) {
                storage.forecastDao().updateForecastForLocation(
                    locationId = matchingLocation.networkId,
                    locationName = locationNameFallback,
                    forecastResponse
                )
            }

            coVerifyOrder {
                storage.matchingLocationsDao().getLocation(locationId)
                network.getForecastWeather(matchingLocation.toPoint())
                apiParser.parseOrThrowError(response)
                storage.forecastDao().updateForecastForLocation(
                    locationId = matchingLocation.networkId,
                    locationName = locationNameFallback,
                    forecastResponse
                )
            }

            confirmVerified(storage, network)
        }

    @Test
    fun `should use location name when city is null during update`() =
        runTest(dispatcher) {

            val locationNameFallback = "Fallback Name"
            val locationWithNullCity = matchingLocation.copy(city = null, name = locationNameFallback)
            val response = Response.success(forecastResponse)

            coEvery { storage.matchingLocationsDao().getLocation(locationId) } returns locationWithNullCity
            coEvery { network.getForecastWeather(any()) } returns response
            every { apiParser.parseOrThrowError(response) } returns forecastResponse
            coEvery { storage.forecastDao().updateForecastForLocation(any(), any(), any()) } returns Unit

            repository.refreshForecastByLocationId(locationId)

            coVerify(exactly = 1) {
                storage.forecastDao().updateForecastForLocation(
                    locationId = any(),
                    locationName = locationNameFallback,
                    forecastResponse = any()
                )
            }
        }

    @Test
    fun `should mutex is lock when refreshForecastByLocationId is called`() =
        runTest {

            val expectedException = updateForecastMutexBlockedException

            val mutex = Mutex(true)
            updateMutex[locationId] = mutex

            val actualResult = repository.refreshForecastByLocationId(locationId)

            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            coVerify(exactly = 0) { storage.matchingLocationsDao().getLocation(locationId) }

            confirmVerified(storage)
        }

    @Test
    fun `should update forecast not call when refreshForecastByLocationId is called and mutex is locked`() =
        runTest {

            val mutex = Mutex(true)
            updateMutex[locationId] = mutex

            repository.refreshForecastByLocationId(locationId)

            verify { network wasNot Called }
            verify { apiParser wasNot Called }
            verify { storage wasNot Called }
        }

    @Test
    fun `should mutex is unlock when refreshForecastByLocationId is called`() =
        runTest {

            coEvery { storage.matchingLocationsDao().getLocation(locationId) } throws RuntimeException()

            repository.refreshForecastByLocationId(locationId)

            val mutex = updateMutex[locationId]
            assertNotNull(mutex)
            assertFalse(mutex!!.isLocked)
        }

    @Test
    fun `should return failure result with storage exception when matching location not found`() =
        runTest(dispatcher) {

            val expectedException = matchingLocationNotFoundException

            coEvery { storage.matchingLocationsDao().getLocation(locationId) } throws storageException

            val actualResult = repository.refreshForecastByLocationId(locationId)
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            coVerify(exactly = 1) { storage.matchingLocationsDao().getLocation(locationId) }
        }

    @Test
    fun `should return failure result with exception when forecast not found in network`() =
        runTest(dispatcher) {

            val expectedException = forecastNotFoundException

            coEvery { storage.matchingLocationsDao().getLocation(locationId) } returns matchingLocation
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

            coEvery { storage.matchingLocationsDao().getLocation(locationId) } returns matchingLocation
            coEvery { network.getForecastWeather(matchingLocation.toPoint()) } returns response
            every { apiParser.parseOrThrowError(response) } throws expectedException

            val actualResult = repository.refreshForecastByLocationId(locationId)
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)

            verify(exactly = 1) { apiParser.parseOrThrowError(response) }
        }

    @Test
    fun `should return failure result with storage exception when forecast can't update in storage`() =
        runTest(dispatcher) {

            val expectedException = forecastNotUpdatedException
            val response = Response.success(forecastResponse)
            coEvery { storage.matchingLocationsDao().getLocation(any()) } returns matchingLocation
            coEvery { network.getForecastWeather(any()) } returns response
            every { apiParser.parseOrThrowError(response) } returns forecastResponse
            coEvery { storage.forecastDao().updateForecastForLocation(any(), any(), any()) } throws storageException

            val actualResult = repository.refreshForecastByLocationId(locationId)
            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)

            coVerify(exactly = 1) { storage.forecastDao().updateForecastForLocation(any(), any(), any()) }
        }


    companion object {
        private val locationId = LocationDomainFixtures.generateLocationId()
        private val forecastDataFirstItem = ForecastDataFixtures.generateRandomForecastData()
        private val forecastDataSecondItem = ForecastDataFixtures.generateRandomForecastData()
        private val appSettingsFirstItem = AppSettingsDomainFixtures.generateRandom()

        private val forecastFirstItem = ForecastDomainFixtures.generateRandom()
        private val forecastSecondItem = ForecastDomainFixtures.generateRandom()

        private val matchingLocation = MatchingLocationDataFixtures.generateRandom()
        private val forecastResponse = ForecastDataFixtures.generateRandomForecastNetwork()

        private val mappingException = IllegalArgumentException("Mapping error")
        private val storageException = RuntimeException("Storage error")
        private val matchingLocationNotFoundException = DomainException.StorageError(null, storageException)
        private val forecastNotFoundException = DomainException.BadRequest(null, storageException)
        private val forecastNotUpdatedException = DomainException.OperationFailed(null, storageException)
        private val updateForecastMutexBlockedException = DomainException.OperationFailed(null, null)

    }
}