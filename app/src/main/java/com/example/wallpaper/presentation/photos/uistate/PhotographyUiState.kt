package com.example.wallpaper.presentation.photos.uistate

import com.example.wallpaper.domain.model.Photography

sealed class PhotographyUiState {
    data class Content(
        val photography: List<Photography>
    ) : PhotographyUiState()

    data object Loading : PhotographyUiState()

    data class Error(val message: String) : PhotographyUiState()
}