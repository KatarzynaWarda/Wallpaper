package com.example.wallpaper.data.image

import android.content.Context
import android.util.Log
import coil3.imageLoader
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.size.Size
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageRepositoryImpl(
    private val context: Context,
): ImageRepository {

    override suspend operator fun invoke(
        uri: String,
    ): ImageResult = withContext(Dispatchers.IO) {
        val imageLoader = context.imageLoader

        val requestBuilder = ImageRequest.Builder(context).data(uri)
        requestBuilder.size(Size.ORIGINAL)
        val request = requestBuilder.build()

        when (val result = imageLoader.execute(request)) {
            is SuccessResult -> {
                val bitmap = result.image.toBitmap()
                ImageResult.Success(bitmap)
            }

            is ErrorResult -> {
                Log.d("Investigation", "ImageRepository: ErrorResult ${result.throwable}")
                ImageResult.Error
            }
        }
    }
}