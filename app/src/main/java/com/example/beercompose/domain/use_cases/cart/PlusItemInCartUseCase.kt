package com.example.beercompose.domain.use_cases.cart

import com.example.beercompose.data.database.entities.UserMenuItemCart
import com.example.beercompose.domain.repositories.CartRepository

class PlusItemInCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(number: String, itemId: String, itemQuantity: Int) {
        cartRepository.updateItemInCart(
            UserMenuItemCart(
                number,
                itemId,
                itemQuantity + 1
            )
        )
    }
}