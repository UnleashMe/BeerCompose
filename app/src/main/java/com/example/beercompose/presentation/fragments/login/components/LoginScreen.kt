package com.example.beercompose.presentation.fragments.login.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.beercompose.presentation.common.CustomCard
import com.example.beercompose.presentation.common.TextFieldWithError
import com.example.beercompose.presentation.util.navigation.Screen
import com.example.beercompose.presentation.fragments.login.LoginViewModel
import com.example.beercompose.presentation.fragments.login.util.LoginEvent
import com.example.beercompose.presentation.util.navigation.BottomBarScreen
import com.example.beercompose.R
import com.example.beercompose.presentation.common.CardContent
import com.example.beercompose.util.LocalAppGradient

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {

    val state = viewModel.state.value

    val event = viewModel.loginEvent.collectAsState(initial = "")
    val context = LocalContext.current
    val x = rememberScrollState()

    LaunchedEffect(key1 = event.value) {
        if (event.value.isNotEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.welcome_toast, event.value),
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(BottomBarScreen.MenuCategoriesListScreen.route)
        }
    }

    Column(
        modifier = Modifier
            .background(LocalAppGradient.current.gradient)
            .padding(16.dp)
            .padding(bottom = 50.dp)
            .fillMaxSize()
            .verticalScroll(x)
    ) {
        CustomCard(modifier = Modifier.padding(bottom = 16.dp)) {
            CardContent(
                text = stringResource(id = R.string.log_in_to_account),
                buttonText = stringResource(
                    id = R.string.log_in
                ),
                onButtonClick = { viewModel.onEvent(LoginEvent.Login) },
                buttonEnabled = state.isButtonEnabled
            ) {
                TextFieldWithError(
                    value = state.inputFields.number,
                    onValueChange = { viewModel.onEvent(LoginEvent.OnNumberInput(it)) },
                    label = stringResource(id = R.string.phone_number_hint),
                    error = state.errors.numberError,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextFieldWithError(
                    value = state.inputFields.password.joinToString(""),
                    onValueChange = { viewModel.onEvent(LoginEvent.OnPasswordInput(it)) },
                    label = stringResource(id = R.string.password_hint),
                    error = state.errors.passwordError,
                    keyboardType = KeyboardType.Password,
                    isHidden = true
                )
            }
        }
        CustomCard {
            CardContent(text = stringResource(id = R.string.first_time),
                buttonText = stringResource(
                    id = R.string.registration
                ),
                onButtonClick = { navController.navigate(Screen.RegistrationScreen.route) })
        }
    }
}