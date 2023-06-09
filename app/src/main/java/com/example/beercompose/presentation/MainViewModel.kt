package com.example.beercompose.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beercompose.data.settings.UserRoleTypeAdapter
import com.example.beercompose.domain.model.User
import com.example.beercompose.domain.use_cases.users.UserProfileUseCases
import com.example.beercompose.presentation.util.MainState
import com.example.beercompose.domain.use_cases.cart.CartUseCases
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val userProfileUseCases: UserProfileUseCases,
    val cartUseCases: CartUseCases
) : ViewModel() {

    private val _state = mutableStateOf(MainState())
    val state: State<MainState> = _state

    init {
        viewModelScope.launch {
            userProfileUseCases.addUserListenerUseCase().onEach {
                val gson =
                    GsonBuilder().registerTypeAdapter(User.Role::class.java, UserRoleTypeAdapter())
                val user =
                    gson.create().fromJson(it, User::class.java) ?: User(role = User.Role.NoUser)
                _state.value = state.value.copy(user = user)
                getUsersCart()
            }.launchIn(viewModelScope)
        }
    }

    private fun getUsersCart() = viewModelScope.launch {
        cartUseCases.getUserCartFlowUseCase(state.value.user.phoneNumber).collect { inCartItems ->
            val list = cartUseCases.getCartListUseCase(inCartItems)
            _state.value = state.value.copy(
                itemsInCart = list.sumOf { it.quantity },
                price = list.sumOf { it.price * it.quantity }
            )
        }
    }

    fun onOrderClick() = viewModelScope.launch {
        cartUseCases.clearUserCart(state.value.user.phoneNumber)
    }
}