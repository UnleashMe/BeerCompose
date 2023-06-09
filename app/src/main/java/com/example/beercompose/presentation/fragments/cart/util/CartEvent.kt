package com.example.beercompose.presentation.fragments.cart.util

import com.example.beercompose.domain.model.InCartItem

sealed class CartEvent {
    data class AddItem(val inCartItem: InCartItem) : CartEvent()
    data class RemoveItem(val inCartItem: InCartItem) : CartEvent()
}
