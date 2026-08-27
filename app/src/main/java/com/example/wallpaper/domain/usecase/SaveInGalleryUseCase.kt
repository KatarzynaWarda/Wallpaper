package com.example.wallpaper.domain.usecase

import com.example.wallpaper.domain.result.SaveInGalleryResult
import com.example.wallpaper.domain.repository.GalleryRepository

class SaveInGalleryUseCase(
    private val galleryRepository: GalleryRepository
) {

    suspend operator fun invoke(uri: String): SaveInGalleryResult = galleryRepository.saveImage(uri)
}