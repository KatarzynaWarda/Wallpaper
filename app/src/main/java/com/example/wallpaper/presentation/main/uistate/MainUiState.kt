package com.example.wallpaper.presentation.main.uistate

data class MainUiState(
    val title: String,
    val subtitle: String,
    val sectionTitle: String,
    val sectionSubtitle: String,
    val wallpaperButtons: List<WallpaperButtons>,
    val imageUri: String? = null,
)

data class WallpaperButtons(
    val title: String,
    val subtitle: String,
)
