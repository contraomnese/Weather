package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.home.model.LocationDomainModel
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val FIRST_LOCATION_ID = "1"
private const val FIRST_LOCATION_NAME = "firstName"
private const val SECOND_LOCATION_ID = "2"
private const val SECOND_LOCATION_NAME = "secondName"

class GetLocationsUseCaseTest {

    private lateinit var useCase: GetLocationsUseCase
    private val repositoryMock = mockk<LocationsRepository>()
    private lateinit var coroutineContextProvider: CoroutineContextProvider
    private val expectedLocations = listOf(
        LocationDomainModel(
            id = FIRST_LOCATION_ID,
            name = FIRST_LOCATION_NAME
        ),
        LocationDomainModel(
            id = SECOND_LOCATION_ID,
            name = SECOND_LOCATION_NAME
        )
    )
    private val expectedEmptyLocations = emptyList<LocationDomainModel>()

    @BeforeEach
    fun setUp() {
        coroutineContextProvider = FakeCoroutineContextProvider
        useCase = GetLocationsUseCase(
            repository = repositoryMock,
            coroutineContextProvider = coroutineContextProvider
        )
    }

    @Test
    fun `test use case returns correct locations`() = runTest {
        coEvery { repositoryMock.getLocationsBy() } returns expectedLocations
        val actualResult = useCase.executeInBackground()
        assertEquals(expectedLocations, actualResult)
        assertEquals(expectedLocations.size, actualResult.size)
        assertEquals(expectedLocations[0], actualResult[0])
        assertEquals(expectedLocations[1], actualResult[1])
        verify { repositoryMock.getLocationsBy() }
    }

    @Test
    fun `test use case returns empty locations`() = runTest {
        coEvery { repositoryMock.getLocationsBy() } returns expectedEmptyLocations
        val actualResult = useCase.executeInBackground()
        assertEquals(expectedEmptyLocations, actualResult)
        verify { repositoryMock.getLocationsBy() }
    }

    @Test
    fun `test use case uses repository correctly`() = runTest {
        coEvery { repositoryMock.getLocationsBy() } returns expectedLocations
        useCase.executeInBackground()
        coVerify(exactly = 1) { repositoryMock.getLocationsBy() }
        confirmVerified(repositoryMock)
    }
}