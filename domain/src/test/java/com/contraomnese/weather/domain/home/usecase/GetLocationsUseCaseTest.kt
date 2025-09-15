package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.home.model.MatchingLocationDomainModel
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


private const val SEARCH_QUERY = "example"
private const val FIRST_LOCATION_ID = 1
private const val FIRST_LOCATION_NAME = "exampleFirstName"
private const val FIRST_LOCATION_COUNTRY_NAME = "firstCountryName"
private const val SECOND_LOCATION_ID = 2
private const val SECOND_LOCATION_NAME = "exampleSecondName"
private const val SECOND_LOCATION_COUNTRY_NAME = "secondCountryName"

class GetLocationsUseCaseTest {

    private lateinit var useCase: GetLocationsUseCase
    private val repositoryMock = mockk<LocationsRepository>()
    private lateinit var coroutineContextProvider: CoroutineContextProvider
    private val expectedLocations = listOf(
        MatchingLocationDomainModel(
            id = FIRST_LOCATION_ID,
            name = FIRST_LOCATION_NAME,
            countryName = FIRST_LOCATION_COUNTRY_NAME
        ),
        MatchingLocationDomainModel(
            id = SECOND_LOCATION_ID,
            name = SECOND_LOCATION_NAME,
            countryName = SECOND_LOCATION_COUNTRY_NAME
        )
    )
    private val expectedEmptyLocations = emptyList<MatchingLocationDomainModel>()

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
        coEvery { repositoryMock.getLocationsBy(SEARCH_QUERY) } returns expectedLocations
        val actualResult = useCase.executeInBackground(SEARCH_QUERY)
        assertEquals(expectedLocations, actualResult)
        assertEquals(expectedLocations.size, actualResult.size)
        assertEquals(expectedLocations[0], actualResult[0])
        assertEquals(expectedLocations[1], actualResult[1])
        verify { repositoryMock.getLocationsBy(SEARCH_QUERY) }
    }

    @Test
    fun `test use case returns empty locations`() = runTest {
        coEvery { repositoryMock.getLocationsBy(SEARCH_QUERY) } returns expectedEmptyLocations
        val actualResult = useCase.executeInBackground(SEARCH_QUERY)
        assertEquals(expectedEmptyLocations, actualResult)
        verify { repositoryMock.getLocationsBy(SEARCH_QUERY) }
    }

    @Test
    fun `test use case uses repository correctly`() = runTest {
        coEvery { repositoryMock.getLocationsBy(SEARCH_QUERY) } returns expectedLocations
        useCase.executeInBackground(SEARCH_QUERY)
        coVerify(exactly = 1) { repositoryMock.getLocationsBy(SEARCH_QUERY) }
        confirmVerified(repositoryMock)
    }
}