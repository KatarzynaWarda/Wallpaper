package com.example.wallpaper.domain.result

import com.example.wallpaper.domain.model.Photography

sealed interface GetPhotographyResult {
    data class Success(val photographyList:  List<Photography>) : GetPhotographyResult
    data object Error : GetPhotographyResult
}