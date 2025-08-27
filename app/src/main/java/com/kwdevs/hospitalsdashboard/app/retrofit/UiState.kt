package com.kwdevs.hospitalsdashboard.app.retrofit

import com.kwdevs.hospitalsdashboard.responses.errors.ErrorResponse


sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val exception: ErrorResponse) : UiState<Nothing>()
    data object Reload : UiState<Nothing>()
}