package com.example.beercompose.domain.use_cases.menu_items

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.data.network.dto.snack.SnackRequest
import com.example.beercompose.domain.repositories.MenuItemsRepository

class AddSnackUseCase(
    private val repository: MenuItemsRepository,
    private val context: Context
) {
    suspend operator fun invoke(snackRequest: SnackRequest): String {
        val response = repository.addApiSnack(snackRequest)
        response?.let {
            repository.addSnackToDatabase(it)
            return it.msg
        } ?: return context.getString(R.string.check_connection)

    }
}