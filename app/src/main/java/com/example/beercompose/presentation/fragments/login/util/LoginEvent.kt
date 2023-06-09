package com.example.beercompose.presentation.fragments.login.util

sealed class LoginEvent {
    data class OnNumberInput(val text: String) : LoginEvent()
    data class OnPasswordInput(val text: String) : LoginEvent()
    object Login : LoginEvent()
}
