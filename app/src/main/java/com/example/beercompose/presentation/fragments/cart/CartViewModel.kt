package com.example.beercompose.presentation.fragments.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beercompose.presentation.fragments.cart.util.CartFragmentState
import com.example.beercompose.domain.model.User
import com.example.beercompose.domain.use_cases.cart.CartUseCases
import com.example.beercompose.domain.use_cases.users.UserProfileUseCases
import com.example.beercompose.presentation.fragments.cart.util.CartEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val userProfileUseCases: UserProfileUseCases,
    private val cartUseCases: CartUseCases
) : ViewModel() {

    private val _state = mutableStateOf(CartFragmentState())
    val state: State<CartFragmentState> = _state

    private lateinit var user: User

    fun getUsersCart() = viewModelScope.launch {
        user = userProfileUseCases.getUserUseCase()
        cartUseCases.getUserCartFlowUseCase(user.phoneNumber).collect {
            val list = cartUseCases.getCartListUseCase(it)
            _state.value = state.value.copy(
                inCartItems = list,
                price = list.sumOf { inCArtItem -> inCArtItem.price * inCArtItem.quantity })
        }
    }

    fun onEvent(event: CartEvent) = viewModelScope.launch {
        when (event) {
            is CartEvent.AddItem -> {
                cartUseCases.plusItemInCart(
                    user.phoneNumber,
                    event.inCartItem.UID,
                    event.inCartItem.quantity
                )
            }
            is CartEvent.RemoveItem -> {
                cartUseCases.minusItemInCart(user.phoneNumber, event.inCartItem)
            }
        }
    }
}