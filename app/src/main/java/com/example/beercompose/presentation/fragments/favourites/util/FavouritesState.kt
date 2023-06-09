package com.example.beercompose.presentation.fragments.favourites.util

import com.example.beercompose.domain.model.DomainItem
import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.domain.model.User

data class FavouritesState(
    val likedItems: List<DomainItem> = emptyList(),
    val inCartItems: List<InCartItem> = emptyList(),
    val user: User = User()
)