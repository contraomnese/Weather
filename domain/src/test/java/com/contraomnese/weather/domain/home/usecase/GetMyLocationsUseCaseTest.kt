package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.cleanarchitecture.coroutine.CoroutineContextProvider
import com.contraomnese.weather.domain.home.model.MyLocationDomainModel
import com.contraomnese.weather.domain.home.repository.MyLocationsRepository
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

class GetMyLocationsUseCaseTest {

    private lateinit var useCase: GetMyLocationsUseCase
    private val repositoryMock = mockk<MyLocationsRepository>()
    private lateinit var coroutineContextProvider: CoroutineContextProvider
    private val expectedLocations = listOf(
        MyLocationDomainModel(
            id = FIRST_LOCATION_ID,
            name = FIRST_LOCATION_NAME
        ),
        MyLocationDomainModel(
            id = SECOND_LOCATION_ID,
            name = SECOND_LOCATION_NAME
        )
    )
    private val expectedEmptyLocations = emptyList<MyLocationDomainModel>()

    @BeforeEach
    fun setUp() {
        coroutineContextProvider = FakeCoroutineContextProvider
        useCase = GetMyLocationsUseCase(
            repository = repositoryMock,
            coroutineContextProvider = coroutineContextProvider
        )
    }

    @Test
    fun `test use case returns correct locations`() = runTest {
        coEvery { repositoryMock.getLocations() } returns expectedLocations
        val actualResult = useCase.executeInBackground()
        assertEquals(expectedLocations, actualResult)
        assertEquals(expectedLocations.size, actualResult.size)
        assertEquals(expectedLocations[0], actualResult[0])
        assertEquals(expectedLocations[1], actualResult[1])
        verify { repositoryMock.getLocations() }
    }

    @Test
    fun `test use case returns empty locations`() = runTest {
        coEvery { repositoryMock.getLocations() } returns expectedEmptyLocations
        val actualResult = useCase.executeInBackground()
        assertEquals(expectedEmptyLocations, actualResult)
        verify { repositoryMock.getLocations() }
    }

    @Test
    fun `test use case uses repository correctly`() = runTest {
        coEvery { repositoryMock.getLocations() } returns expectedLocations
        useCase.executeInBackground()
        coVerify(exactly = 1) { repositoryMock.getLocations() }
        confirmVerified(repositoryMock)
    }
}