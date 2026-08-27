package com.example.wallpaper.domain.model

data class Photography(
    val id: Int,
    val url: String,
    val previewUrl: String,
    val tags: List<String>,
    val author: String,
)