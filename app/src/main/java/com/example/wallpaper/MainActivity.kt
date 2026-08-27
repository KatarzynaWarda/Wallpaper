package com.example.wallpaper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wallpaper.navigation.Screen
import com.example.wallpaper.presentation.common.theme.WallpaperTheme
import com.example.wallpaper.presentation.editor.screen.PhotoEditorScreen
import com.example.wallpaper.presentation.editor.viewmodel.PhotoEditorViewModel
import com.example.wallpaper.presentation.main.screen.MainScreen
import com.example.wallpaper.presentation.main.viewmodel.MainViewModel
import com.example.wallpaper.presentation.photos.screen.PhotosScreen
import com.example.wallpaper.presentation.photos.viewmodel.PhotoViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WallpaperTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.Main.route) {
                    composable(Screen.Main.route) {
                        val viewModel: MainViewModel = koinViewModel<MainViewModel>()
                        val mainUiState by viewModel.uiState.collectAsStateWithLifecycle()
                        MainScreen(
                            uiState = mainUiState,
                            onLinkClick = {
                                navController.navigate(Screen.Photos.route)
                            },
                            saveImage = { uri ->
                                navController.navigate(Screen.createEditorRoute(uri.toString()))
                            }
                        )
                    }

                    composable(Screen.Photos.route) {
                        val viewModel: PhotoViewModel = koinViewModel<PhotoViewModel>()
                        val photographyState by viewModel.uiState.collectAsStateWithLifecycle()
                        PhotosScreen(
                            photographyUiState = photographyState,
                            onPhotoClick = { uri ->
                                navController.navigate(Screen.createEditorRoute(uri))
                            }
                        )
                    }

                    composable(Screen.Editor.route) { backStackEntry ->
                        val encodedUri = backStackEntry.arguments?.getString("photoUri")
                        val photoEditorViewModel: PhotoEditorViewModel =
                            koinViewModel<PhotoEditorViewModel>(parameters = { parametersOf(encodedUri.orEmpty()) })
                        val photoEditorUiState by photoEditorViewModel.state.collectAsStateWithLifecycle()

                        PhotoEditorScreen(
                            photoEditorUiState = photoEditorUiState,
                            effect = photoEditorViewModel.effect,
                            saveInGalleryClick = { uri ->
                                photoEditorViewModel.saveInGallery(uri = uri)
                            },
                            setAsWallpaperClick = { uri ->
                                photoEditorViewModel.setAsWallpaper(uri = uri)
                            },
                            onSendClick = { uri ->
                                photoEditorViewModel.onSendClick(uri = uri)
                            },
                            onPromptChanged = { prompt ->
                                photoEditorViewModel.onPromptChange(prompt)
                            },
                        )
                    }
                }
            }
        }
    }
}