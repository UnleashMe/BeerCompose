package com.example.beercompose.presentation.fragments.favourites.util

import com.example.beercompose.domain.model.InCartItem

sealed class FavouritesEvent {
    object GetLikedItems : FavouritesEvent()
    data class OnFavIconClick(val itemId: String) : FavouritesEvent()
    data class OnPlusClick(val inCartItem: InCartItem) : FavouritesEvent()
    data class OnMinusClick(val inCartItem: InCartItem) : FavouritesEvent()
    data class AddItemToCart(val id: String) : FavouritesEvent()
}