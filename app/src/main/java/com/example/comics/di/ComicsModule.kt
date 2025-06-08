package com.example.comics.di

import com.example.comics.domain.usecases.GetComicsUseCase
import com.example.comics.ui.viewmodel.ComicsHomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val comicsModule = module {
    single { GetComicsUseCase(get()) }

    viewModel { ComicsHomeViewModel(get()) }
}