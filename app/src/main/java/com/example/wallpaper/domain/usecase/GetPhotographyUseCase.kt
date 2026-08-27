package com.example.wallpaper.domain.usecase

import com.example.wallpaper.domain.result.GetPhotographyResult
import com.example.wallpaper.domain.repository.PhotographyRepository

class GetPhotographyUseCase(
    private val photographyRepository: PhotographyRepository,
) {

    suspend operator fun invoke(): GetPhotographyResult = photographyRepository.getPhotography()
}