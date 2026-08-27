package com.example.wallpaper.domain.repository

import com.example.wallpaper.domain.model.EditedPhoto

interface EditPhotoRepository {

    suspend fun getEditedPhoto(
        prompt: String,
        uri: String,
    ): EditedPhoto
}