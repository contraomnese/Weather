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
import kotlin.test.assertFailsWith

private const val FIRST_LOCATION_ID = "1"
private const val FIRST_LOCATION_NAME = "firstName"
private const val BAD_LOCATION_ID = "bad id"

class GetMyLocationUseCaseTest {

    private lateinit var useCase: GetMyLocationUseCase
    private val repositoryMock = mockk<MyLocationsRepository>()
    private lateinit var coroutineContextProvider: CoroutineContextProvider
    private val expectedLocation = MyLocationDomainModel(id = FIRST_LOCATION_ID, name = FIRST_LOCATION_NAME)
    private val expectedException = Exception("Location not found")

    @BeforeEach
    fun setUp() {
        coroutineContextProvider = FakeCoroutineContextProvider
        useCase = GetMyLocationUseCase(
            repository = repositoryMock,
            coroutineContextProvider = coroutineContextProvider
        )
    }

    @Test
    fun `test use case returns correct location`() = runTest {
        coEvery { repositoryMock.getLocationBy(id = FIRST_LOCATION_ID) } returns expectedLocation
        val actualResult = useCase.executeInBackground(request = FIRST_LOCATION_ID)
        assertEquals(expectedLocation, actualResult)
        verify { repositoryMock.getLocationBy(id = FIRST_LOCATION_ID) }
    }

    @Test
    fun `test use case uses repository correctly`() = runTest {
        coEvery { repositoryMock.getLocationBy(id = FIRST_LOCATION_ID) } returns expectedLocation
        useCase.executeInBackground(request = FIRST_LOCATION_ID)
        coVerify(exactly = 1) { repositoryMock.getLocationBy(id = FIRST_LOCATION_ID) }
        confirmVerified(repositoryMock)
    }

    @Test
    fun `test use case return throw when call repository with bad id`() = runTest {
        // Arrange
        coEvery { repositoryMock.getLocationBy(BAD_LOCATION_ID) } throws expectedException

        // Act & Assert
        val thrown = assertFailsWith<Exception> {
            useCase.executeInBackground(BAD_LOCATION_ID)
        }

        assertEquals(expectedException.message, thrown.message)
        coVerify { repositoryMock.getLocationBy(BAD_LOCATION_ID) }
        confirmVerified(repositoryMock)
    }
}