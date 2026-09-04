package com.example.wallpaper.data.image

import android.content.Context
import android.util.Log
import com.example.wallpaper.TestData.URI
import coil3.Bitmap
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.asImage
import coil3.request.ErrorResult
import coil3.request.SuccessResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(DelicateCoilApi::class)
class ImageRepositoryImplTest {

    private val context = mockk<Context>(relaxed = true)
    private val imageLoader = mockk<ImageLoader>()
    private val repository = ImageRepositoryImpl(context)

    @Before
    fun setUp() {
        mockkStatic(Log::class)

        every { Log.d(any(), any()) } returns 0

        SingletonImageLoader.setUnsafe(imageLoader)
    }

    @Test
    fun `invoke returns Success when image loading succeeds`() = runTest {
        val bitmap = mockk<Bitmap>(relaxed = true)
        val coilSuccessResult = mockk<SuccessResult>()
        every { coilSuccessResult.image } returns bitmap.asImage()

        coEvery {
            imageLoader.execute(any())
        } returns coilSuccessResult

        val result = repository(uri = URI)

        assertEquals(ImageResult.Success(bitmap), result)
    }

    @Test
    fun `invoke returns Error when image loading fails`() = runTest {
        val coilErrorResult = mockk<ErrorResult>(relaxed = true)
        coEvery { imageLoader.execute(any()) } returns coilErrorResult

        val result = repository(uri = URI)

        assertEquals(ImageResult.Error, result)
    }

    @After
    fun tearDown() {
        SingletonImageLoader.reset()
        unmockkStatic(Log::class)
    }
}
