package com.example.beercompose.presentation.fragments.profile.components

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.beercompose.R
import com.example.beercompose.presentation.common.CustomCard
import com.example.beercompose.presentation.common.CardContent
import com.example.beercompose.presentation.common.TextFieldWithError
import com.example.beercompose.presentation.fragments.profile.util.VMStringResource
import com.example.beercompose.presentation.util.navigation.BottomBarScreen
import com.example.beercompose.presentation.fragments.profile.ProfileViewModel
import com.example.beercompose.presentation.fragments.profile.util.ProfileEvent

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {

    val state = viewModel.state.value
    val x = rememberScrollState()
    val context = LocalContext.current
    val toastEvent = viewModel.toastMessage.collectAsState(initial = VMStringResource.Blanc)

    LaunchedEffect(key1 = toastEvent.value) {
        when (toastEvent.value) {
            is VMStringResource.Blanc -> {}
            is VMStringResource.AccountDeleted -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.account_deleted_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }
            is VMStringResource.NameChanged -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.name_changed_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }
            is VMStringResource.PasswordChanged -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.password_changed_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .padding(16.dp)
            .padding(bottom = 50.dp)
            .verticalScroll(x)
    ) {
        CustomCard(modifier = Modifier.padding(bottom = 12.dp)) {
            CardContent(
                text = stringResource(id = R.string.log_out),
                buttonText = stringResource(R.string.logout),
                onButtonClick = {
                    viewModel.onEvent(ProfileEvent.Logout)
                    navController.navigate(BottomBarScreen.MenuCategoriesListScreen.route)
                })
        }
        CustomCard(modifier = Modifier.padding(bottom = 12.dp)) {
            CardContent(
                text = stringResource(id = R.string.change_name), buttonText = stringResource(
                    id = R.string.save
                ), buttonEnabled = state.isButtonsEnabled.isChangeNameEnabled,
                onButtonClick = { viewModel.onEvent(ProfileEvent.ChangeName) }
            ) {
                TextFieldWithError(
                    value = state.inputFields.changeNameField,
                    onValueChange = { viewModel.onEvent(ProfileEvent.OnChangeNameInput(it)) },
                    label = stringResource(id = R.string.name_hint),
                    error = state.fieldErrors.changeNameError
                )
            }
        }
        CustomCard(modifier = Modifier.padding(bottom = 12.dp)) {
            CardContent(
                text = stringResource(id = R.string.change_password),
                buttonText = stringResource(
                    id = R.string.save
                ),
                onButtonClick = { viewModel.onEvent(ProfileEvent.ChangeName) },
                buttonEnabled = state.isButtonsEnabled.isChangePasswordEnabled
            ) {
                TextFieldWithError(
                    value = state.inputFields.oldPasswordField.joinToString(""),
                    onValueChange = { viewModel.onEvent(ProfileEvent.OnOldPasswordInput(it)) },
                    label = stringResource(id = R.string.old_password),
                    error = state.fieldErrors.oldPasswordError,
                    isHidden = true
                )
                TextFieldWithError(
                    value = state.inputFields.newPasswordField.joinToString(""),
                    onValueChange = { viewModel.onEvent(ProfileEvent.OnNewPasswordInput(it)) },
                    label = stringResource(id = R.string.new_password),
                    error = state.fieldErrors.newPasswordError,
                    isHidden = true
                )
                TextFieldWithError(
                    value = state.inputFields.repeatNewPasswordField.joinToString(""),
                    onValueChange = { viewModel.onEvent(ProfileEvent.OnRepeatNewPasswordInput(it)) },
                    label = stringResource(id = R.string.repeat_new_password),
                    error = state.fieldErrors.repeatNewPasswordError,
                    isHidden = true
                )
            }
        }
        CustomCard {
            CardContent(
                text = stringResource(id = R.string.delete_account),
                buttonText = stringResource(
                    id = R.string.delete
                ),
                textModifier = Modifier.padding(bottom = 8.dp),
                onButtonClick = { viewModel.onEvent(ProfileEvent.DeleteUser) },
                buttonEnabled = state.isButtonsEnabled.isDeleteUserEnabled
            ) {
                TextFieldWithError(
                    value = state.inputFields.deleteUserPasswordField.joinToString(""),
                    onValueChange = { viewModel.onEvent(ProfileEvent.OnDeletePasswordInput(it)) },
                    label = stringResource(id = R.string.password_hint),
                    error = state.fieldErrors.deleteUserPasswordError
                )
            }
        }
    }
}