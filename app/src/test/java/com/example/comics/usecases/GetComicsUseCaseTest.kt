package com.example.comics.usecases

import com.example.comics.data.model.ComicModel
import com.example.comics.data.repository.ComicRepository
import com.example.comics.domain.usecases.GetComicsUseCase
import com.example.comics.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class GetComicsUseCaseTest {

    private val repository = mockk<ComicRepository>()
    private val useCase = GetComicsUseCase(repository)

    @Test
    fun `invoke returns success when repository returns data`() = runTest {
        val expectedModel = mockk<ComicModel>()
        coEvery { repository.getComics() } returns expectedModel

        val result = useCase()

        assert(result is Result.Success)
        assertEquals(expectedModel, (result as Result.Success).data)
    }

    @Test
    fun `invoke returns failure when repository throws exception`() = runTest {
        val exception = RuntimeException(REPOSITORY_FAILURE)
        coEvery { repository.getComics() } throws exception

        val result = useCase()

        assert(result is Result.Failure)
        assertEquals(exception.message, (result as Result.Failure).error.message)
    }

    private companion object {
        const val REPOSITORY_FAILURE = "Repository failure"
    }
}