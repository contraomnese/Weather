package com.contraomnese.weather.domain.home.usecase

import com.contraomnese.weather.domain.LocationDomainFixtures
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class ObserveFavoritesUseCaseTest {

    private lateinit var useCase: ObserveFavoritesUseCase
    private val repository = mockk<LocationsRepository>()

    @BeforeEach
    fun setUp() {
        useCase = ObserveFavoritesUseCaseImpl(repository)
    }

    @Test
    fun `should return correct multiple items when invoke is called`() = runTest {
        val expectedData = listOf(firstItems, secondItems)

        coEvery { repository.observeFavorites() } returns flow {
            emit(firstItems)
            emit(secondItems)
        }
        val actualResult = useCase().toList()

        assertEquals(expectedData.size, actualResult.size)
        assertEquals(expectedData[0].size, actualResult[0].size)
        assertEquals(expectedData[0], actualResult[0])
        assertEquals(expectedData[1].size, actualResult[1].size)
        assertEquals(expectedData[1], actualResult[1])
        coVerify(exactly = 1) { repository.observeFavorites() }
        confirmVerified(repository)
    }

    @Test
    fun `should throw exception when invoke is called`() = runTest {
        coEvery { repository.observeFavorites() } returns flow {
            throw repositoryException
        }
        val actualException = assertFailsWith<RuntimeException> {
            useCase().toList()
        }

        assertEquals(repositoryException.message, actualException.message)
        coVerify(exactly = 1) { repository.observeFavorites() }
        confirmVerified(repository)
    }

    companion object {
        private val firstItems = LocationDomainFixtures.generateLocations()
        private val secondItems = LocationDomainFixtures.generateLocations()
        private val repositoryException = RuntimeException("Repository exception")
    }

}