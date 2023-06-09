package com.example.beercompose.domain.use_cases.menu_validation

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.util.Constants.MAX_SALE_PERCENTAGE
import com.example.beercompose.util.InvalidSalePercentageException

class SalePercentageValidationUseCase(
    private val context: Context
) {
    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw InvalidSalePercentageException(context.getString(R.string.empty_field_exception))
        }
        try {
            val salePercentage = text.toDouble()
            if (salePercentage > MAX_SALE_PERCENTAGE) {
                throw InvalidSalePercentageException(context.getString(R.string.high_sale_percentage))
            }
        } catch (e: java.lang.NumberFormatException) {
            throw InvalidSalePercentageException(context.getString(R.string.invalid_input))
        }
    }
}