package com.example.beercompose.domain.use_cases.user_validation

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.util.InvalidNumberException

class PhoneNumberValidationUseCase(
    private val context: Context
) {
    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw InvalidNumberException(context.getString(R.string.empty_field_exception))
        }
    }
}