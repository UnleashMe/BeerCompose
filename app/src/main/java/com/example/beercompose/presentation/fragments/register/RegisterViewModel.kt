package com.example.beercompose.presentation.fragments.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beercompose.domain.use_cases.user_validation.UserValidationUseCases
import com.example.beercompose.domain.use_cases.users.UserProfileUseCases
import com.example.beercompose.presentation.fragments.register.util.RegisterEvent
import com.example.beercompose.presentation.fragments.register.util.RegisterState
import com.example.beercompose.util.InvalidNameException
import com.example.beercompose.util.InvalidNumberException
import com.example.beercompose.util.NumberAlreadyExistsException
import com.example.beercompose.util.PasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userProfileUseCases: UserProfileUseCases,
    private val userValidationUseCases: UserValidationUseCases
) : ViewModel() {

    private val _state = mutableStateOf(RegisterState())
    val state: State<RegisterState> = _state

    private val _goBackEvent = MutableSharedFlow<Boolean>()
    val goBackEvent: SharedFlow<Boolean> = _goBackEvent

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnNumberInput -> onNumberInput(event.text)
            is RegisterEvent.OnNameInput -> onNameInput(event.text)
            is RegisterEvent.OnPasswordInput -> onPasswordInput(event.text)
            is RegisterEvent.OnRepeatPasswordInput -> onRepeatPasswordInput(event.text)
            is RegisterEvent.Register -> register()
        }
    }

    private fun onNumberInput(text: String) {
        try {
            userValidationUseCases.phoneNumberValidationUseCase(text)
            _state.value = state.value.copy(
                errors = state.value.errors.copy(
                    numberError = ""
                ),
                inputFields = state.value.inputFields.copy(
                    number = text
                )
            )
        } catch (e: InvalidNumberException) {
            _state.value = state.value.copy(
                errors = state.value.errors.copy(numberError = e.message ?: ""),
                inputFields = state.value.inputFields.copy(number = text)
            )
        }
        enableRegisterButton()
    }

    private fun onNameInput(text: String) {
        try {
            userValidationUseCases.nameValidationUseCase(text)
            _state.value = state.value.copy(
                inputFields = state.value.inputFields.copy(name = text),
                errors = state.value.errors.copy(nameError = "")
            )
        } catch (e: InvalidNameException) {
            _state.value = state.value.copy(
                errors = state.value.errors.copy(nameError = e.message ?: ""),
                inputFields = state.value.inputFields.copy(name = text)
            )
        }
        enableRegisterButton()
    }

    private fun onPasswordInput(text: String) {
        try {
            userValidationUseCases.passwordValidationUseCase(text)
            _state.value = state.value.copy(
                errors = state.value.errors.copy(
                    passwordError = ""
                ),
                inputFields = state.value.inputFields.copy(
                    password = text.toCharArray()
                )
            )
        } catch (e: PasswordException.NewPasswordException) {
            _state.value =
                state.value.copy(
                    errors = state.value.errors.copy(
                        passwordError = e.message ?: "",
                        passwordRepeatError = ""
                    ),
                    inputFields = state.value.inputFields.copy(password = text.toCharArray())
                )
        }
        enableRegisterButton()
    }

    private fun onRepeatPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(
                repeatPassword = text.toCharArray()
            ),
            errors = state.value.errors.copy(
                passwordRepeatError = ""
            )
        )
        enableRegisterButton()
    }

    private fun register() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userProfileUseCases.registerUserUseCase(
                    registerInputFields = state.value.inputFields
                )
                _goBackEvent.emit(true)
            } catch (e: PasswordException.RepeatPasswordException) {
                _state.value = state.value.copy(
                    errors = state.value.errors.copy(
                        passwordRepeatError = e.message ?: ""
                    )
                )
            } catch (e: NumberAlreadyExistsException) {
                _state.value =
                    state.value.copy(
                        errors = state.value.errors.copy(
                            numberError = e.message ?: ""
                        )
                    )
            }
        }
    }

    private fun enableRegisterButton() {
        _state.value = state.value.copy(
            isButtonEnabled = state.value.inputFields.number.isNotEmpty() &&
                    state.value.inputFields.name.isNotEmpty() &&
                    state.value.inputFields.password.isNotEmpty() &&
                    state.value.inputFields.repeatPassword.isNotEmpty() &&
                    state.value.errors.numberError.isEmpty() &&
                    state.value.errors.nameError.isEmpty() &&
                    state.value.errors.passwordError.isEmpty() &&
                    state.value.errors.passwordRepeatError.isEmpty()
        )
    }
}