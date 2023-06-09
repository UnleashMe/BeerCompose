package com.example.beercompose.presentation.fragments.singleItem.util

import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.domain.model.DomainItem
import com.example.beercompose.domain.model.User

data class SingleItemState(
    val mainItem: DomainItem = DomainItem(),
    val likedItems: List<String> = emptyList(),
    val itemsSet: Set<DomainItem> = emptySet(),
    val isMainItemLiked: Boolean = false,
    val isMainItemInCart: Boolean = false,
    val inCartItem: InCartItem = InCartItem(),
    val user: User = User(),
    val inCartItemsSet: List<InCartItem> = emptyList(),
    val likes: Int = 0
)