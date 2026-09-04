package com.example.wallpaper.presentation.editor.viewmodel

import com.example.wallpaper.MainDispatcherRule
import com.example.wallpaper.TestData.EDITED_URI
import com.example.wallpaper.TestData.INITIAL_URI
import com.example.wallpaper.domain.model.EditedPhoto
import com.example.wallpaper.domain.result.SaveInGalleryResult
import com.example.wallpaper.domain.result.SetAsWallpaperResult
import com.example.wallpaper.domain.usecase.GetEditedPhotoUseCase
import com.example.wallpaper.domain.usecase.SaveInGalleryUseCase
import com.example.wallpaper.domain.usecase.SetAsWallpaperUseCase
import com.example.wallpaper.presentation.editor.uistate.PhotoEditorEffect
import com.example.wallpaper.presentation.editor.uistate.PhotoEditorUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoEditorViewModelTest {

    private companion object {
        const val PROMPT = "make it blue"
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val setAsWallpaperUseCase = mockk<SetAsWallpaperUseCase>()
    private val saveInGalleryUseCase = mockk<SaveInGalleryUseCase>()
    private val getEditedPhotoUseCase = mockk<GetEditedPhotoUseCase>()

    @Test
    fun `shows initial editor content`() {
        val viewModel = viewModel()

        assertEquals(expectedContent(), viewModel.state.value)
    }

    @Test
    fun `updates prompt in content state`() {
        val viewModel = viewModel()

        viewModel.onPromptChange(PROMPT)

        assertEquals(expectedContent(prompt = PROMPT), viewModel.state.value)
    }

    @Test
    fun `shows edited image and clears prompt when editing succeeds`() = runTest {
        coEvery { getEditedPhotoUseCase(PROMPT, INITIAL_URI) } returns EditedPhoto(EDITED_URI)
        val viewModel = viewModel()
        viewModel.onPromptChange(PROMPT)

        viewModel.onSendClick(INITIAL_URI)
        advanceUntilIdle()

        assertEquals(expectedContent(uri = EDITED_URI), viewModel.state.value)
        coVerify(exactly = 1) { getEditedPhotoUseCase(PROMPT, INITIAL_URI) }
    }

    @Test
    fun `shows error when editing throws IOException`() = runTest {
        coEvery {
            getEditedPhotoUseCase("", INITIAL_URI)
        } throws IOException("Editing failed")
        val viewModel = viewModel()

        viewModel.onSendClick(INITIAL_URI)
        advanceUntilIdle()

        assertEquals(
            PhotoEditorUiState.Error("Error message Editing failed"),
            viewModel.state.value,
        )
    }

    @Test
    fun `emits success effect when image is saved`() = runTest {
        coEvery { saveInGalleryUseCase(INITIAL_URI) } returns SaveInGalleryResult.Success
        val viewModel = viewModel()
        val effect = collectNextEffect(viewModel)

        viewModel.saveInGallery(INITIAL_URI)
        advanceUntilIdle()

        assertEquals(PhotoEditorEffect.ShowToast("Save success"), effect.await())
    }

    @Test
    fun `shows error when saving image fails`() = runTest {
        coEvery { saveInGalleryUseCase(INITIAL_URI) } returns SaveInGalleryResult.Error
        val viewModel = viewModel()

        viewModel.saveInGallery(INITIAL_URI)
        advanceUntilIdle()

        assertSomethingWentWrong(viewModel)
    }

    @Test
    fun `emits success effect when wallpaper is set`() = runTest {
        coEvery { setAsWallpaperUseCase(INITIAL_URI) } returns SetAsWallpaperResult.Success
        val viewModel = viewModel()
        val effect = collectNextEffect(viewModel)

        viewModel.setAsWallpaper(INITIAL_URI)
        advanceUntilIdle()

        assertEquals(PhotoEditorEffect.ShowToast("Set success"), effect.await())
    }

    @Test
    fun `shows error when setting wallpaper fails`() = runTest {
        coEvery { setAsWallpaperUseCase(INITIAL_URI) } returns SetAsWallpaperResult.Error
        val viewModel = viewModel()

        viewModel.setAsWallpaper(INITIAL_URI)
        advanceUntilIdle()

        assertSomethingWentWrong(viewModel)
    }

    private fun viewModel() = PhotoEditorViewModel(
        initialUri = INITIAL_URI,
        setAsWallpaperUseCase = setAsWallpaperUseCase,
        saveInGalleryUseCase = saveInGalleryUseCase,
        getEditedPhotoUseCase = getEditedPhotoUseCase,
    )

    private fun expectedContent(
        uri: String = INITIAL_URI,
        prompt: String = "",
    ) = PhotoEditorUiState.Content(
        uri = uri,
        prompt = prompt,
        title = "Edit photo",
        textField = "Enter a prompt",
        sendText = "Send",
        setWallpaperText = "Set as wallpaper",
        saveInGalleryText = "Save",
    )

    private fun assertSomethingWentWrong(viewModel: PhotoEditorViewModel) {
        assertEquals(
            PhotoEditorUiState.Error("something went wrong"),
            viewModel.state.value,
        )
    }

    private fun TestScope.collectNextEffect(
        viewModel: PhotoEditorViewModel,
    ): Deferred<PhotoEditorEffect> = async(UnconfinedTestDispatcher(testScheduler)) {
        viewModel.effect.first()
    }
}
