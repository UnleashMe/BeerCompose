package com.example.beercompose.presentation.fragments.profile.util

sealed class VMStringResource {
    object NameChanged : VMStringResource()
    object PasswordChanged : VMStringResource()
    object AccountDeleted : VMStringResource()
    object Blanc : VMStringResource()
}