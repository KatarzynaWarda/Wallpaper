package com.example.wallpaper.domain.repository

import com.example.wallpaper.domain.result.SaveInGalleryResult

interface GalleryRepository {

    suspend fun saveImage(uri: String): SaveInGalleryResult
}