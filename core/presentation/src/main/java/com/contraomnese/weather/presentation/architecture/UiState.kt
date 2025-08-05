package com.contraomnese.weather.presentation.architecture

interface UiState {
    val isLoading: Boolean
    fun loading(): UiState
}