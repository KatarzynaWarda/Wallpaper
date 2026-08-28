package com.example.wallpaper.data.mapper

import com.example.wallpaper.data.dto.EditPhotoResponseDto
import com.example.wallpaper.domain.model.EditedPhoto
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class EditPhotoMapperTest {
    private companion object {
        const val IMAGE_URL = "image url"
    }

    private lateinit var mapper: EditPhotoMapper
    private lateinit var editPhotoResponseDto: EditPhotoResponseDto
    private lateinit var editedPhoto: EditedPhoto

    @Before
    fun setUp() {
        mapper = EditPhotoMapper()
        editPhotoResponseDto = EditPhotoResponseDto(
            imageUrl = IMAGE_URL,
        )
        editedPhoto = EditedPhoto(
            imageUrl = IMAGE_URL,
        )
    }

    @Test
    fun `return correct Photography`() {

        val result = mapper(editPhotoResponseDto)
        assertEquals(editedPhoto, result)
    }
}