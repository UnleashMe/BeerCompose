package com.example.beercompose.presentation.fragments.favourites.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.beercompose.R
import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.domain.model.User
import com.example.beercompose.presentation.common.EmptyScreen
import com.example.beercompose.presentation.common.MenuItem
import com.example.beercompose.presentation.util.navigation.Screen
import com.example.beercompose.presentation.fragments.favourites.FavouritesViewModel
import com.example.beercompose.presentation.fragments.favourites.util.FavouritesEvent
import com.example.beercompose.presentation.util.navigation.BottomBarScreen
import com.example.beercompose.util.LocalAppGradient
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavouritesScreen(
    navController: NavHostController,
    viewModel: FavouritesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    viewModel.onEvent(FavouritesEvent.GetLikedItems)

    var isToastShowing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isToastShowing) {
        if (isToastShowing) {
            delay(2500)
            isToastShowing = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppGradient.current.gradient)
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
                text = stringResource(id = R.string.favourites),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
        Spacer(
            modifier = Modifier
                .height(3.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
        )
        if (state.likedItems.isEmpty()) {
            EmptyScreen {
                navController.navigate(BottomBarScreen.MenuCategoriesListScreen.route)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(4.dp)
                    .padding(bottom = 70.dp)
            ) {
                items(items = state.likedItems, key = { it.UID }) { domainItem ->
                    MenuItem(
                        item = domainItem,
                        isItemLiked = state.likedItems.contains(domainItem),
                        onDeleteClick = {},
                        onChangeClick = {},
                        onItemClick = {
                            navController.navigate(Screen.ItemInfoScreen.passId(it))
                        },
                        onFavClick = { id ->
                            viewModel.onEvent(FavouritesEvent.OnFavIconClick(id))
                        },
                        inCartItem = state.inCartItems.find { it.UID == domainItem.UID }
                            ?: InCartItem(),
                        onAddToCartClick = { viewModel.onEvent(FavouritesEvent.AddItemToCart(it)) },
                        onMinusClick = { viewModel.onEvent(FavouritesEvent.OnMinusClick(it)) },
                        onPlusClick = { viewModel.onEvent(FavouritesEvent.OnPlusClick(it)) },
                        isUserLogged = state.user.role != User.Role.NoUser,
                        isToastShowing = isToastShowing,
                        onToastStart = { isToastShowing = true },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }
    }
}