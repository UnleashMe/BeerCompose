package com.example.beercompose.domain.use_cases.cart

import com.example.beercompose.domain.repositories.CartRepository

class ClearUserCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(number: String) {
        cartRepository.clearUserCart(number)
    }
}