package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private const val FIRST_LOCATION_ID = 1
private const val FIRST_LOCATION_LATITUDE = 11.0
private const val FIRST_LOCATION_LONGITUDE = 22.0
private const val SECOND_LOCATION_ID = 2
private const val SECOND_LOCATION_LATITUDE = 22.0
private const val SECOND_LOCATION_LONGITUDE = 44.0

private const val LOCATIONS_SIZE = 2

class ObserveFavoritesUseCaseTest {

    private lateinit var useCase: ObserveFavoritesUseCase
    private val repository = mockk<LocationsRepository>()

    private val expectedFirstItem = Location.fromGeo(FIRST_LOCATION_ID, FIRST_LOCATION_LATITUDE, FIRST_LOCATION_LONGITUDE)
    private val expectedSecondItem = Location.fromGeo(SECOND_LOCATION_ID, SECOND_LOCATION_LATITUDE, SECOND_LOCATION_LONGITUDE)
    private val expectedDataOne = listOf(expectedFirstItem)
    private val expectedDataTwo = listOf(expectedFirstItem, expectedSecondItem)
    private val expectedException = RuntimeException("DB access error")

    @BeforeEach
    fun setUp() {
        useCase = ObserveFavoritesUseCaseImpl(repository)
    }

    @Test
    fun `given repository returns flow with list of favorite locations`() = runTest {
        coEvery { repository.observeFavorites() } returns flow {
            emit(expectedDataOne)
            delay(1000)
            emit(expectedDataTwo)
        }
        val actualResult = useCase().toList()

        assertEquals(LOCATIONS_SIZE, actualResult.size)
        assertEquals(expectedDataOne, actualResult[0])
        assertEquals(expectedDataTwo, actualResult[1])
        coVerify(exactly = 1) { repository.observeFavorites() }
        confirmVerified(repository)
    }

    @Test
    fun `given repository throws exception when invoke is called then flow throws error`() = runTest {
        coEvery { repository.observeFavorites() } returns flow {
            throw expectedException
        }
        val actualException = assertThrows<RuntimeException> {
            useCase().toList()
        }

        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.observeFavorites() }
        confirmVerified(repository)
    }

}