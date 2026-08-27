package com.example.wallpaper.data.network

import com.example.wallpaper.data.dto.EditPhotoResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface EditPhotoApi {

    @Multipart
    @POST("edit-image")
    suspend fun getEditedPhoto(
        @Part("prompt") prompt: RequestBody,
        @Part image: MultipartBody.Part
    ): EditPhotoResponseDto
}