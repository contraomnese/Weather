package com.contraomnese.weather.data.repository

import android.util.Log
import com.contraomnese.weather.data.ForecastDataFixtures
import com.contraomnese.weather.data.mappers.forecast.ForecastWeatherMapper
import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.models.WeatherErrorResponse
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.domain.ForecastDomainFixtures
import com.contraomnese.weather.domain.LocationDomainFixtures
import com.contraomnese.weather.domain.MockForecastReal
import com.contraomnese.weather.domain.app.model.AppSettings
import com.contraomnese.weather.domain.app.model.Language
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Converter
import java.util.Locale
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastWeatherRepositoryTest {

    private lateinit var originalLocale: Locale
    private lateinit var repository: ForecastWeatherRepository
    private val network = mockk<WeatherApi>()
    private val appSettingsRepository = mockk<AppSettingsRepository>()
    private val storage = mockk<WeatherDatabase>()
    private val errorConverter = mockk<Converter<ResponseBody, WeatherErrorResponse>>()
    private val dispatcher = UnconfinedTestDispatcher()
    private val mapper = spyk(ForecastWeatherMapper())

    @BeforeEach
    fun setUp() {
        mockkStatic(Log::class)
        justRun { Log.e(any(), any(), any()) }
        every { Log.d(any(), any()) } returns 0

        originalLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)

        repository = ForecastWeatherRepositoryImpl(
            api = network,
            appSettingsRepository = appSettingsRepository,
            weatherDatabase = storage,
            errorConverter = errorConverter,
            dispatcher = dispatcher,
            mapper = mapper
        )
    }

    @AfterEach
    fun tearDown() {
        Locale.setDefault(originalLocale)
        unmockkStatic(Log::class)
    }

    @Test
    fun `should returns flow with correct item when getForecastByLocationId is called`() =
        runTest(dispatcher) {

            val expectedData = item

            coEvery { storage.forecastDao().observeForecastBy(locationId) } returns flowOf(forecastDataFirstItem)
            coEvery { appSettingsRepository.observeSettings() } returns flowOf(appSettingsFirstItem)
//            every { mapper.toDomain(forecastDataFirstItem, appSettingsFirstItem) } returns expectedData

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

    companion object {
        private val locationId = LocationDomainFixtures.generateLocationId()
        private val forecastDataFirstItem = mockForecastDataReal
        private val forecastDataSecondItem = ForecastDataFixtures.generateRandom()
        private val appSettingsFirstItem = AppSettings(language = Language("ru"))

        private val item = MockForecastReal.get()
        private val firstItem = ForecastDomainFixtures.generate()
        private val secondItem = ForecastDomainFixtures.generate()

    }
}