package com.example.beercompose.domain.use_cases.user_validation

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.util.PasswordException

class PasswordValidationUseCase(
    private val context: Context
) {
    operator fun invoke(text: String) {
        if (text.length in 1..5) {
            throw PasswordException.NewPasswordException(context.getString(R.string.short_password_exception))
        }
    }
}