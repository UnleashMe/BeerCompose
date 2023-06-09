package com.example.beercompose.domain.use_cases.menu_validation

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.util.EmptyFieldException

class TitleValidationUseCase(
    private val context: Context
) {
    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw EmptyFieldException(context.getString(R.string.empty_field_exception))
        }
    }
}