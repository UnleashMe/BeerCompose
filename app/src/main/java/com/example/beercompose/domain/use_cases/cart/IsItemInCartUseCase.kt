package com.example.beercompose.domain.use_cases.cart

import com.example.beercompose.data.database.entities.UserMenuItemCart
import com.example.beercompose.domain.repositories.CartRepository

class IsItemInCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(id: String, number: String): UserMenuItemCart? {
        return cartRepository.getItemInCart(number, id)
    }
}