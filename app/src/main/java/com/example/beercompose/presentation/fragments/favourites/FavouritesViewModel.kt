package com.example.beercompose.presentation.fragments.favourites

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.domain.use_cases.cart.CartUseCases
import com.example.beercompose.domain.use_cases.likes.LikesUseCases
import com.example.beercompose.domain.use_cases.menu_items.MenuItemsUseCases
import com.example.beercompose.domain.use_cases.users.UserProfileUseCases
import com.example.beercompose.presentation.fragments.favourites.util.FavouritesEvent
import com.example.beercompose.presentation.fragments.favourites.util.FavouritesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val menuItemsUseCases: MenuItemsUseCases,
    private val likesUseCases: LikesUseCases,
    private val cartUseCases: CartUseCases,
    private val userProfileUseCases: UserProfileUseCases
) : ViewModel() {

    private val _state = mutableStateOf(FavouritesState())
    val state: State<FavouritesState> = _state

    fun onEvent(event: FavouritesEvent) {
        when (event) {
            is FavouritesEvent.GetLikedItems -> getLikedItems()
            is FavouritesEvent.OnFavIconClick -> onFavIconClick(event.itemId)
            is FavouritesEvent.OnPlusClick -> plusCartItem(event.inCartItem)
            is FavouritesEvent.OnMinusClick -> minusCartItem(event.inCartItem)
            is FavouritesEvent.AddItemToCart -> addItemToCart(event.id)
        }
    }

    private fun getLikedItems() = viewModelScope.launch {
        val likes = likesUseCases.getLikesUseCase()
        _state.value = state.value.copy(
            user = userProfileUseCases.getUserUseCase()
        )
        val items = menuItemsUseCases.getAllItemsUseCases().first().filter {
            it.UID in likes
        }.sortedBy { it.category.name }
        val inCartItems = mutableListOf<InCartItem>()
        items.forEach { domainItem ->
            inCartItems.add(
                domainItem.toInCartItem(
                    quantity = cartUseCases.isItemInCartUseCase(
                        domainItem.UID,
                        state.value.user.phoneNumber
                    )?.quantity ?: 0
                )
            )
        }
        _state.value = state.value.copy(
            likedItems = items,
            inCartItems = inCartItems
        )
    }

    private fun onFavIconClick(domainItemId: String) = viewModelScope.launch {
        likesUseCases.likeOrDislikeUseCase(domainItemId, listOf(domainItemId))
        _state.value =
            state.value.copy(likedItems = state.value.likedItems.filterNot { it.UID == domainItemId })
    }

    private fun plusCartItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.plusItemInCart(
            state.value.user.phoneNumber,
            inCartItem.UID,
            inCartItem.quantity
        )
        _state.value = state.value.copy(
            inCartItems = state.value.inCartItems - inCartItem + inCartItem.copy(
                quantity = inCartItem.quantity + 1
            )
        )
    }

    private fun minusCartItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.minusItemInCart(state.value.user.phoneNumber, inCartItem)
        _state.value = state.value.copy(
            inCartItems = state.value.inCartItems - inCartItem + inCartItem.copy(
                quantity = inCartItem.quantity - 1
            )
        )
    }

    private fun addItemToCart(id: String) = viewModelScope.launch {
        cartUseCases.addItemToCartUseCase(state.value.user.phoneNumber, id)
        val inCartItem = state.value.inCartItems.find { it.UID == id }!!
        _state.value = state.value.copy(
            inCartItems = state.value.inCartItems - inCartItem + inCartItem.copy(quantity = inCartItem.quantity + 1)
        )
    }
}