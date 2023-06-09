package com.example.beercompose.presentation.fragments.profile.util

data class ProfileFieldErrors(
    val changeNameError: String = "",
    val oldPasswordError: String = "",
    val newPasswordError: String = "",
    val repeatNewPasswordError: String = "",
    val deleteUserPasswordError: String = ""
)