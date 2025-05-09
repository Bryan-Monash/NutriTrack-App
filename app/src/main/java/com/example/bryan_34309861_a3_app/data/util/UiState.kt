package com.example.bryan_34309861_a3_app.data.util

sealed interface UiState {

    object Initial: UiState

    object Loading: UiState

    data class Success(val outputText: String) : UiState

    data class Error(val errorMessage: String) : UiState
}