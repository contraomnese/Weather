package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.LocationDomainFixtures
import com.contraomnese.weather.domain.LocationDomainFixtures.generateLocationName
import com.contraomnese.weather.domain.assertIsFailure
import com.contraomnese.weather.domain.assertIsSuccess
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.weatherByLocation.model.Location
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetLocationsUseCaseTest {

    private lateinit var useCase: GetLocationsUseCase
    private val repository = mockk<LocationsRepository>()

    @BeforeEach
    fun setUp() {
        useCase = GetLocationsUseCaseImpl(repository)
    }

    @Test
    fun `should return flow with correct item when invoke is called`() = runTest {
        val expectedData = location

        coEvery { repository.getLocationsByName(locationName) } returns Result.success(listOf(expectedData))
        val actualResult = useCase(locationName)

        val actualData = actualResult.assertIsSuccess().first()

        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.getLocationsByName(locationName) }
        confirmVerified(repository)
    }

    @Test
    fun `should return flow with correct multiple items when invoke is called`() = runTest {
        val expectedData = listOf(firstItem, secondItem)

        coEvery { repository.getLocationsByName(locationName) } returns Result.success(expectedData)
        val actualResult = useCase(locationName)

        val actualData = actualResult.assertIsSuccess()
        assertEquals(expectedData.size, actualData.size)
        assertEquals(expectedData[0], actualData[0])
        assertEquals(expectedData[1], actualData[1])
        coVerify(exactly = 1) { repository.getLocationsByName(locationName) }
        confirmVerified(repository)
    }

    @Test
    fun `should return flow with empty items when invoke is called`() = runTest {

        val expectedData = emptyList<Location>()

        coEvery { repository.getLocationsByName(locationName) } returns Result.success(expectedData)
        val actualResult = useCase(locationName)

        val actualData = actualResult.assertIsSuccess()
        assertEquals(true, actualData.isEmpty())
        coVerify(exactly = 1) { repository.getLocationsByName(locationName) }
        confirmVerified(repository)
    }

    @Test
    fun `should return failure result with exception invoke is called`() = runTest {
        coEvery { repository.getLocationsByName(locationName) } returns Result.failure(repositoryException)
        val actualResult = useCase(locationName)

        val actualException = actualResult.assertIsFailure()
        assertEquals(repositoryException.message, actualException.message)
        coVerify(exactly = 1) { repository.getLocationsByName(locationName) }
        confirmVerified(repository)
    }

    companion object {
        private val locationName = generateLocationName()
        private val location = LocationDomainFixtures.generateLocation()
        private val firstItem = LocationDomainFixtures.generateLocation()
        private val secondItem = LocationDomainFixtures.generateLocation()
        private val repositoryException = RuntimeException("Repository exception")
    }
}