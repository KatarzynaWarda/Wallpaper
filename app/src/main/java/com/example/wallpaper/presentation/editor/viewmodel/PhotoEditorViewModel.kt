package com.example.wallpaper.presentation.editor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallpaper.domain.result.SaveInGalleryResult
import com.example.wallpaper.domain.result.SetAsWallpaperResult
import com.example.wallpaper.domain.usecase.GetEditedPhotoUseCase
import com.example.wallpaper.domain.usecase.SaveInGalleryUseCase
import com.example.wallpaper.domain.usecase.SetAsWallpaperUseCase
import com.example.wallpaper.presentation.editor.uistate.PhotoEditorEffect
import com.example.wallpaper.presentation.editor.uistate.PhotoEditorUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException

class PhotoEditorViewModel(
    private val initialUri: String,
    private val setAsWallpaperUseCase: SetAsWallpaperUseCase,
    private val saveInGalleryUseCase: SaveInGalleryUseCase,
    private val getEditedPhotoUseCase: GetEditedPhotoUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<PhotoEditorUiState>(PhotoEditorUiState.Loading)
    var state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PhotoEditorEffect>()
    val effect = _effect.asSharedFlow()

    init {
        _state.value = contentUiState()
    }

    fun onPromptChange(newPrompt: String) {
        _state.update { currentState ->
            if (currentState is PhotoEditorUiState.Content) {
                currentState.copy(prompt = newPrompt)
            } else {
                currentState
            }
        }
    }

    fun onSendClick(uri: String) =
        viewModelScope.launch {
            val lastContent = _state.value as? PhotoEditorUiState.Content
            val currentPrompt = lastContent?.prompt ?: ""

            _state.value = PhotoEditorUiState.Loading

            try {
                val editedPhoto = getEditedPhotoUseCase(
                    prompt = currentPrompt,
                    uri = uri,
                )
                val newContent = lastContent?.copy(
                    uri = editedPhoto.imageUrl,
                    prompt = "",
                ) ?: contentUiState().copy(uri = editedPhoto.imageUrl)

                _state.value = newContent
            } catch (e: IOException) {
                _state.value = PhotoEditorUiState.Error("Error message ${e.message}")
            }
        }

    fun saveInGallery(uri: String) {
        viewModelScope.launch {
            when (saveInGalleryUseCase(uri)) {
                SaveInGalleryResult.Success -> {
                    _effect.emit(
                        PhotoEditorEffect.ShowToast("Save success")
                    )
                }

                SaveInGalleryResult.Error -> {
                    _state.value = PhotoEditorUiState.Error(message = "something went wrong")
                }
            }
        }
    }

    fun setAsWallpaper(uri: String) {
        viewModelScope.launch {
            when (setAsWallpaperUseCase(uri)) {
                SetAsWallpaperResult.Success -> {
                    _effect.emit(
                        PhotoEditorEffect.ShowToast("Set success")
                    )
                }

                SetAsWallpaperResult.Error -> {
                    _state.value = PhotoEditorUiState.Error(message = "something went wrong")
                }

            }
        }
    }

    private fun contentUiState() = PhotoEditorUiState.Content(
        uri = initialUri,
        prompt = "",
        title = "Edit photo",
        textField = "Enter a prompt",
        sendText = "Send",
        setWallpaperText = "Set as wallpaper",
        saveInGalleryText = "Save",
    )
}