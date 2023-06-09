package com.example.beercompose.presentation.fragments.itemlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.domain.use_cases.cart.CartUseCases
import com.example.beercompose.domain.use_cases.likes.LikesUseCases
import com.example.beercompose.domain.use_cases.menu_items.MenuItemsUseCases
import com.example.beercompose.util.ScreenState
import com.example.beercompose.domain.use_cases.users.UserProfileUseCases
import com.example.beercompose.presentation.fragments.itemlist.util.ItemListEvent
import com.example.beercompose.presentation.fragments.itemlist.util.MenuItemListState
import com.example.beercompose.util.Constants.MENU_LIST_CATEGORY_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemListViewModel @Inject constructor(
    private val likesUseCases: LikesUseCases,
    private val menuItemsUseCases: MenuItemsUseCases,
    private val userProfileUseCases: UserProfileUseCases,
    private val cartUseCases: CartUseCases,
    stateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(MenuItemListState())
    val state: State<MenuItemListState> = _state

    private val _response = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val response: SharedFlow<String> = _response

    init {
        val category =
            MenuCategory.toMenuCategory(stateHandle.get<String>(MENU_LIST_CATEGORY_ARG) ?: "")
        _state.value = state.value.copy(
            category = category
        )
    }

    fun onEvent(event: ItemListEvent) {
        when (event) {
            is ItemListEvent.GetItemList -> getItemList(event.category)
            is ItemListEvent.DeleteItem -> deleteItem(event.id, event.category)
            is ItemListEvent.GetLikes -> getLikes()
            is ItemListEvent.OnFavClick -> onFavClick(event.id)
            is ItemListEvent.AddItemToCart -> addItemToCart(event.id)
            is ItemListEvent.OnPlusClick -> plusCartItem(event.inCartItem)
            is ItemListEvent.OnMinusClick -> minusCartItem(event.inCartItem)
            is ItemListEvent.UpdateSearchField -> updateSearchField(event.text)
        }
    }

    private fun getItemList(category: MenuCategory) = viewModelScope.launch {
        try {
            val user = userProfileUseCases.getUserUseCase()
            _state.value = state.value.copy(
                screenState = ScreenState.Loading
            )
            val items = menuItemsUseCases.getAllItemsUseCases(category).first()
            val inCartItems = mutableListOf<InCartItem>()
            items.forEach { domainItem ->
                inCartItems.add(
                    domainItem.toInCartItem(
                        quantity = cartUseCases.isItemInCartUseCase(
                            domainItem.UID,
                            user.phoneNumber
                        )?.quantity ?: 0
                    )
                )
            }
            _state.value = state.value.copy(
                menuItems = items,
                inCartItems = inCartItems,
                backingList = items,
                screenState = ScreenState.Success,
                user = user
            )

        } catch (e: Exception) {
            _state.value = state.value.copy(screenState = ScreenState.Error)
        }
        getLikes()
    }

    private fun deleteItem(itemId: String, category: MenuCategory) =
        viewModelScope.launch(Dispatchers.IO) {
            val response = menuItemsUseCases.deleteItemUseCase(category, itemId)
            _response.emit(response)
            val item = state.value.menuItems.find { it.UID == itemId } ?: return@launch
            if (response != "Проверьте подключение к интернету!") {
                _state.value = state.value.copy(menuItems = state.value.menuItems - item)
            }
        }

    private fun getLikes() = viewModelScope.launch {
        _state.value = state.value.copy(
            likedItems = likesUseCases.getLikesUseCase()
        )
    }

    private fun onFavClick(domainItemId: String) = viewModelScope.launch {
        _state.value = state.value.copy(
            likedItems = likesUseCases.likeOrDislikeUseCase(
                domainItemId,
                state.value.likedItems
            )
        )
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
            inCartItems = state.value.inCartItems - inCartItem + inCartItem.copy(
                quantity = inCartItem.quantity + 1
            )
        )
    }

    private fun updateSearchField(str: String) {
        _state.value = state.value.copy(
            searchField = str
        )
        if (str.isEmpty()) {
            _state.value = state.value.copy(
                menuItems = state.value.backingList
            )
        } else {
            _state.value = state.value.copy(
                menuItems = state.value.backingList.filter {
                    it.name.lowercase().contains(str.lowercase())
                }
            )
        }
    }
}