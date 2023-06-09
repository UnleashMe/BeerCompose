package com.example.beercompose.presentation.fragments.register.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.beercompose.presentation.common.TextFieldWithError
import com.example.beercompose.presentation.fragments.register.RegisterViewModel
import com.example.beercompose.presentation.fragments.register.util.RegisterEvent
import com.example.beercompose.presentation.util.navigation.BottomBarScreen
import com.example.beercompose.R
import com.example.beercompose.presentation.common.CustomCard
import com.example.beercompose.presentation.common.CardContent
import com.example.beercompose.util.LocalAppGradient

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {

    val state = viewModel.state.value
    val event = viewModel.goBackEvent.collectAsState(initial = false)
    val context = LocalContext.current

    LaunchedEffect(key1 = event.value) {
        if (event.value) {
            Toast.makeText(
                context,
                context.getString(R.string.acoount_created_toast),
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(BottomBarScreen.LoginScreen.route)
        }
    }

    Column(
        modifier = Modifier
            .background(LocalAppGradient.current.gradient)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        CustomCard(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            CardContent(text = stringResource(id = R.string.create_account),
                buttonText = stringResource(
                    id = R.string.registration
                ),
                buttonEnabled = state.isButtonEnabled,
                onButtonClick = { viewModel.onEvent(RegisterEvent.Register) }) {
                TextFieldWithError(
                    value = state.inputFields.number,
                    onValueChange = { viewModel.onEvent(RegisterEvent.OnNumberInput(it)) },
                    label = stringResource(id = R.string.phone_number_hint),
                    error = state.errors.numberError,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextFieldWithError(
                    value = state.inputFields.name,
                    onValueChange = { viewModel.onEvent(RegisterEvent.OnNameInput(it)) },
                    label = stringResource(id = R.string.name_hint),
                    error = state.errors.nameError,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextFieldWithError(
                    value = state.inputFields.password.joinToString(""),
                    onValueChange = { viewModel.onEvent(RegisterEvent.OnPasswordInput(it)) },
                    label = stringResource(id = R.string.password_hint),
                    error = state.errors.passwordError,
                    isHidden = true,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextFieldWithError(
                    value = state.inputFields.repeatPassword.joinToString(""),
                    onValueChange = { viewModel.onEvent(RegisterEvent.OnRepeatPasswordInput(it)) },
                    label = stringResource(id = R.string.repeat_password),
                    error = state.errors.passwordRepeatError,
                    isHidden = true
                )
            }
        }
        CustomCard {
            CardContent(text = stringResource(id = R.string.already_have_account),
                buttonText = stringResource(
                    id = R.string.log_in
                ),
                onButtonClick = { navController.navigate(BottomBarScreen.LoginScreen.route) })
        }
    }
}