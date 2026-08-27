package com.example.wallpaper.domain.usecase

import com.example.wallpaper.domain.model.EditedPhoto
import com.example.wallpaper.domain.repository.EditPhotoRepository

class GetEditedPhotoUseCase(
    private val editPhotoRepository: EditPhotoRepository,
) {

    suspend operator fun invoke(
        prompt: String,
        uri: String,
    ): EditedPhoto =
        editPhotoRepository.getEditedPhoto(
            prompt = prompt,
            uri = uri,
        )
}