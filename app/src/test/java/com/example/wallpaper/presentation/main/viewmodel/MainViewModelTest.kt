package com.example.wallpaper.presentation.main.viewmodel

import com.example.wallpaper.presentation.main.uistate.MainUiState
import com.example.wallpaper.presentation.main.uistate.WallpaperButtons
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {

    @Test
    fun `exposes main screen content`() {
        val viewModel = MainViewModel()

        assertEquals(
            MainUiState(
                title = "Wallpaper Creator",
                subtitle = "Create a unique wallpaper with AI",
                sectionTitle = "Choose a photo",
                sectionSubtitle = "Add an image you want to transform",
                wallpaperButtons = listOf(
                    WallpaperButtons("From gallery", "Choose from your device"),
                    WallpaperButtons("Take a photo", "Use the camera"),
                    WallpaperButtons("From the web", "Download from the internet"),
                ),
            ),
            viewModel.uiState.value,
        )
    }
}
