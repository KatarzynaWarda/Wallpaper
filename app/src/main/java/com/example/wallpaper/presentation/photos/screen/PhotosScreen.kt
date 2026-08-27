package com.example.wallpaper.presentation.photos.screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.wallpaper.presentation.common.ErrorScreen
import com.example.wallpaper.presentation.common.LoadingScreen
import com.example.wallpaper.presentation.photos.uistate.PhotographyUiState


@Composable
fun PhotosScreen(
    photographyUiState: PhotographyUiState,
    onPhotoClick: (String) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (photographyUiState) {
            is PhotographyUiState.Loading -> {
                LoadingScreen()
            }

            is PhotographyUiState.Content -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(photographyUiState.photography) { photo ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .height(200.dp)
                                .clickable{ onPhotoClick(photo.url) },
                        ) {
                            AsyncImage(
                                model = photo.previewUrl,
                                contentDescription = photo.tags.firstOrNull(),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            is PhotographyUiState.Error -> {
                ErrorScreen(photographyUiState.message)
            }
        }
    }
}