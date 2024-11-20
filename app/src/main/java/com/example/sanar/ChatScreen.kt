package com.example.sanar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatScreen(
    conversationViewModel: ConversationViewModel = viewModel(),
    onInfoClick: () -> Unit // Navegación hacia InfoScreen
) {
    var prompt by rememberSaveable { mutableStateOf("") }
    val uiState by conversationViewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }

    LaunchedEffect(uiState) {
        val successState = uiState as? UiState.Success
        if (successState != null) {
            messages = successState.messages
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFB3E5FC), Color(0xFF64B5F6)) // Degradado azul claro a oscuro
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(messages) { message ->
                    ChatMessage(message)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                TextField(
                    value = prompt,
                    onValueChange = { newValue -> prompt = newValue },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .align(Alignment.CenterVertically),
                    placeholder = { Text("Escribe tu mensaje...", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.Black),
                    singleLine = true
                )

                Button(
                    onClick = {
                        conversationViewModel.sendPrompt(prompt)
                        prompt = ""
                    },
                    enabled = prompt.isNotEmpty(),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
                ) {
                    Text("Enviar", color = Color.White)
                }
            }

            if (uiState is UiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
            } else if (uiState is UiState.Error) {
                val errorState = uiState as? UiState.Error
                Text(
                    text = errorState?.errorMessage ?: "Ocurrió un problema desconocido.",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
            }

            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Menú")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Nueva conversación") },
                    onClick = {
                        conversationViewModel.startNewConversation()
                        expanded = false
                    },
                    leadingIcon = { Icon(Icons.Filled.Refresh, contentDescription = null) }
                )

                conversationViewModel.conversations.collectAsState().value.forEach { conversation ->
                    DropdownMenuItem(
                        text = { Text(conversation.title) },
                        onClick = {
                            conversationViewModel.loadConversation(conversation.id)
                            expanded = false
                        }
                    )
                }

                Divider()

                DropdownMenuItem(
                    text = { Text("Información de la App") },
                    onClick = {
                        onInfoClick() // Redirige a InfoScreen
                        expanded = false
                    },
                    leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) }
                )
            }
        }
    }
}

@Composable
fun ChatMessage(message: Message) {
    val horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isUser) Color(0xFFBBDEFB) else Color(0xFFC8E6C9)
    val textColor = if (message.isUser) Color.Black else Color.DarkGray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = horizontalAlignment,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = message.text,
                color = textColor,
                style = TextStyle(fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                    .padding(12.dp)
            )
        }
    }
}











