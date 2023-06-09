package com.example.beercompose.util

sealed class ScreenState {
    object Success : ScreenState()
    object Loading : ScreenState()
    object Error : ScreenState()
}