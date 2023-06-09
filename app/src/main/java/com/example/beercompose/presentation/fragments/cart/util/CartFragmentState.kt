package com.example.beercompose.presentation.fragments.cart.util

import com.example.beercompose.domain.model.InCartItem

data class CartFragmentState(
    val inCartItems: List<InCartItem> = emptyList(),
    val price: Double = 0.0,
    val isButtonEnabled: Boolean = false
)