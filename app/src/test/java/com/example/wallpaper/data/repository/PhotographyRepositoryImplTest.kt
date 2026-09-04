package com.example.wallpaper.data.repository

import com.example.wallpaper.data.dto.PhotographyDto
import com.example.wallpaper.data.dto.PixabayResponseDto
import com.example.wallpaper.data.mapper.PhotographyMapper
import com.example.wallpaper.data.network.PhotographyApi
import com.example.wallpaper.domain.model.Photography
import com.example.wallpaper.domain.result.GetPhotographyResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class PhotographyRepositoryImplTest {

    private val api = mockk<PhotographyApi>()
    private val mapper = mockk<PhotographyMapper>()
    private val repository = PhotographyRepositoryImpl(api, mapper)

    @Test
    fun `returns mapped photography when API succeeds`() = runTest {
        val firstDto = photographyDto(id = 1)
        val secondDto = photographyDto(id = 2)
        val firstPhotography = photography(id = 1)
        val secondPhotography = photography(id = 2)
        coEvery { api.getPhotography() } returns PixabayResponseDto(
            total = 2,
            totalHits = 2,
            hits = listOf(firstDto, secondDto),
        )
        every { mapper(firstDto) } returns firstPhotography
        every { mapper(secondDto) } returns secondPhotography

        val result = repository.getPhotography()

        assertEquals(
            GetPhotographyResult.Success(listOf(firstPhotography, secondPhotography)),
            result,
        )
    }

    @Test
    fun `returns empty photography list when API response has no hits`() = runTest {
        coEvery { api.getPhotography() } returns PixabayResponseDto(
            total = 0,
            totalHits = 0,
            hits = emptyList(),
        )

        val result = repository.getPhotography()

        assertEquals(GetPhotographyResult.Success(emptyList()), result)
    }

    @Test
    fun `returns Error when API throws IOException`() = runTest {
        coEvery { api.getPhotography() } throws IOException("Network error")

        val result = repository.getPhotography()

        assertEquals(GetPhotographyResult.Error, result)
    }

    private fun photographyDto(id: Int) = PhotographyDto(
        id = id,
        tags = "nature, sky",
        previewURL = "preview-$id",
        webformatURL = "web-$id",
        largeImageURL = "large-$id",
        user = "user-$id",
        userImageURL = "avatar-$id",
    )

    private fun photography(id: Int) = Photography(
        id = id,
        url = "large-$id",
        previewUrl = "preview-$id",
        tags = listOf("nature", "sky"),
        author = "user-$id",
    )
}
