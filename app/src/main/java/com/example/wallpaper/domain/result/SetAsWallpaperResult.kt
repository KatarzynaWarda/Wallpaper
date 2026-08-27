package com.example.wallpaper.domain.result

sealed interface SetAsWallpaperResult {
    data object Success : SetAsWallpaperResult
    data object Error : SetAsWallpaperResult
}