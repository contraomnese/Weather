package com.contraomnese.weather.data.repository

import android.util.Log
import com.contraomnese.weather.data.ForecastDataFixtures
import com.contraomnese.weather.data.MatchingLocationDataFixtures
import com.contraomnese.weather.data.mappers.forecast.ForecastWeatherMapper
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.models.ForecastResponse
import com.contraomnese.weather.data.network.models.WeatherErrorResponse
import com.contraomnese.weather.data.network.parsers.parseOrThrowError
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.data.storage.db.forecast.dao.ForecastData
import com.contraomnese.weather.domain.ForecastDomainFixtures
import com.contraomnese.weather.domain.LocationDomainFixtures
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.assertIsFailure
import com.contraomnese.weather.domain.assertIsSuccess
import com.contraomnese.weather.domain.exceptions.DomainException
import com.contraomnese.weather.domain.exceptions.logPrefix
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Converter
import retrofit2.Response
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastWeatherRepositoryTest {

    private lateinit var originalLocale: Locale
    private lateinit var repository: ForecastWeatherRepository
    private val network = mockk<WeatherApi>()
    private val appSettingsRepository = mockk<AppSettingsRepository>()
    private val storage = mockk<WeatherDatabase>()
    private val errorConverter = mockk<Converter<ResponseBody, WeatherErrorResponse>>()
    private val dispatcher = UnconfinedTestDispatcher()
    private val mapper = mockk<ForecastWeatherMapper>()
    private val updateMutex: MutableMap<Int, Mutex> = ConcurrentHashMap()

    @BeforeEach
    fun setUp() {
        mockkStatic(Log::class)
        justRun { Log.e(any(), any(), any()) }
        every { Log.d(any(), any()) } returns 0

        mockkStatic("com.contraomnese.weather.data.network.parsers.WeatherApiParserKt")
        mockkStatic("com.contraomnese.weather.domain.exceptions.DomainExceptionKt")

        originalLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        repository = ForecastWeatherRepositoryImpl(
            api = network,
            appSettingsRepository = appSettingsRepository,
            weatherDatabase = storage,
            errorConverter = errorConverter,
            dispatcher = dispatcher,
            mapper = mapper,
            updateMutex = updateMutex
        )
    }

    @AfterEach
    fun tearDown() {
        Locale.setDefault(originalLocale)
        unmockkStatic(Log::class)
        unmockkStatic("com.contraomnese.weather.data.network.parsers.WeatherApiParserKt")
        unmockkStatic("com.contraomnese.weather.domain.exceptions.DomainExceptionKt")
    }

    @Test
    fun `should returns flow with correct item when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedData = forecastFirstItem

            coEvery { storage.forecastDao().observeForecastBy(locationId) } returns flowOf(forecastDataFirstItem)
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(appSettingsFirstItem)
            every { mapper.toDomain(forecastDataFirstItem, appSettingsFirstItem) } returns expectedData

            val actualData = repository.getForecastByLocationId(locationId).first()

            assertEquals(expectedData, actualData)

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

            val actualData = repository.getForecastByLocationId(locationId).first()

            assertEquals(expectedData, actualData)

            coVerify(exactly = 1) { storage.forecastDao().observeForecastBy(locationId) }
            coVerify(exactly = 1) { appSettingsRepository.observeSettings() }
            verify(exactly = 0) { mapper.toDomain(any<ForecastData>(), appSettingsFirstItem) }

            coVerifyOrder {
                storage.forecastDao().observeForecastBy(locationId)
                appSettingsRepository.observeSettings()
            }

            confirmVerified(storage, appSettingsRepository)
        }

    @Test
    fun `should returns flow with multiple correct item when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedData = listOf(forecastFirstItem, forecastSecondItem)

            coEvery { storage.forecastDao().observeForecastBy(locationId) } returns flow {
                emit(forecastDataFirstItem)
                delay(1)
                emit(forecastDataSecondItem)
            }
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(appSettingsFirstItem)
            every { mapper.toDomain(forecastDataFirstItem, appSettingsFirstItem) } returns forecastFirstItem
            every { mapper.toDomain(forecastDataSecondItem, appSettingsFirstItem) } returns forecastSecondItem

            val actualData = repository.getForecastByLocationId(locationId).toList()

            assertEquals(expectedData.size, actualData.size)
            assertEquals(expectedData[0], actualData[0])
            assertEquals(expectedData[1], actualData[1])

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

            val actualException = assertFailsWith<IllegalArgumentException> {
                repository.getForecastByLocationId(locationId).first()
            }

            assertEquals(expectedException.message, actualException.message)

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
    fun `should returns success result with updated id when updateForecast is called`() =
        runTest(dispatcher) {

            val expectedData = locationId

            coEvery { storage.matchingLocationsDao().getLocation(locationId) } returns matchingLocation
            coEvery { network.getForecastWeather(matchingLocation.toPoint()) } returns Response.success(forecastResponse)

            every { any<Response<ForecastResponse>>().parseOrThrowError(errorConverter) } returns forecastResponse
            coEvery {
                storage.forecastDao().updateForecastForLocation(
                    locationId = matchingLocation.networkId,
                    locationName = matchingLocation.city ?: matchingLocation.name,
                    forecastResponse
                )
            } returns Unit

            val actualResult = repository.refreshForecastByLocationId(locationId)

            val actualData = actualResult.assertIsSuccess()

            assertEquals(expectedData, actualData)

            coVerify(exactly = 1) { storage.matchingLocationsDao().getLocation(locationId) }
            coVerify(exactly = 1) { network.getForecastWeather(matchingLocation.toPoint()) }
            verify(exactly = 1) { any<Response<ForecastResponse>>().parseOrThrowError(errorConverter) }
            coVerify(exactly = 1) {
                storage.forecastDao().updateForecastForLocation(
                    locationId = matchingLocation.networkId,
                    locationName = matchingLocation.city ?: matchingLocation.name,
                    forecastResponse
                )
            }

            coVerifyOrder {
                storage.matchingLocationsDao().getLocation(locationId)
                network.getForecastWeather(matchingLocation.toPoint())
                any<Response<ForecastResponse>>().parseOrThrowError(errorConverter)
                storage.forecastDao().updateForecastForLocation(
                    locationId = matchingLocation.networkId,
                    locationName = matchingLocation.city ?: matchingLocation.name,
                    forecastResponse
                )
            }

            confirmVerified(storage, network, errorConverter)
        }

    @Test
    fun `should returns failure result when updateForecast is called and mutex is locked`() =
        runTest {

            val expectedException = mutexBlockedException

            every { repository.logPrefix(any<String>()) } returns expectedException.message!!

            val mutex = Mutex(true)
            updateMutex[locationId] = mutex

            val actualResult = repository.refreshForecastByLocationId(locationId)

            val actualException = actualResult.assertIsFailure()

            assertEquals(expectedException::class.java, actualException::class.java)
            assertEquals(expectedException.err, actualException.cause)
            assertEquals(expectedException.message, actualException.message)

            coVerify(exactly = 0) { storage.matchingLocationsDao().getLocation(locationId) }

            confirmVerified(storage)
        }

    @Test
    fun `should mutex is unlock when updateForecast is called`() =
        runTest {

            coEvery { storage.matchingLocationsDao().getLocation(locationId) } throws RuntimeException()

            repository.refreshForecastByLocationId(locationId)

            val mutex = updateMutex[locationId]
            assertNotNull(mutex)
            assertFalse(mutex!!.isLocked)
        }


    companion object {
        private val locationId = LocationDomainFixtures.generateLocationId()
        private val forecastDataFirstItem = ForecastDataFixtures.generateReal()
        private val forecastDataSecondItem = ForecastDataFixtures.generateRandomForecastData()
        private val appSettingsFirstItem = AppSettings(language = Language("ru"))

        private val forecastFirstItem = ForecastDomainFixtures.generateRandom()
        private val forecastSecondItem = ForecastDomainFixtures.generateRandom()
        private val firstItem = ForecastDomainFixtures.generateRandom()
        private val secondItem = ForecastDomainFixtures.generateRandom()

        private val matchingLocation = MatchingLocationDataFixtures.generateRandom()
        private val forecastResponse = ForecastDataFixtures.generateRandomForecastNetwork()

        private val mappingException = IllegalArgumentException("Can't convert entity")
        private val mutexBlockedException = DomainException.OperationFailed("Refreshing already launch for this location id", null)

    }
}