package com.example.comics.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comics.domain.usecases.GetComicsUseCase
import com.example.comics.ui.ComicItemVO
import com.example.comics.ui.mapper.toVOList
import com.example.comics.util.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

sealed class ComicsViewState {
    object Loading : ComicsViewState()
    object Fail : ComicsViewState()
    object EmptyComics : ComicsViewState()
    data class UpdatedComics(
        val comics: List<ComicItemVO>
    ) : ComicsViewState()
}

class ComicsHomeViewModel(
    private val getComicsUseCase: GetComicsUseCase
) : ViewModel() {

    private val _comicsViewState = MutableSharedFlow<ComicsViewState>(replay = 1)
    val comicsViewState: SharedFlow<ComicsViewState> = _comicsViewState

    fun getComics() {
        viewModelScope.launch {
            _comicsViewState.emit(ComicsViewState.Loading)

            when (val result = getComicsUseCase()) {
                is Result.Success -> {
                    val comicsList = result.data.toVOList()

                    if (comicsList.isEmpty()) {
                        _comicsViewState.emit(ComicsViewState.EmptyComics)
                    } else {
                        _comicsViewState.emit(
                            ComicsViewState.UpdatedComics(comicsList)
                        )
                    }
                }

                is Result.Failure -> _comicsViewState.emit(ComicsViewState.Fail)
            }
        }
    }
}