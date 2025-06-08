package com.example.comics.di

import com.example.comics.data.remote.Api
import com.example.comics.data.repository.ComicRepository
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single { Gson() }

    single {
        Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/v1/public/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<Api> { get<Retrofit>().create(Api::class.java) }

    single { ComicRepository(get()) }
}