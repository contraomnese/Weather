package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.LocationDomainFixtures
import com.contraomnese.weather.domain.assertIsFailure
import com.contraomnese.weather.domain.assertIsSuccess
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RemoveFavoriteUseCaseTest {

    private lateinit var useCase: RemoveFavoriteUseCase
    private val repository = mockk<LocationsRepository>()

    @BeforeEach
    fun setUp() {
        useCase = RemoveFavoriteUseCaseImpl(repository)
    }

    @Test
    fun `should return success result with unit when invoke is called`() = runTest {
        val expectedData = Unit

        coEvery { repository.deleteFavorite(favoriteLocationId) } returns Result.success(expectedData)
        val actualResult = useCase(favoriteLocationId)

        val actualData = actualResult.assertIsSuccess()
        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.deleteFavorite(favoriteLocationId) }
        confirmVerified(repository)
    }

    @Test
    fun `should return failure result with exception invoke is called`() = runTest {

        val expectedException = repositoryException

        coEvery { repository.deleteFavorite(favoriteLocationId) } returns Result.failure(expectedException)
        val actualResult = useCase(favoriteLocationId)

        val actualException = actualResult.assertIsFailure()
        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.deleteFavorite(favoriteLocationId) }
        confirmVerified(repository)
    }

    companion object {
        private val favoriteLocationId = LocationDomainFixtures.generateLocationId()
        private val repositoryException = RuntimeException("Repository error")
    }

}