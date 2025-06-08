package com.example.comics.viewmodel

import app.cash.turbine.test
import com.example.comics.CoroutinesTestRule
import com.example.comics.data.model.ComicModel
import com.example.comics.data.model.DataModel
import com.example.comics.data.model.ResultModel
import com.example.comics.data.model.ThumbnailModel
import com.example.comics.domain.usecases.GetComicsUseCase
import com.example.comics.ui.mapper.toVOList
import com.example.comics.ui.viewmodel.ComicsHomeViewModel
import com.example.comics.ui.viewmodel.ComicsViewState
import com.example.comics.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ComicsHomeViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private val getComicsUseCase: GetComicsUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ComicsHomeViewModel

    @Before
    fun setUp() {
        viewModel = ComicsHomeViewModel(getComicsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getComics emits Loading and UpdatedComics on success`() = runTest {
        val comicModel = ComicModel(
            DataModel(
                results = listOf(
                    ResultModel(
                        id = 1,
                        title = "Spider-man",
                        description = "Peter wanted to travel this weekend but Dr Octopus has different plans",
                        thumbnail = ThumbnailModel("https://image.url", ".jpg")
                    ),
                    ResultModel(
                        id = 2,
                        title = "Iron man",
                        description = "Stark industries are on the deadline",
                        thumbnail = ThumbnailModel("https://image.url", ".jpg")
                    ),
                    ResultModel(
                        id = 3,
                        title = "Thor",
                        description = "Not even his hammer can help him against Loki's army",
                        thumbnail = ThumbnailModel("https://image.url", ".jpg")
                    )
                )
            )
        )

        coEvery { getComicsUseCase() } returns Result.Success(comicModel)

        val expectedList = comicModel.toVOList()

        viewModel.comicsViewState.test {
            viewModel.getComics()

            assertEquals(ComicsViewState.Loading, awaitItem())
            assertEquals(ComicsViewState.UpdatedComics(expectedList), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getComics emits Loading and UpdatedComics with empty list`() = runTest {
        val emptyList = ComicModel(data = DataModel(results = emptyList()))
        coEvery { getComicsUseCase() } returns Result.Success(emptyList)

        viewModel.comicsViewState.test {
            viewModel.getComics()

            assertEquals(ComicsViewState.Loading, awaitItem())
            assertEquals(ComicsViewState.EmptyComics, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getComics emits Loading and Fail on failure`() = runTest {
        coEvery { getComicsUseCase() } returns Result.Failure(Exception(MOCK_EXCEPTION))

        viewModel.comicsViewState.test {
            viewModel.getComics()

            assertEquals(ComicsViewState.Loading, awaitItem())
            assertEquals(ComicsViewState.Fail, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private companion object {
        const val MOCK_EXCEPTION = "Exception mock"
    }
}