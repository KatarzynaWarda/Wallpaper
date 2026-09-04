package com.example.wallpaper.presentation.photos.viewmodel

import com.example.wallpaper.MainDispatcherRule
import com.example.wallpaper.domain.model.Photography
import com.example.wallpaper.domain.result.GetPhotographyResult
import com.example.wallpaper.domain.usecase.GetPhotographyUseCase
import com.example.wallpaper.presentation.photos.uistate.PhotographyUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getPhotographyUseCase = mockk<GetPhotographyUseCase>()

    @Test
    fun `shows photography when loading succeeds`() = runTest {
        val photography = Photography(
            id = 1,
            url = "url",
            previewUrl = "preview",
            tags = listOf("nature"),
            author = "author",
        )
        coEvery { getPhotographyUseCase() } returns GetPhotographyResult.Success(
            listOf(photography),
        )

        val viewModel = PhotoViewModel(getPhotographyUseCase)
        advanceUntilIdle()

        assertEquals(
            PhotographyUiState.Content(listOf(photography)),
            viewModel.uiState.value,
        )
    }

    @Test
    fun `shows error when loading fails`() = runTest {
        coEvery { getPhotographyUseCase() } returns GetPhotographyResult.Error

        val viewModel = PhotoViewModel(getPhotographyUseCase)
        advanceUntilIdle()

        assertEquals(
            PhotographyUiState.Error("Something went wrong"),
            viewModel.uiState.value,
        )
    }
}
