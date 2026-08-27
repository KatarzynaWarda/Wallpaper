package com.example.wallpaper.data.mapper

import com.example.wallpaper.data.dto.EditPhotoResponseDto
import com.example.wallpaper.domain.model.EditedPhoto

class EditPhotoMapper {

    operator fun invoke(dto: EditPhotoResponseDto) =
        EditedPhoto(
            imageUrl = dto.imageUrl
        )
}