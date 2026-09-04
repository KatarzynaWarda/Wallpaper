package com.example.wallpaper.data.repository

import android.content.ContentResolver
import android.net.Uri
import com.example.wallpaper.TestData.PROMPT
import com.example.wallpaper.TestData.URI
import com.example.wallpaper.data.dto.EditPhotoResponseDto
import com.example.wallpaper.data.mapper.EditPhotoMapper
import com.example.wallpaper.data.network.EditPhotoApi
import okhttp3.Call
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream

class EditPhotoRepositoryImplTest {

    private companion object {
        const val HTTP_URI = "https://example.com/image.jpg"
        val IMAGE_BYTES = byteArrayOf(1, 2, 3)
    }
    private val editPhotoApi = mockk<EditPhotoApi>()
    private val editPhotoMapper = EditPhotoMapper()
    private val okHttpClient = mockk<OkHttpClient>()
    private val contentResolver = mockk<ContentResolver>()
    private val localAndroidUri = mockk<Uri>()
    private val httpAndroidUri = mockk<Uri>()
    private val repository = EditPhotoRepositoryImpl(
        api = editPhotoApi,
        editPhotoMapper = editPhotoMapper,
        okHttpClient = okHttpClient,
        contentResolver = contentResolver,
    )

    @Before
    fun setUp() {
        mockkStatic(Uri::class)

        every { Uri.parse(URI) } returns localAndroidUri
        every { localAndroidUri.scheme } returns null

        every { Uri.parse(HTTP_URI) } returns httpAndroidUri
        every { httpAndroidUri.scheme } returns "https"

    }

    @Test
    fun `returns edited photo when local image is read successfully`() = runTest {
        coEvery { editPhotoApi.getEditedPhoto(any(), any()) } returns EditPhotoResponseDto(URI)
        givenLocalImageCanBeRead()

        val result = repository.getEditedPhoto(PROMPT, URI)

        assertEquals(editPhotoMapper(EditPhotoResponseDto(URI)), result)
    }

    @Test
    fun `throws IllegalArgumentException when local image cannot be read`() {
        every { contentResolver.openInputStream(any()) } returns null

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runTest {
                repository.getEditedPhoto(PROMPT, URI)
            }
        }

        assertEquals("Unable to read the image", exception.message)
    }

    @Test
    fun `returns edited photo when HTTP image is downloaded successfully`() = runTest {
        givenHttpImageResponse(
            isSuccessful = true,
            body = IMAGE_BYTES.toResponseBody(),
        )
        coEvery { editPhotoApi.getEditedPhoto(any(), any()) } returns EditPhotoResponseDto(HTTP_URI)

        val result = repository.getEditedPhoto(PROMPT, HTTP_URI)

        assertEquals(editPhotoMapper(EditPhotoResponseDto(HTTP_URI)), result)
    }

    @Test
    fun `throws exception when HTTP image download is unsuccessful`() {
        givenHttpImageResponse(isSuccessful = false, code = 404)

        val exception = assertThrows(Exception::class.java) {
            runTest {
                repository.getEditedPhoto(PROMPT, HTTP_URI)
            }
        }

        assertEquals("Failed to download the image: 404", exception.message)
    }

    @Test
    fun `throws exception when HTTP image response has no body`() {
        givenHttpImageResponse(isSuccessful = true, body = null)

        val exception = assertThrows(Exception::class.java) {
            runTest {
                repository.getEditedPhoto(PROMPT, HTTP_URI)
            }
        }

        assertEquals("The image file is empty", exception.message)
    }

    @Test
    fun `propagates exception when edit photo API fails`() {
        givenLocalImageCanBeRead()
        coEvery { editPhotoApi.getEditedPhoto(any(), any()) } throws Exception("API error")

        val exception = assertThrows(Exception::class.java) {
            runTest {
                repository.getEditedPhoto(PROMPT, URI)
            }
        }

        assertEquals("API error", exception.message)
    }

    @After
    fun tearDown() {
        unmockkStatic(Uri::class)
    }

    private fun givenHttpImageResponse(
        isSuccessful: Boolean,
        code: Int = 200,
        body: okhttp3.ResponseBody? = null,
    ) {
        val call = mockk<Call>()
        val response = mockk<Response>(relaxed = true)

        every { okHttpClient.newCall(any()) } returns call
        every { call.execute() } returns response
        every { response.isSuccessful } returns isSuccessful
        every { response.code } returns code
        every { response.body } returns body
    }

    private fun givenLocalImageCanBeRead() {
        every { contentResolver.openInputStream(any()) } returns ByteArrayInputStream(IMAGE_BYTES)
    }
}
