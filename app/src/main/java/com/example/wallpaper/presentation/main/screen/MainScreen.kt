package com.example.wallpaper.presentation.main.screen

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.wallpaper.R
import com.example.wallpaper.presentation.common.GenericButton
import com.example.wallpaper.presentation.common.theme.Gray
import com.example.wallpaper.presentation.common.theme.LightGray
import com.example.wallpaper.presentation.common.theme.Orange400
import com.example.wallpaper.presentation.common.theme.Pink500
import com.example.wallpaper.presentation.common.theme.Purple600
import com.example.wallpaper.presentation.common.theme.WallpaperTheme
import com.example.wallpaper.presentation.common.theme.White
import com.example.wallpaper.presentation.main.uistate.MainUiState
import com.example.wallpaper.presentation.main.uistate.WallpaperButtons
import java.io.File

@Composable
fun MainScreen(
    saveImage: (Uri) -> Unit,
    onLinkClick: () -> Unit,
    uiState: MainUiState,
) {

    val context = LocalContext.current
    var cameraImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            saveImage(uri)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->

        val uri = cameraImageUri

        if (success && uri != null) {
            saveImage(uri)
        }
    }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                cameraImageUri?.let {
                    cameraLauncher.launch(it)
                }
            }
        }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    0.0f to Purple600,
                    0.5f to Pink500,
                    1f to Orange400,
                )
            )
            .padding(top = 50.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = White.copy(alpha = 0.20f),
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_spa),
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(70.dp)
            )
        }

        Text(
            text = uiState.title,
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = Bold,
                color = White,
            ),
            modifier = Modifier.padding(top = 40.dp, bottom = 15.dp)
        )

        Text(
            text = uiState.subtitle,
            style = MaterialTheme.typography.titleLarge.copy(
                color = White,
            ),
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Column(
            modifier = Modifier
                .background(
                    color = White,
                    shape = RoundedCornerShape(topEnd = 40.dp, topStart = 40.dp)
                )
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = uiState.sectionTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = Medium,
                    color = Gray,
                ),
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Text(
                text = uiState.sectionSubtitle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = LightGray,
                ),
                modifier = Modifier.padding(bottom = 30.dp)
            )
            GenericButton(
                buttonColor = Purple600,
                buttonTitle = uiState.wallpaperButtons[0].title,
                buttonSubtitle = uiState.wallpaperButtons[0].subtitle,
                buttonIcon = R.drawable.ic_add_photo,
                onClick = { galleryLauncher.launch("image/*") },
            )
            GenericButton(
                buttonColor = Pink500,
                buttonTitle = uiState.wallpaperButtons[1].title,
                buttonSubtitle = uiState.wallpaperButtons[1].subtitle,
                buttonIcon = R.drawable.ic_take_photo,
                onClick = {
                    val file =
                        File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
                    val uri = FileProvider.getUriForFile(context,
                        "com.example.wallpaper.provider",
                        file
                    )
                    cameraImageUri = uri
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                },
            )
            GenericButton(
                buttonColor = Orange400,
                buttonTitle = uiState.wallpaperButtons[2].title,
                buttonSubtitle = uiState.wallpaperButtons[2].subtitle,
                buttonIcon = R.drawable.ic_link,
                onClick = onLinkClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    WallpaperTheme {
        MainScreen(
            saveImage = {},
            onLinkClick = {},uiState = MainUiState(
                title = "Wallpaper Creator",
                subtitle = "Create a unique wallpaper with AI",
                sectionTitle = "Choose a photo",
                sectionSubtitle = "Add an image you want to transform",
                wallpaperButtons = listOf(
                    WallpaperButtons(
                        title = "From gallery",
                        subtitle = "Choose from your device",
                    ),
                    WallpaperButtons(
                        title = "Take a photo",
                        subtitle = "Use the camera",
                    ),
                    WallpaperButtons(
                        title = "From the web",
                        subtitle = "Download from the internet",
                    )
                )
            )
        )
    }
}