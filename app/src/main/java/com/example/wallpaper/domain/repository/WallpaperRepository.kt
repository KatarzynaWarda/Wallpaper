package com.example.wallpaper.domain.repository

import com.example.wallpaper.domain.result.SetAsWallpaperResult

interface WallpaperRepository {

    suspend fun setImage(uri: String): SetAsWallpaperResult
}