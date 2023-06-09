package com.example.beercompose.presentation.fragments.itemlist.util

import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.domain.model.InCartItem

sealed class ItemListEvent {
    data class GetItemList(val category: MenuCategory) : ItemListEvent()
    data class DeleteItem(val id: String, val category: MenuCategory) : ItemListEvent()
    object GetLikes : ItemListEvent()
    data class OnFavClick(val id: String) : ItemListEvent()
    data class OnPlusClick(val inCartItem: InCartItem) : ItemListEvent()
    data class OnMinusClick(val inCartItem: InCartItem) : ItemListEvent()
    data class AddItemToCart(val id: String) : ItemListEvent()
    data class UpdateSearchField(val text: String) : ItemListEvent()
}