package com.example.wallpaper.domain.result

sealed interface SaveInGalleryResult {
    data object Success : SaveInGalleryResult
    data object Error : SaveInGalleryResult
}