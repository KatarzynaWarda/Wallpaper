package com.example.wallpaper.data.network

import com.example.wallpaper.data.dto.PixabayResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotographyApi {

    @GET("photography")
    suspend fun getPhotography(
        @Query("q") query: String = "nature",
        @Query("image_type") imageType: String = "photo"
    ): PixabayResponseDto
}