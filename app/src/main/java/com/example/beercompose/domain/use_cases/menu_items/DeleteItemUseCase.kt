package com.example.beercompose.domain.use_cases.menu_items

import android.content.Context
import com.example.beercompose.R
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.domain.repositories.MenuItemsRepository

class DeleteItemUseCase(
    private val repository: MenuItemsRepository,
    private val context: Context
) {
    suspend operator fun invoke(category: MenuCategory, itemId: String): String {
        val response = if (category == MenuCategory.BeerCategory) {
            repository.deleteApiBeer(itemId)?.msg
        } else repository.deleteApiSnack(itemId)?.msg
        response?.let {
            return response
        } ?: return context.getString(R.string.check_connection)
    }
}