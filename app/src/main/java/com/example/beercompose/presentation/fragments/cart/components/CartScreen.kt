package com.example.beercompose.presentation.fragments.cart.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.beercompose.R
import com.example.beercompose.presentation.common.EmptyScreen
import com.example.beercompose.presentation.fragments.cart.CartViewModel
import com.example.beercompose.presentation.fragments.cart.util.CartEvent
import com.example.beercompose.presentation.util.navigation.BottomBarScreen
import com.example.beercompose.presentation.util.navigation.Screen
import com.example.beercompose.util.LocalAppGradient

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.getUsersCart()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppGradient.current.gradient)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.cart),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary
            )
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
        Spacer(
            modifier = Modifier
                .height(3.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
        )
        if (state.price == 0.0) {
            EmptyScreen {
                navController.navigate(BottomBarScreen.MenuCategoriesListScreen.route)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(bottom = 100.dp)) {
                items(state.inCartItems, key = { it.UID }) {
                    InCartItem(
                        inCartItem = it,
                        onMinusClick = { inCartItem ->
                            viewModel.onEvent(
                                CartEvent.RemoveItem(
                                    inCartItem
                                )
                            )
                        },
                        onPlusClick = { inCartItem -> viewModel.onEvent(CartEvent.AddItem(inCartItem)) },
                        onItemClick = { id -> navController.navigate(Screen.ItemInfoScreen.passId(id)) },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }
    }
}