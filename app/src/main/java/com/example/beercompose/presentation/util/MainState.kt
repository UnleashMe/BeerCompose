package com.example.beercompose.presentation.util

import com.example.beercompose.domain.model.User

data class MainState(
    val user: User = User(),
    val itemsInCart: Int = 0,
    val price: Double = 0.0
)