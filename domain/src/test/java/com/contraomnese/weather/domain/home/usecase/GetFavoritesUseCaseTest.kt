package com.contraomnese.weather.domain.home.usecase

import android.database.sqlite.SQLiteException
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.utils.assertIsFailure
import com.contraomnese.weather.domain.utils.assertIsSuccess
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val FIRST_LOCATION_ID = 1
private const val FIRST_LOCATION_LATITUDE = 11.0
private const val FIRST_LOCATION_LONGITUDE = 22.0
private const val SECOND_LOCATION_ID = 2
private const val SECOND_LOCATION_LATITUDE = 22.0
private const val SECOND_LOCATION_LONGITUDE = 44.0

class GetFavoritesUseCaseTest {

    private lateinit var useCase: GetFavoritesUseCase
    private val repository = mockk<LocationsRepository>()

    private val expectedFirstItem = Location.fromGeo(FIRST_LOCATION_ID, FIRST_LOCATION_LATITUDE, FIRST_LOCATION_LONGITUDE)
    private val expectedFavoriteItem = Location.fromGeo(SECOND_LOCATION_ID, SECOND_LOCATION_LATITUDE, SECOND_LOCATION_LONGITUDE)
    private val expectedData = listOf(expectedFirstItem, expectedFavoriteItem)
    private val expectedException = SQLiteException("Database doesn't exist")

    @BeforeEach
    fun setUp() {
        useCase = GetFavoritesUseCaseImpl(repository)
    }

    @Test
    fun `given repository returns success result when invoke is called then returns favorite locations`() = runTest {
        coEvery { repository.getFavorites() } returns Result.success(expectedData)
        val actualResult = useCase()

        val actualData = actualResult.assertIsSuccess()
        assertEquals(expectedData.size, actualData.size)
        assertEquals(expectedData[0], actualData[0])
        assertEquals(expectedData[1], actualData[1])
        coVerify(exactly = 1) { repository.getFavorites() }
        confirmVerified(repository)
    }

    @Test
    fun `given repository returns failure result when invoke is called then throws exception`() = runTest {
        coEvery { repository.getFavorites() } returns Result.failure(expectedException)
        val actualResult = useCase()

        val actualException = actualResult.assertIsFailure()
        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.getFavorites() }
        confirmVerified(repository)
    }
}