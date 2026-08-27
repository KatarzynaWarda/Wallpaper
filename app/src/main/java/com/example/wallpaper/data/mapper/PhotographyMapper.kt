package com.example.wallpaper.data.mapper

import com.example.wallpaper.data.dto.PhotographyDto
import com.example.wallpaper.domain.model.Photography

class PhotographyMapper {

    operator fun invoke(dto: PhotographyDto) =
        Photography(
            id = dto.id,
            url = dto.largeImageURL,
            previewUrl = dto.previewURL,
            tags = dto.tags.split(",").map { it.trim() },
            author = dto.user
        )
}