package com.example.sanar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("home") }

    when (currentScreen) {
        "home" -> HomeScreen(
            onChatClick = { currentScreen = "chat" },
            onInfoClick = { currentScreen = "info" }
        )
        "chat" -> ChatScreen(
            onInfoClick = { currentScreen = "info" }  // Redirige al mismo InfoScreen
        )
        "info" -> InfoScreen(
            onBackClick = { currentScreen = "home" } // Vuelve a la pantalla principal
        )
    }
}