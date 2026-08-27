package com.example.wallpaper.di

import com.example.wallpaper.data.network.EditPhotoApi
import com.example.wallpaper.data.network.PhotographyApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<PhotographyApi> {
        get<Retrofit>().create(PhotographyApi::class.java)
    }

    single<EditPhotoApi> {
        get<Retrofit>().create(EditPhotoApi::class.java)
    }
}