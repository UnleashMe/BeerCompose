package com.example.beercompose.presentation.fragments.addbeer.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.presentation.common.CustomCard
import com.example.beercompose.presentation.common.TextFieldWithError
import com.example.beercompose.presentation.fragments.addbeer.AddBeerViewModel
import com.example.beercompose.presentation.fragments.addbeer.util.AddUpdateEvent
import com.example.beercompose.presentation.fragments.addbeer.util.AddUpdateMode
import com.example.beercompose.presentation.util.navigation.Screen
import com.example.beercompose.R
import com.example.beercompose.presentation.common.CustomButton
import com.example.beercompose.util.LocalAppGradient

@Composable
fun AddUpdateScreen(
    viewModel: AddBeerViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val state = viewModel.state.value
    val response = viewModel.response.collectAsState(initial = "")
    val context = LocalContext.current

    LaunchedEffect(key1 = response.value) {
        if (response.value.isNotEmpty()) {
            Toast.makeText(context, response.value, Toast.LENGTH_SHORT).show()
            if (response.value.contains("Проверьте")) {
                return@LaunchedEffect
            }
            navController.navigate(Screen.MenuItemsListScreen.passCategory(state.mainItem.category))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppGradient.current.gradient)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navController::popBackStack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
            }
            Text(
                text = stringResource(state.screenTitle, state.mainItem.name),
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onSurface
            )
        }
        CustomCard {
            TextFieldWithError(
                value = state.validationModel.title,
                onValueChange = { viewModel.onEvent(AddUpdateEvent.TitleTextChanged(it)) },
                label = stringResource(id = R.string.title),
                error = state.addUpdateErrorFields.titleError
            )
            TextFieldWithError(
                value = state.validationModel.description,
                onValueChange = { viewModel.onEvent(AddUpdateEvent.DescriptionTextChanged(it)) },
                label = stringResource(id = R.string.description),
                error = state.addUpdateErrorFields.descriptionError
            )
            TextFieldWithError(
                value = state.validationModel.type,
                onValueChange = { viewModel.onEvent(AddUpdateEvent.TypeTextChanged(it)) },
                label = stringResource(id = R.string.type),
                error = state.addUpdateErrorFields.typeError
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                TextFieldWithError(
                    value = state.validationModel.price,
                    onValueChange = { viewModel.onEvent(AddUpdateEvent.PriceTextChanged(it)) },
                    label = stringResource(id = R.string.price_hint),
                    error = state.addUpdateErrorFields.priceError,
                    modifier = Modifier.weight(1f)
                )
                if (state.mainItem.category is MenuCategory.BeerCategory) {
                    TextFieldWithError(
                        value = state.validationModel.alc,
                        onValueChange = {
                            viewModel.onEvent(
                                AddUpdateEvent.AlcPercentageTextChanged(
                                    it
                                )
                            )
                        },
                        label = stringResource(id = R.string.alc_hint),
                        error = state.addUpdateErrorFields.alcPercentageError,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    TextFieldWithError(
                        value = state.validationModel.weight,
                        onValueChange = { viewModel.onEvent(AddUpdateEvent.WeightTextChanged(it)) },
                        label = stringResource(id = R.string.weight_hint),
                        error = state.addUpdateErrorFields.weightError,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.mainItem.category is MenuCategory.BeerCategory) {
                    TextFieldWithError(
                        value = state.validationModel.salePercentage,
                        onValueChange = {
                            viewModel.onEvent(
                                AddUpdateEvent.SalePercentageTextChanged(
                                    it
                                )
                            )
                        },
                        label = stringResource(id = R.string.discount_hint),
                        error = state.addUpdateErrorFields.salePercentageError,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 24.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                CustomButton(
                    enabled = state.isButtonEnabled,
                    text = if (state.mode == AddUpdateMode.ADD) stringResource(id = R.string.create) else stringResource(
                        id = R.string.save
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .width(100.dp)
                ) {
                    viewModel.onEvent(AddUpdateEvent.ButtonClick)
                }
            }
        }
    }
}