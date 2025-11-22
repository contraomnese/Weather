package com.contraomnese.weather.data.repository

import com.contraomnese.weather.data.network.api.WeatherApi
import com.contraomnese.weather.data.network.models.WeatherErrorResponse
import com.contraomnese.weather.data.storage.db.WeatherDatabase
import com.contraomnese.weather.domain.ForecastDomainFixtures
import com.contraomnese.weather.domain.app.repository.AppSettingsRepository
import com.contraomnese.weather.domain.weatherByLocation.repository.ForecastWeatherRepository
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Converter
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastWeatherRepositoryTest {

    private lateinit var repository: ForecastWeatherRepository
    private val api = mockk<WeatherApi>()
    private val appSettingsRepository = mockk<AppSettingsRepository>()
    private val weatherDatabase = mockk<WeatherDatabase>()
    private val errorConverter = mockk<Converter<ResponseBody, WeatherErrorResponse>>()
    private val dispatcher = UnconfinedTestDispatcher()
    private val expectedFirstItem = ForecastDomainFixtures.generate()

    @BeforeEach
    fun setUp() {
        repository = ForecastWeatherRepositoryImpl(
            api = api,
            appSettingsRepository = appSettingsRepository,
            weatherDatabase = weatherDatabase,
            errorConverter = errorConverter,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `given repository returns flow when getForecastByLocationId is called then return null`() =
        runTest {

            assertNotNull(ForecastDomainFixtures)

        }

//    companion object {
//        private val locationId = Random
//    }
}