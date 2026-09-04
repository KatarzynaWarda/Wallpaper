package com.example.wallpaper.data.mapper

import com.example.wallpaper.data.dto.EditPhotoResponseDto
import com.example.wallpaper.domain.model.EditedPhoto
import org.junit.Assert.assertEquals
import org.junit.Test

class EditPhotoMapperTest {
    private companion object {
        const val IMAGE_URL = "image url"
    }

    private val mapper = EditPhotoMapper()
    private val editPhotoResponseDto = EditPhotoResponseDto(imageUrl = IMAGE_URL)
    private val editedPhoto = EditedPhoto(imageUrl = IMAGE_URL)

    @Test
    fun `returns mapped edited photo`() {
        val result = mapper(editPhotoResponseDto)

        assertEquals(editedPhoto, result)
    }
}
