package com.example.wallpaper.data.repository

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.scale
import com.example.wallpaper.data.image.ImageRepository
import com.example.wallpaper.data.image.ImageResult
import com.example.wallpaper.domain.result.SetAsWallpaperResult
import com.example.wallpaper.domain.repository.WallpaperRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.math.max

class WallpaperRepositoryImpl(
    val imageRepository: ImageRepository,
    val context: Context,
) : WallpaperRepository {
    override suspend fun setImage(uri: String): SetAsWallpaperResult = withContext(Dispatchers.IO) {
        try {
            when (val result = imageRepository(uri = uri)) {
                is ImageResult.Success -> {
                    val wallpaperManager = WallpaperManager.getInstance(context)
                    val bitmap = result.bitmap

                    val displayMetrics = context.resources.displayMetrics
                    val screenWidth = displayMetrics.widthPixels
                    val screenHeight = displayMetrics.heightPixels

                    wallpaperManager.suggestDesiredDimensions(screenWidth, screenHeight)

                    val scale = max(
                        screenWidth.toFloat() / bitmap.width,
                        screenHeight.toFloat() / bitmap.height
                    )
                    val scaledWidth = (bitmap.width * scale).toInt()
                    val scaledHeight = (bitmap.height * scale).toInt()

                    val scaledBitmap = bitmap.scale(scaledWidth, scaledHeight)

                    val xOffset = (scaledWidth - screenWidth) / 2
                    val yOffset = (scaledHeight - screenHeight) / 2

                    val finalBitmap = Bitmap.createBitmap(
                        scaledBitmap,
                        xOffset,
                        yOffset,
                        screenWidth,
                        screenHeight
                    )

                    wallpaperManager.setBitmap(
                        finalBitmap,
                        null,
                        true,
                        WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                    )
                    SetAsWallpaperResult.Success
                }

                ImageResult.Error -> SetAsWallpaperResult.Error
            }
        } catch (_: IOException) {
            SetAsWallpaperResult.Error
        }
    }
}