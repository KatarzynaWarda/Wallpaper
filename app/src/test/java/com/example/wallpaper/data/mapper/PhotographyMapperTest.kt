package com.example.wallpaper.data.mapper

import com.example.wallpaper.data.dto.PhotographyDto
import com.example.wallpaper.domain.model.Photography
import org.junit.Assert.assertEquals
import org.junit.Test

class PhotographyMapperTest {

    private companion object {
        const val ID = 1
        const val PREVIEW_URL = "previewUrl"
        const val WEBFORMAT_URL = "webformatURL"
        const val LARGE_IMAGE_URL = "largeImageURL"
        const val TAGS = "1, 2"
        const val USER = "user"
        const val USER_IMAGE_URL = "userImageURL"
    }

    private val mapper = PhotographyMapper()
    private val photographyDto = PhotographyDto(
            id = ID,
            previewURL = PREVIEW_URL,
            webformatURL = WEBFORMAT_URL,
            largeImageURL = LARGE_IMAGE_URL,
            tags = TAGS,
            user = USER,
            userImageURL = USER_IMAGE_URL,
    )
    private val photography = Photography(
            id = ID,
            url = LARGE_IMAGE_URL,
            previewUrl = PREVIEW_URL,
            tags = listOf("1", "2"),
            author = USER,
    )

    @Test
    fun `returns mapped photography`() {
        val result = mapper(photographyDto)

        assertEquals(photography, result)
    }
}
