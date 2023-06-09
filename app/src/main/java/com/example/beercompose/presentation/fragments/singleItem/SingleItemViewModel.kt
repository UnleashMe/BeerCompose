package com.example.beercompose.presentation.fragments.singleItem

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.domain.use_cases.cart.CartUseCases
import com.example.beercompose.domain.use_cases.likes.LikesUseCases
import com.example.beercompose.domain.use_cases.menu_items.MenuItemsUseCases
import com.example.beercompose.domain.use_cases.users.UserProfileUseCases
import com.example.beercompose.presentation.fragments.singleItem.util.SingleItemEvent
import com.example.beercompose.presentation.fragments.singleItem.util.SingleItemState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SingleItemViewModel @Inject constructor(
    private val likesUseCases: LikesUseCases,
    private val menuItemsUseCases: MenuItemsUseCases,
    private val userProfileUseCases: UserProfileUseCases,
    private val cartUseCases: CartUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var id: String

    private val _state = mutableStateOf(SingleItemState())
    val state: State<SingleItemState> = _state

    private val _likes = MutableStateFlow(0)
    val likes: StateFlow<Int> = _likes

    init {
        id = savedStateHandle.get<String>("id") ?: ""
    }

    fun onEvent(event: SingleItemEvent) {
        when (event) {
            is SingleItemEvent.GetMenuItem -> getMenuItemById(event.id)
            is SingleItemEvent.OnFavMainItem -> onFavMainItem()
            is SingleItemEvent.OnFavItemInSet -> onFavItemInSet(event.id)
            is SingleItemEvent.OnMinusSetItem -> minusCartItem(event.inCartItem)
            is SingleItemEvent.OnPlusSetItem -> plusCartItem(event.inCartItem)
            is SingleItemEvent.AddMainItemToCart -> addToCart()
            is SingleItemEvent.AddSetItemToCart -> addItemToCart(event.id)
            is SingleItemEvent.OnPlusMainItem -> plusItemInCart()
            is SingleItemEvent.OnMinusMainItem -> minusItemInCart()
        }
    }

    private fun getMenuItemById(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        val item = menuItemsUseCases.getMenuItemByIdUseCase(itemId)
        val user = userProfileUseCases.getUserUseCase()
        withContext(Dispatchers.Main) {
            _state.value = state.value.copy(
                mainItem = item,
                inCartItem = item.toInCartItem(),
                user = user
            )
            launch {
                getItemsSet()
            }
            isItemLiked()
            isItemInCart()
            getLikes(itemId)
        }
    }

    private fun onFavMainItem() = viewModelScope.launch {
        val list = likesUseCases.likeOrDislikeUseCase(
            state.value.mainItem.UID,
            listOf(state.value.mainItem.UID)
        )
        if (list.isEmpty()) {
            _state.value = state.value.copy(isMainItemLiked = false)
        } else {
            _state.value = state.value.copy(isMainItemLiked = true)
        }
    }

    private fun onFavItemInSet(domainItemId: String) = viewModelScope.launch {
        _state.value = state.value.copy(
            likedItems = likesUseCases.likeOrDislikeUseCase(
                domainItemId,
                state.value.likedItems
            )
        )
    }

    private fun addToCart() = viewModelScope.launch {
        _state.value = state.value.copy(
            isMainItemInCart = !state.value.isMainItemInCart,
            inCartItem = state.value.inCartItem.copy(quantity = state.value.inCartItem.quantity + 1)
        )
        val currentUser = state.value.user
        val id = state.value.mainItem.UID
        cartUseCases.addItemToCartUseCase(currentUser.phoneNumber, id)
    }

    private fun plusItemInCart() = viewModelScope.launch {
        val currentUser = state.value.user
        cartUseCases.plusItemInCart(
            currentUser.phoneNumber,
            state.value.inCartItem.UID,
            state.value.inCartItem.quantity
        )
        _state.value =
            state.value.copy(inCartItem = state.value.inCartItem.copy(quantity = state.value.inCartItem.quantity + 1))
    }

    private fun minusItemInCart() = viewModelScope.launch {
        val currentUser = state.value.user
        cartUseCases.minusItemInCart(currentUser.phoneNumber, state.value.inCartItem)
        _state.value =
            state.value.copy(inCartItem = state.value.inCartItem.copy(quantity = state.value.inCartItem.quantity - 1))
    }

    private fun plusCartItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.plusItemInCart(
            state.value.user.phoneNumber,
            inCartItem.UID,
            inCartItem.quantity
        )
        _state.value = state.value.copy(
            inCartItemsSet = state.value.inCartItemsSet - inCartItem + inCartItem.copy(
                quantity = inCartItem.quantity + 1
            )
        )
    }

    private fun minusCartItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.minusItemInCart(state.value.user.phoneNumber, inCartItem)
        _state.value = state.value.copy(
            inCartItemsSet = state.value.inCartItemsSet - inCartItem + inCartItem.copy(
                quantity = inCartItem.quantity - 1
            )
        )
    }

    private fun addItemToCart(id: String) = viewModelScope.launch {
        cartUseCases.addItemToCartUseCase(state.value.user.phoneNumber, id)
        val inCartItem = state.value.inCartItemsSet.find { it.UID == id }!!
        _state.value = state.value.copy(
            inCartItemsSet = state.value.inCartItemsSet - inCartItem + inCartItem.copy(
                quantity = inCartItem.quantity + 1
            )
        )
    }

    private fun getLikes(itemId: String) = viewModelScope.launch {
        likesUseCases.getItemLikesByIdUseCase(itemId).collect {
            _state.value = state.value.copy(
                likes = it
            )
        }
    }

    private fun getItemsSet() = viewModelScope.launch {
        val allItems = menuItemsUseCases.getAllItemsUseCases()
        allItems.collect {
            val set = menuItemsUseCases.getItemsSetUseCase(it, state.value.mainItem.category)
            val inCartItems = mutableListOf<InCartItem>()
            set.forEach { domainItem ->
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
                itemsSet = set,
                inCartItemsSet = inCartItems
            )
            val list = mutableListOf<String>()
            set.forEach { item ->
                val isLiked = likesUseCases.isItemLikedUseCase(item.UID)
                if (isLiked != null) {
                    list.add(item.UID)
                }
            }
            _state.value = state.value.copy(likedItems = list)
        }
    }

    private fun isItemLiked() = viewModelScope.launch {
        val like = likesUseCases.isItemLikedUseCase(state.value.mainItem.UID)
        like?.let {
            _state.value = state.value.copy(isMainItemLiked = true)
        } ?: kotlin.run {
            _state.value = state.value.copy(isMainItemLiked = false)
        }
    }

    private fun isItemInCart() = viewModelScope.launch {
        val currentUserNumber = state.value.user.phoneNumber
        val isInCart = cartUseCases.isItemInCartUseCase(state.value.mainItem.UID, currentUserNumber)
        isInCart?.let {
            _state.value = state.value.copy(
                isMainItemInCart = true,
                inCartItem = state.value.inCartItem.copy(quantity = it.quantity)
            )
        } ?: kotlin.run {
            _state.value = state.value.copy(
                isMainItemInCart = false,
                inCartItem = state.value.inCartItem.copy(quantity = 0)
            )
        }
    }
}