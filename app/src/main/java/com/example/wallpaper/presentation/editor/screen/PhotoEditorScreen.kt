package com.example.wallpaper.presentation.editor.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.wallpaper.R
import com.example.wallpaper.presentation.common.ErrorScreen
import com.example.wallpaper.presentation.common.LoadingScreen
import com.example.wallpaper.presentation.common.theme.Primary
import com.example.wallpaper.presentation.editor.uistate.PhotoEditorEffect
import com.example.wallpaper.presentation.editor.uistate.PhotoEditorUiState
import kotlinx.coroutines.flow.Flow

@Composable
fun PhotoEditorScreen(
    photoEditorUiState: PhotoEditorUiState,
    effect: Flow<PhotoEditorEffect>,
    saveInGalleryClick: (String) -> Unit,
    setAsWallpaperClick: (String) -> Unit,
    onSendClick: (String) -> Unit,
    onPromptChanged: (String) -> Unit,
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        effect.collect { effect ->
            when (effect) {
                is PhotoEditorEffect.ShowToast -> {
                    Toast.makeText(
                        context,
                        effect.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    when (photoEditorUiState) {
        is PhotoEditorUiState.Error -> {
            ErrorScreen(photoEditorUiState.message)
        }

        PhotoEditorUiState.Loading -> {
            LoadingScreen()
        }

        is PhotoEditorUiState.Content -> {

            val imageUri = photoEditorUiState.uri.orEmpty()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = photoEditorUiState.title,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = Bold,
                        color = Primary,
                    ),
                    modifier = Modifier.padding(top = 40.dp, bottom = 15.dp)
                )
                AsyncImage(
                    model = imageUri,
                    contentDescription = "description",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f),
                    contentScale = ContentScale.Crop
                )

                OutlinedTextField(
                    value = photoEditorUiState.prompt,
                    onValueChange = onPromptChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(photoEditorUiState.textField)
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { onSendClick(imageUri) },
                            enabled = true
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_outline_schedule_send),
                                contentDescription = "Wyślij"
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        modifier = Modifier.wrapContentSize(),
                        colors = ButtonDefaults.buttonColors().copy(containerColor = Primary),
                        onClick = { setAsWallpaperClick(imageUri) },
                    ) {
                        Text(
                            text = photoEditorUiState.setWallpaperText,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = Medium)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier.wrapContentSize(),
                        colors = ButtonDefaults.buttonColors().copy(containerColor = Primary),
                        onClick = { saveInGalleryClick(imageUri) },
                    ) {
                        Text(
                            text = photoEditorUiState.saveInGalleryText,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = Medium)
                        )
                    }
                }
            }
        }
    }
}