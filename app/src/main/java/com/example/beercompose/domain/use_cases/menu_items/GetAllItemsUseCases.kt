package com.example.beercompose.domain.use_cases.menu_items

import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.domain.model.DomainItem
import com.example.beercompose.domain.repositories.MenuItemsRepository
import kotlinx.coroutines.flow.*

class GetAllItemsUseCases(
    private val repository: MenuItemsRepository
) {
    operator fun invoke(category: MenuCategory? = null): Flow<List<DomainItem>> {
        category?.let { _category ->
            return flow {
                // filtering flow for 1st time launch when database is empty and first element in flow would be empty
                emit(repository.getDatabaseMenuItems().filter { it.isNotEmpty() }.first().filter {
                    it.category == _category
                })
            }
        } ?: return repository.getDatabaseMenuItems().filter { it.isNotEmpty() }
    }
}