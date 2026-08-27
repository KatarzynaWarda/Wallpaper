package com.example.wallpaper.data.dto

import com.google.gson.annotations.SerializedName

data class PixabayResponseDto(
    val total: Int,
    val totalHits: Int,
    val hits: List<PhotographyDto>
)

data class PhotographyDto(
    val id: Int,
    val tags: String,
    val previewURL: String,
    val webformatURL: String,
    val largeImageURL: String,
    val user: String,
    @SerializedName("userImageURL") val userImageURL: String
)