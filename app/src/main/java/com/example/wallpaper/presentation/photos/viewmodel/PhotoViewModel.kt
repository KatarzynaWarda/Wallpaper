package com.example.wallpaper.presentation.photos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallpaper.domain.result.GetPhotographyResult
import com.example.wallpaper.domain.usecase.GetPhotographyUseCase
import com.example.wallpaper.presentation.photos.uistate.PhotographyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoViewModel(
    private val getPhotographyUseCase: GetPhotographyUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhotographyUiState>(PhotographyUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        showPhotography()
    }

    fun showPhotography() = viewModelScope.launch {
        when (val result = getPhotographyUseCase()) {
            is GetPhotographyResult.Success -> {
                _uiState.value = PhotographyUiState.Content(result.photographyList)
            }

            GetPhotographyResult.Error -> {
                _uiState.value = PhotographyUiState.Error("Something went wrong")
            }
        }
    }
}