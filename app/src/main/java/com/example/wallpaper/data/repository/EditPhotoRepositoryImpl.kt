package com.example.wallpaper.data.repository

import android.content.ContentResolver
import androidx.core.net.toUri
import com.example.wallpaper.data.mapper.EditPhotoMapper
import com.example.wallpaper.data.network.EditPhotoApi
import com.example.wallpaper.domain.model.EditedPhoto
import com.example.wallpaper.domain.repository.EditPhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class EditPhotoRepositoryImpl(
    private val api: EditPhotoApi,
    private val editPhotoMapper: EditPhotoMapper,
    private val okHttpClient: OkHttpClient,
    private val contentResolver: ContentResolver,
): EditPhotoRepository {

    override suspend fun getEditedPhoto(
        prompt: String,
        uri: String,
    ): EditedPhoto = withContext(Dispatchers.IO) {

        val imageBytes = if (uri.toUri().scheme?.startsWith("http") == true) {
            val request = Request.Builder().url(uri).build()
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw Exception("Błąd pobierania obrazu: ${response.code}")
                response.body?.bytes() ?: throw Exception("Pusty plik obrazu")
            }
        } else {
            contentResolver.openInputStream(uri.toUri())?.use { it.readBytes() }
        } ?: throw IllegalArgumentException("Nie można odczytać zdjęcia")

        val promptBody = prompt.toRequestBody("text/plain".toMediaType())
        val imageBody = imageBytes.toRequestBody("image/jpeg".toMediaType())
        val imagePart = MultipartBody.Part.createFormData("image", "image.jpg", imageBody)

        val response = api.getEditedPhoto(
            prompt = promptBody,
            image = imagePart
        )

        editPhotoMapper(response)
    }
}