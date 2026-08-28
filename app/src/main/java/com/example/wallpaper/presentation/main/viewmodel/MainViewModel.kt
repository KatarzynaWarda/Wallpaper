package com.example.wallpaper.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.wallpaper.presentation.main.uistate.MainUiState
import com.example.wallpaper.presentation.main.uistate.WallpaperButtons
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(mainUiState())
    val uiState = _uiState.asStateFlow()

    private fun mainUiState() = MainUiState(
        title = "Wallpaper Creator",
        subtitle = "Create a unique wallpaper with AI",
        sectionTitle = "Choose a photo",
        sectionSubtitle = "Add an image you want to transform",
        wallpaperButtons = listOf(
            WallpaperButtons(
                title = "From gallery",
                subtitle = "Choose from your device",
            ),
            WallpaperButtons(
                title = "Take a photo",
                subtitle = "Use the camera",
            ),
            WallpaperButtons(
                title = "From the web",
                subtitle = "Download from the internet",
            )
        )
    )
}