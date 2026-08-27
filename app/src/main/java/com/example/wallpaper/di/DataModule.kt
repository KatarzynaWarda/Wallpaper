package com.example.wallpaper.di

import android.content.ContentResolver
import com.example.wallpaper.data.mapper.EditPhotoMapper
import com.example.wallpaper.data.repository.EditPhotoRepositoryImpl
import com.example.wallpaper.data.mapper.PhotographyMapper
import com.example.wallpaper.data.repository.PhotographyRepositoryImpl
import com.example.wallpaper.domain.repository.PhotographyRepository
import com.example.wallpaper.domain.repository.EditPhotoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<ContentResolver> {
        androidContext().contentResolver
    }

    single<PhotographyRepository> {
        PhotographyRepositoryImpl(
            api = get(),
            photographyMapper = PhotographyMapper()
        )
    }

    single<EditPhotoRepository> {
        EditPhotoRepositoryImpl(
            api = get(),
            editPhotoMapper = EditPhotoMapper(),
            okHttpClient = get(),
            contentResolver = get(),
        )
    }
}