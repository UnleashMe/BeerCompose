package com.example.beercompose.presentation.fragments.singleItem.util

import com.example.beercompose.domain.model.InCartItem

sealed class SingleItemEvent {
    data class GetMenuItem(val id: String) : SingleItemEvent()
    object OnFavMainItem : SingleItemEvent()
    data class OnFavItemInSet(val id: String) : SingleItemEvent()
    object AddMainItemToCart : SingleItemEvent()
    object OnPlusMainItem : SingleItemEvent()
    object OnMinusMainItem : SingleItemEvent()
    data class AddSetItemToCart(val id: String) : SingleItemEvent()
    data class OnPlusSetItem(val inCartItem: InCartItem) : SingleItemEvent()
    data class OnMinusSetItem(val inCartItem: InCartItem) : SingleItemEvent()
}
