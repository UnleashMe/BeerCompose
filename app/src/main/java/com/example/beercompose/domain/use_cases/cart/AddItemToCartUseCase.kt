package com.example.beercompose.domain.use_cases.cart

import com.example.beercompose.data.database.entities.UserMenuItemCart
import com.example.beercompose.domain.repositories.CartRepository

class AddItemToCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(number: String, id: String) {
        cartRepository.addItemInCart(UserMenuItemCart(number, id))
    }
}