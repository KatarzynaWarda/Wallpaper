package com.example.wallpaper.domain.usecase

import com.example.wallpaper.TestData.URI
import com.example.wallpaper.domain.repository.GalleryRepository
import com.example.wallpaper.domain.result.SaveInGalleryResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveInGalleryUseCaseTest {

    private val repository = mockk<GalleryRepository>()
    private val useCase = SaveInGalleryUseCase(repository)

    @Test
    fun `returns save result from repository`() = runTest {
        coEvery { repository.saveImage(URI) } returns SaveInGalleryResult.Success

        val result = useCase(URI)

        assertEquals(SaveInGalleryResult.Success, result)
        coVerify(exactly = 1) { repository.saveImage(URI) }
    }
}
