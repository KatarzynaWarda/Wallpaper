package com.example.wallpaper.domain.repository

import com.example.wallpaper.domain.result.GetPhotographyResult

interface PhotographyRepository {

    suspend fun getPhotography(): GetPhotographyResult
}