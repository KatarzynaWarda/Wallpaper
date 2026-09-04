package com.example.wallpaper.data.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import coil3.Bitmap
import com.example.wallpaper.TestData.URI
import com.example.wallpaper.data.image.ImageRepository
import com.example.wallpaper.data.image.ImageResult
import com.example.wallpaper.domain.result.SaveInGalleryResult
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkConstructor
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.io.OutputStream

class GalleryRepositoryImplTest {

    private val imageRepository = mockk<ImageRepository>()
    private val contentResolver = mockk<ContentResolver>()
    private val collectionUri = mockk<Uri>()
    private val outputStream = mockk<OutputStream>()
    private val bitmap = mockk<Bitmap>(relaxed = true)
    private val repository = GalleryRepositoryImpl(
        imageRepository = imageRepository,
        contentResolver = contentResolver,
    )

    @Before
    fun setUp() {
        mockkConstructor(ContentValues::class)

        every { anyConstructed<ContentValues>().put(any<String>(), any<String>()) } just Runs
        every { anyConstructed<ContentValues>().put(any<String>(), any<Int>()) } just Runs

        mockkStatic(MediaStore.Images.Media::class)

        every { MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) } returns collectionUri

        coEvery { imageRepository(URI) } returns ImageResult.Success(bitmap)
    }

    @Test
    fun `returns Error when image loading fails`() = runTest {
        coEvery { imageRepository(URI) } returns ImageResult.Error

        val result = repository.saveImage(URI)

        assertEquals(SaveInGalleryResult.Error, result)
    }

    @Test
    fun `returns Error when inserting MediaStore entry fails`() = runTest {
        every { contentResolver.insert(any(), any()) } returns null

        val result = repository.saveImage(URI)

        assertEquals(SaveInGalleryResult.Error, result)
    }

    @Test
    fun `returns Error and deletes MediaStore entry when output stream cannot be opened`() = runTest {
        givenMediaStoreEntryIsInserted()
        every { contentResolver.openOutputStream(collectionUri) } returns null
        givenMediaStoreEntryCanBeDeleted()

        val result = repository.saveImage(URI)

        assertEquals(SaveInGalleryResult.Error, result)
        verifyMediaStoreEntryWasDeleted()
    }

    @Test
    fun `returns Error and deletes MediaStore entry when bitmap compression fails`() = runTest {
        givenMediaStoreEntryIsInserted()
        givenOutputStreamIsOpened()
        givenMediaStoreEntryCanBeDeleted()
        every { bitmap.compress(any(), any(), any()) } returns false

        val result = repository.saveImage(URI)

        assertEquals(SaveInGalleryResult.Error, result)
        verifyMediaStoreEntryWasDeleted()
    }

    @Test
    fun `returns Error and deletes MediaStore entry when writing image throws IOException`() = runTest {
        givenMediaStoreEntryIsInserted()
        every {
            contentResolver.openOutputStream(collectionUri)
        } throws IOException("Writing failed")
        givenMediaStoreEntryCanBeDeleted()

        val result = repository.saveImage(URI)

        assertEquals(SaveInGalleryResult.Error, result)
        verifyMediaStoreEntryWasDeleted()
    }

    @Test
    fun `returns Error and deletes MediaStore entry when finalizing image throws IOException`() = runTest {
        givenMediaStoreEntryIsInserted()
        givenOutputStreamIsOpened()
        every { bitmap.compress(any(), any(), any()) } returns true
        every {
            contentResolver.update(collectionUri, any(), null, null)
        } throws IOException("Finalizing failed")
        givenMediaStoreEntryCanBeDeleted()

        val result = repository.saveImage(URI)

        assertEquals(SaveInGalleryResult.Error, result)
        verifyMediaStoreEntryWasDeleted()
    }

    @Test
    fun `returns Error when loading image throws IOException`() = runTest {
        coEvery { imageRepository(URI) } throws IOException("Loading failed")

        val result = repository.saveImage(URI)

        assertEquals(SaveInGalleryResult.Error, result)
    }

    @Test
    fun `returns Success and finalizes MediaStore entry when image is saved`() = runTest {
        givenMediaStoreEntryIsInserted()
        givenOutputStreamIsOpened()
        every { bitmap.compress(any(), any(), any()) } returns true
        every {
            contentResolver.update(collectionUri, any(), null, null)
        } returns 1

        val result = repository.saveImage(URI)

        assertEquals(SaveInGalleryResult.Success, result)
        verify(exactly = 1) {
            contentResolver.update(collectionUri, any(), null, null)
        }
        verify(exactly = 0) {
            contentResolver.delete(any(), any(), any())
        }
    }

    @After
    fun tearDown() {
        unmockkStatic(MediaStore.Images.Media::class)
        unmockkConstructor(ContentValues::class)
    }

    private fun givenMediaStoreEntryIsInserted() {
        every { contentResolver.insert(any(), any()) } returns collectionUri
    }

    private fun givenOutputStreamIsOpened() {
        every { contentResolver.openOutputStream(collectionUri) } returns outputStream
        every { outputStream.close() } just Runs
    }

    private fun givenMediaStoreEntryCanBeDeleted() {
        every { contentResolver.delete(collectionUri, null, null) } returns 0
    }

    private fun verifyMediaStoreEntryWasDeleted() {
        verify(exactly = 1) {
            contentResolver.delete(collectionUri, null, null)
        }
    }
}
