package com.example.wallpaper.data.repository

import com.example.wallpaper.data.mapper.PhotographyMapper
import com.example.wallpaper.data.network.PhotographyApi
import com.example.wallpaper.domain.result.GetPhotographyResult
import com.example.wallpaper.domain.repository.PhotographyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class PhotographyRepositoryImpl(
    private val api: PhotographyApi,
    private val photographyMapper: PhotographyMapper,
) : PhotographyRepository {

    override suspend fun getPhotography(): GetPhotographyResult =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getPhotography()
                val photographyList = response.hits.map { dto -> photographyMapper(dto) }

                GetPhotographyResult.Success(photographyList)
            } catch (_: IOException) {
                GetPhotographyResult.Error
            }
        }
}