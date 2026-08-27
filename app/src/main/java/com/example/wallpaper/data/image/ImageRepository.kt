package com.example.wallpaper.data.image

interface ImageRepository {

    suspend operator fun invoke(uri: String): ImageResult
}