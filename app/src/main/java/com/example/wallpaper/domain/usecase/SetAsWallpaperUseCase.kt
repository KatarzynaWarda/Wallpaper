package com.example.wallpaper.domain.usecase

import com.example.wallpaper.domain.result.SetAsWallpaperResult
import com.example.wallpaper.domain.repository.WallpaperRepository

class SetAsWallpaperUseCase(
    private val wallpaperRepository: WallpaperRepository,
) {

    suspend operator fun invoke(uri: String): SetAsWallpaperResult =
        wallpaperRepository.setImage(uri)
}