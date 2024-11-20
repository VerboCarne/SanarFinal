package com.example.sanar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onChatClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6)), // Fondo claro
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Texto principal
        Text(
            text = "Bienvenido a SANAR!",
            fontSize = 24.sp,
            color = Color(0xFF4C9A6A), // Verde similar
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Botón "Chatear"
        Button(
            onClick = onChatClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC5E1A5) // Verde pálido
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Chatear", color = Color.Black, fontSize = 16.sp)
        }

        // Botón "Info"
        Button(
            onClick = onInfoClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFF59D) // Amarillo pálido
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Info", color = Color.Black, fontSize = 16.sp)
        }
    }
}
