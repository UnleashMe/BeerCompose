package com.example.beercompose.presentation.fragments.login.util

data class LoginState(
    val inputFields: LoginInputFields = LoginInputFields(),
    val errors: LoginFieldsErrors = LoginFieldsErrors(),
    val isButtonEnabled: Boolean = false
)
