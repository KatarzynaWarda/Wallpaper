package com.example.wallpaper.domain.usecase

import com.example.wallpaper.TestData.URI
import com.example.wallpaper.domain.repository.WallpaperRepository
import com.example.wallpaper.domain.result.SetAsWallpaperResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SetAsWallpaperUseCaseTest {

    private val repository = mockk<WallpaperRepository>()
    private val useCase = SetAsWallpaperUseCase(repository)

    @Test
    fun `returns wallpaper result from repository`() = runTest {
        coEvery { repository.setImage(URI) } returns SetAsWallpaperResult.Success

        val result = useCase(URI)

        assertEquals(SetAsWallpaperResult.Success, result)
        coVerify(exactly = 1) { repository.setImage(URI) }
    }
}
