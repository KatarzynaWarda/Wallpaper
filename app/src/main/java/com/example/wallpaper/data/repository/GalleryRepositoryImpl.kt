package com.example.wallpaper.data.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import com.example.wallpaper.data.image.ImageRepository
import com.example.wallpaper.data.image.ImageResult
import com.example.wallpaper.domain.repository.GalleryRepository
import com.example.wallpaper.domain.result.SaveInGalleryResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class GalleryRepositoryImpl(
    val imageRepository: ImageRepository,
    val contentResolver: ContentResolver,
) : GalleryRepository {
    override suspend fun saveImage(uri: String): SaveInGalleryResult = withContext(Dispatchers.IO) {
        try {
            val image = when (val result = imageRepository(uri = uri)) {
                is ImageResult.Success -> {
                    result.bitmap
                }

                is ImageResult.Error -> return@withContext SaveInGalleryResult.Error
            }

            val values = ContentValues().apply {
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    "photo_${System.currentTimeMillis()}.jpg"
                )
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val collection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )

            val newUri = contentResolver.insert(
                collection,
                values,
            ) ?: return@withContext SaveInGalleryResult.Error

            try {
                val success = contentResolver
                    .openOutputStream(newUri)
                    ?.use { outputStream ->
                        image.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            outputStream
                        )
                    } ?: false

                if (!success) {
                    contentResolver.delete(newUri, null, null)
                    return@withContext SaveInGalleryResult.Error
                }

                val completedValues = ContentValues().apply {
                    put(MediaStore.Images.Media.IS_PENDING, 0)
                }

                contentResolver.update(
                    newUri,
                    completedValues,
                    null,
                    null
                )
                SaveInGalleryResult.Success
            } catch (_: IOException) {
                contentResolver.delete(newUri, null, null)
                SaveInGalleryResult.Error
            }
        } catch (_: IOException) {
            SaveInGalleryResult.Error
        }
    }
}