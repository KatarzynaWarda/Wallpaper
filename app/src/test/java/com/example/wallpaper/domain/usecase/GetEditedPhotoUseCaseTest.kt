package com.example.wallpaper.domain.usecase

import com.example.wallpaper.TestData.PROMPT
import com.example.wallpaper.TestData.URI
import com.example.wallpaper.domain.model.EditedPhoto
import com.example.wallpaper.domain.repository.EditPhotoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetEditedPhotoUseCaseTest {

    private val repository = mockk<EditPhotoRepository>()
    private val useCase = GetEditedPhotoUseCase(repository)

    @Test
    fun `returns edited photo from repository`() = runTest {
        val editedPhoto = EditedPhoto("edited-url")
        coEvery { repository.getEditedPhoto(PROMPT, URI) } returns editedPhoto

        val result = useCase(PROMPT, URI)

        assertEquals(editedPhoto, result)
        coVerify(exactly = 1) { repository.getEditedPhoto(PROMPT, URI) }
    }
}
