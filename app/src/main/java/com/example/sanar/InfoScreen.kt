package com.example.sanar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Texto principal
        Text(
            text = "Prefiere Sanar para encontrarte \ny descubrir por qué te sientes \ncomo te sientes.",
            fontSize = 18.sp,
            color = Color(0xFF4C9A6A), // Verde suave
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xFFFFF59D), RoundedCornerShape(16.dp))
                .padding(24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Texto del desarrollador
        Text(
            text = "Desarrollador: Fabio Hidalgo Meléndez",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para regresar a la pantalla principal
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC5E1A5)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Volver", fontSize = 16.sp, color = Color.Black)
        }
    }
}

