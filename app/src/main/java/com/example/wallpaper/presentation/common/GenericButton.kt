package com.example.wallpaper.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wallpaper.R
import com.example.wallpaper.presentation.common.theme.Purple600
import com.example.wallpaper.presentation.common.theme.White

@Composable
fun GenericButton(
    modifier: Modifier = Modifier,
    buttonColor: Color,
    buttonTitle: String,
    buttonSubtitle: String? = null,
    buttonIcon: Int,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors().copy(containerColor = buttonColor),
        modifier = modifier
            .padding(bottom = 20.dp)
            .height(100.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 40.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        0.0f to buttonColor,
                        0.9f to White.copy(alpha = 0.3f),
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                painter = painterResource(buttonIcon),
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(70.dp)
            )
            Column {
                Text(
                    text = buttonTitle,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = Medium)
                )
                if (buttonSubtitle != null) {
                    Text(
                        text = buttonSubtitle,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    GenericButton(
        buttonColor = Purple600,
        buttonTitle = "From gallery",
        buttonSubtitle = "Choose from your device",
        buttonIcon = R.drawable.ic_add_photo,
        onClick = {},
    )
}