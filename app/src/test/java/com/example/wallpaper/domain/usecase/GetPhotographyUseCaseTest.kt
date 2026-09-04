package com.example.wallpaper.domain.usecase

import com.example.wallpaper.domain.repository.PhotographyRepository
import com.example.wallpaper.domain.result.GetPhotographyResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetPhotographyUseCaseTest {

    private val repository = mockk<PhotographyRepository>()
    private val useCase = GetPhotographyUseCase(repository)

    @Test
    fun `returns photography result from repository`() = runTest {
        coEvery { repository.getPhotography() } returns GetPhotographyResult.Success(emptyList())

        val result = useCase()

        assertEquals(GetPhotographyResult.Success(emptyList()), result)
        coVerify(exactly = 1) { repository.getPhotography() }
    }
}
