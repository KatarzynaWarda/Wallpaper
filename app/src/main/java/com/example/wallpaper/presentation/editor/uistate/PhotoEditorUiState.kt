package com.example.wallpaper.presentation.editor.uistate

sealed class PhotoEditorUiState {

    data object Loading: PhotoEditorUiState()

    data class Content(
        val uri: String?,
        val prompt: String,
        val title: String,
        val textField: String,
        val sendText: String,
        val setWallpaperText: String,
        val saveInGalleryText: String,
    ): PhotoEditorUiState()

    data class Error(val message: String): PhotoEditorUiState()
}

sealed class PhotoEditorEffect {
    data class ShowToast(val message: String) : PhotoEditorEffect()
}

