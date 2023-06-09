package com.example.beercompose.domain.use_cases.cart

import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.data.database.entities.UserMenuItemCart
import com.example.beercompose.domain.repositories.CartRepository

class MinusItemInCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(number: String, inCartItem: InCartItem) {
        if (inCartItem.quantity > 1) {
            cartRepository.updateItemInCart(
                UserMenuItemCart(
                    number,
                    inCartItem.UID,
                    inCartItem.quantity - 1
                )
            )
        } else {
            cartRepository.removeItemFromCart(number, inCartItem.UID)
        }
    }
}