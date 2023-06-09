package com.example.beercompose.presentation.fragments.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beercompose.domain.use_cases.users.UserProfileUseCases
import com.example.beercompose.presentation.fragments.login.util.LoginEvent
import com.example.beercompose.presentation.fragments.login.util.LoginState
import com.example.beercompose.util.PasswordException
import com.example.beercompose.util.UserDoesntExistException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userProfileUseCases: UserProfileUseCases
) : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    private val _loginEvent = MutableSharedFlow<String>()
    val loginEvent: SharedFlow<String> = _loginEvent

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnNumberInput -> onNumberInput(event.text)
            is LoginEvent.OnPasswordInput -> onPasswordInput(event.text)
            is LoginEvent.Login -> login()
        }
    }

    private fun onNumberInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(
                number = text
            ),
            errors = state.value.errors.copy(
                numberError = ""
            )
        )
        enableLoginButton()
    }

    private fun onPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(
                password = text.toCharArray()
            ),
            errors = state.value.errors.copy(
                passwordError = ""
            )
        )
        enableLoginButton()
    }

    private fun login() = viewModelScope.launch {
        try {
            val user = userProfileUseCases.findUserByNumberUseCase(
                state.value.inputFields.number,
                state.value.inputFields.password
            )
            userProfileUseCases.setCurrentUserUseCase(user)
            _loginEvent.emit(user.name)
        } catch (e: UserDoesntExistException) {
            _state.value = state.value.copy(
                errors = state.value.errors.copy(
                    numberError = e.message ?: ""
                )
            )
        } catch (e: PasswordException) {
            _state.value = state.value.copy(
                errors = state.value.errors.copy(
                    passwordError = e.message ?: ""
                )
            )
        }
    }

    private fun enableLoginButton() {
        _state.value = state.value.copy(
            isButtonEnabled = state.value.inputFields.number.isNotEmpty()
                    && state.value.inputFields.password.isNotEmpty()
        )
    }
}