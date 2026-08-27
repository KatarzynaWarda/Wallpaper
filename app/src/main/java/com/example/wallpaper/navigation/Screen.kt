package com.example.wallpaper.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Photos : Screen("photos")
    object Editor : Screen("editor/{photoUri}")

    companion object {
        fun createEditorRoute(uri: String): String {
            val encodedUri = Uri.encode(uri)
            return "editor/$encodedUri"
        }
    }
}