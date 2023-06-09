package com.example.beercompose.domain.use_cases.cart

import com.example.beercompose.data.database.entities.UserAndItemsInCart
import com.example.beercompose.domain.repositories.CartRepository
import kotlinx.coroutines.flow.Flow

class GetUserCartFlowUseCase(
    private val repository: CartRepository
) {
    operator fun invoke(number: String): Flow<UserAndItemsInCart> {
        return repository.getUserCartByNumber(number)
    }
}