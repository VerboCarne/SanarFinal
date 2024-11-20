package com.example.sanar

sealed interface UiState {
    object Initial : UiState
    object Loading : UiState
    data class Success(val messages: List<Message>) : UiState
    data class Error(val errorMessage: String) : UiState
}

data class Message(val text: String, val isUser: Boolean)