package com.example.beercompose.presentation.fragments.itemlist.util

import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.domain.model.DomainItem
import com.example.beercompose.util.ScreenState
import com.example.beercompose.domain.model.User

data class MenuItemListState(
    val menuItems: List<DomainItem> = emptyList(),
    val likedItems: List<String> = emptyList(),
    val category: MenuCategory = MenuCategory.BeerCategory,
    val screenState: ScreenState = ScreenState.Loading,
    val inCartItems: List<InCartItem> = emptyList(),
    val user: User = User(),
    val searchField: String = "",
    val backingList: List<DomainItem> = emptyList()
)
