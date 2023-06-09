package com.example.beercompose.presentation.fragments.profile.util

sealed class ProfileEvent {
    data class OnChangeNameInput(val text: String) : ProfileEvent()
    data class OnOldPasswordInput(val text: String) : ProfileEvent()
    data class OnNewPasswordInput(val text: String) : ProfileEvent()
    data class OnRepeatNewPasswordInput(val text: String) : ProfileEvent()
    data class OnDeletePasswordInput(val text: String) : ProfileEvent()
    object ChangeName : ProfileEvent()
    object ChangePassword : ProfileEvent()
    object DeleteUser : ProfileEvent()
    object Logout : ProfileEvent()
}
