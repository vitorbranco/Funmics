package com.example.comics.domain.usecases

import com.example.comics.data.model.ComicModel
import com.example.comics.data.repository.ComicRepository
import com.example.comics.util.Result
import com.example.comics.util.safeRunDispatcher

class GetComicsUseCase(
    private val comicRepository: ComicRepository
) {
    suspend operator fun invoke(): Result<ComicModel> {
        return safeRunDispatcher { comicRepository.getComics() }
    }
}