package com.example.beercompose.domain.use_cases.menu_validation

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.util.Constants.MAX_ALC
import com.example.beercompose.util.InvalidAlcPercentageException

class AlcPercentageValidationUseCase(
    private val context: Context
) {
    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw InvalidAlcPercentageException(context.getString(R.string.empty_field_exception))
        } else try {
            val alc = text.toDouble()
            if (alc > MAX_ALC) {
                throw InvalidAlcPercentageException(context.getString(R.string.high_alc_field))
            }
        } catch (e: java.lang.NumberFormatException) {
            throw InvalidAlcPercentageException(context.getString(R.string.invalid_input))
        }
    }
}