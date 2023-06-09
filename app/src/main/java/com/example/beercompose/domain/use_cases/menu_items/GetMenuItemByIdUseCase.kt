package com.example.beercompose.domain.use_cases.menu_items

import com.example.beercompose.domain.model.DomainItem
import com.example.beercompose.domain.repositories.MenuItemsRepository

class GetMenuItemByIdUseCase(
    private val repository: MenuItemsRepository
) {
    suspend operator fun invoke(itemId: String): DomainItem {
        return repository.getMenuItemById(itemId)
    }
}