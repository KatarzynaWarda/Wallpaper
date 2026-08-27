package com.example.wallpaper.di

import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        dataModule,
        presentationModule,
    )
}