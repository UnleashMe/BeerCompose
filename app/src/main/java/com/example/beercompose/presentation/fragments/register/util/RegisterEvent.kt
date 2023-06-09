package com.example.beercompose.presentation.fragments.register.util

sealed class RegisterEvent {
    data class OnNumberInput(val text: String) : RegisterEvent()
    data class OnNameInput(val text: String) : RegisterEvent()
    data class OnPasswordInput(val text: String) : RegisterEvent()
    data class OnRepeatPasswordInput(val text: String) : RegisterEvent()
    object Register : RegisterEvent()
}