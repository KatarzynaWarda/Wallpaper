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
        title = "Kreator Tapet",
        subtitle = "Stwórz unikalną tapetę z AI",
        sectionTitle = "Wybierz zdjęcie",
        sectionSubtitle = "Dodaj obraz, który chcesz przekształcić",
        wallpaperButtons = listOf(
            WallpaperButtons(
                title = "Z galerii",
                subtitle = "Wybierz z telefonu",
            ),
            WallpaperButtons(
                title = "Zrób zdjęcie",
                subtitle = "Użyj aparatu",
            ),
            WallpaperButtons(
                title = "Z linku",
                subtitle = "Pobierz z internetu",
            )
        )
    )
}