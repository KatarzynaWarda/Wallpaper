package com.example.wallpaper.data.repository

import android.app.WallpaperManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.util.DisplayMetrics
import com.example.wallpaper.TestData.URI
import com.example.wallpaper.data.image.ImageRepository
import com.example.wallpaper.data.image.ImageResult
import com.example.wallpaper.domain.result.SetAsWallpaperResult
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class WallpaperRepositoryImplTest {

    private companion object {
        const val SCREEN_WIDTH = 100
        const val SCREEN_HEIGHT = 200
    }

    private val imageRepository = mockk<ImageRepository>()
    private val context = mockk<Context>()
    private val wallpaperManager = mockk<WallpaperManager>()
    private val repository = WallpaperRepositoryImpl(imageRepository, context)

    @Before
    fun setUp() {
        mockkStatic(WallpaperManager::class)
        mockkStatic(Bitmap::class)

        every { WallpaperManager.getInstance(context) } returns wallpaperManager
    }

    @Test
    fun `returns Error when image loading fails`() = runTest {
        coEvery { imageRepository(URI) } returns ImageResult.Error

        val result = repository.setImage(URI)

        assertEquals(SetAsWallpaperResult.Error, result)
    }

    @Test
    fun `returns Error when image loading throws IOException`() = runTest {
        coEvery { imageRepository(URI) } throws IOException("Loading failed")

        val result = repository.setImage(URI)

        assertEquals(SetAsWallpaperResult.Error, result)
    }

    @Test
    fun `scales crops and sets image as system and lock wallpaper`() = runTest {
        val bitmap = mockk<Bitmap>()
        val scaledBitmap = mockk<Bitmap>()
        val finalBitmap = mockk<Bitmap>()
        val resources = mockk<Resources>()
        val displayMetrics = mockk<DisplayMetrics>(relaxed = true).apply {
            widthPixels = SCREEN_WIDTH
            heightPixels = SCREEN_HEIGHT
        }
        coEvery { imageRepository(URI) } returns ImageResult.Success(bitmap)
        every { context.resources } returns resources
        every { resources.displayMetrics } returns displayMetrics
        every { bitmap.width } returns 100
        every { bitmap.height } returns 100
        every {
            Bitmap.createScaledBitmap(bitmap, 200, 200, true)
        } returns scaledBitmap
        every {
            Bitmap.createBitmap(scaledBitmap, 50, 0, SCREEN_WIDTH, SCREEN_HEIGHT)
        } returns finalBitmap
        every {
            wallpaperManager.suggestDesiredDimensions(SCREEN_WIDTH, SCREEN_HEIGHT)
        } just Runs
        every {
            wallpaperManager.setBitmap(
                finalBitmap,
                null,
                true,
                WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK,
            )
        } returns 1

        val result = repository.setImage(URI)

        assertEquals(SetAsWallpaperResult.Success, result)
        verify(exactly = 1) {
            wallpaperManager.setBitmap(
                finalBitmap,
                null,
                true,
                WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK,
            )
        }
    }

    @After
    fun tearDown() {
        unmockkStatic(Bitmap::class)
        unmockkStatic(WallpaperManager::class)
    }
}
