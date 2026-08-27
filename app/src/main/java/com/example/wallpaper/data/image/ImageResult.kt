package com.example.wallpaper.data.image

import coil3.Bitmap

sealed interface ImageResult {
    data class Success(val bitmap: Bitmap) : ImageResult
    data object Error : ImageResult
}