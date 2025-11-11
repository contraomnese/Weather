package com.contraomnese.weather.domain.home.usecase

import android.database.sqlite.SQLiteException
import com.contraomnese.weather.domain.home.repository.LocationsRepository
import com.contraomnese.weather.domain.utils.assertIsFailure
import com.contraomnese.weather.domain.utils.assertIsSuccess
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
    private val favoriteId = 1
    private val expectedData = Unit
    private val expectedException = SQLiteException("Database doesn't exist")

    @BeforeEach
    fun setUp() {
        useCase = RemoveFavoriteUseCaseImpl(repository)
    }

    @Test
    fun `given repository returns success result when invoke is called then returns Unit`() = runTest {
        coEvery { repository.deleteFavorite(favoriteId) } returns Result.success(Unit)
        val actualResult = useCase(favoriteId)

        val actualData = actualResult.assertIsSuccess()
        assertEquals(expectedData, actualData)
        coVerify(exactly = 1) { repository.deleteFavorite(favoriteId) }
        confirmVerified(repository)
    }

    @Test
    fun `given repository returns failure result when invoke is called then throws exception`() = runTest {
        coEvery { repository.deleteFavorite(favoriteId) } returns Result.failure(expectedException)
        val actualResult = useCase(favoriteId)

        val actualException = actualResult.assertIsFailure()
        assertEquals(expectedException.message, actualException.message)
        coVerify(exactly = 1) { repository.deleteFavorite(favoriteId) }
        confirmVerified(repository)
    }

}