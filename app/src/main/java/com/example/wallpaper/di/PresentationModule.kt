package com.example.wallpaper.di

import com.example.wallpaper.data.repository.GalleryRepositoryImpl
import com.example.wallpaper.data.image.ImageRepository
import com.example.wallpaper.data.image.ImageRepositoryImpl
import com.example.wallpaper.data.repository.WallpaperRepositoryImpl
import com.example.wallpaper.domain.usecase.GetPhotographyUseCase
import com.example.wallpaper.domain.repository.GalleryRepository
import com.example.wallpaper.domain.usecase.GetEditedPhotoUseCase
import com.example.wallpaper.domain.usecase.SaveInGalleryUseCase
import com.example.wallpaper.domain.usecase.SetAsWallpaperUseCase
import com.example.wallpaper.domain.repository.WallpaperRepository
import com.example.wallpaper.presentation.editor.viewmodel.PhotoEditorViewModel
import com.example.wallpaper.presentation.main.viewmodel.MainViewModel
import com.example.wallpaper.presentation.photos.viewmodel.PhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    single<ImageRepository> {
        ImageRepositoryImpl(
            context = get()
        )
    }

    single<GalleryRepository> {
        GalleryRepositoryImpl(
            imageRepository = get(),
            contentResolver = get(),
        )
    }

    single<WallpaperRepository> {
        WallpaperRepositoryImpl(
            imageRepository = get(),
            context = get(),
        )
    }

    single {
        SetAsWallpaperUseCase(
            wallpaperRepository = get(),
        )
    }

    single {
        SaveInGalleryUseCase(
            galleryRepository = get(),
        )
    }

    single {
        GetEditedPhotoUseCase(
            editPhotoRepository = get()
        )
    }

    single {
        GetPhotographyUseCase(
            photographyRepository = get()
        )
    }

    viewModel {
        MainViewModel()
    }

    viewModel {
        PhotoViewModel(
            getPhotographyUseCase = get(),
        )
    }

    viewModel { params ->
        PhotoEditorViewModel(
            initialUri = params.get(),
            setAsWallpaperUseCase = get(),
            saveInGalleryUseCase = get(),
            getEditedPhotoUseCase = get(),
        )
    }
}