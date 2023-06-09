package com.example.beercompose.presentation.fragments.itemlist.components

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.beercompose.domain.model.InCartItem
import com.example.beercompose.domain.model.User
import com.example.beercompose.presentation.common.MenuItem
import com.example.beercompose.presentation.util.navigation.Screen
import com.example.beercompose.util.ScreenState
import com.example.beercompose.presentation.fragments.itemlist.MenuItemListViewModel
import com.example.beercompose.presentation.fragments.itemlist.util.ItemListEvent
import com.example.beercompose.util.LocalAppGradient
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemListScreen(
    navController: NavController,
    viewModel: MenuItemListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(ItemListEvent.GetItemList(state.category))
    }

    var chosen by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val onDeleteClick: (String) -> Unit = {
        viewModel.onEvent(ItemListEvent.DeleteItem(it, state.category))
    }
    val onChangeClick: (String) -> Unit = {
        navController.navigate(Screen.AddUpdateScreen.passId(it, state.category))
    }

    var isToastShowing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isToastShowing) {
        if (isToastShowing) {
            delay(2500)
            isToastShowing = false
        }
    }

    val toastResponse = viewModel.response.collectAsState(initial = "")
    LaunchedEffect(key1 = toastResponse.value) {
        if (toastResponse.value.isNotEmpty()) {
            Toast.makeText(context, toastResponse.value, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        floatingActionButton = {
            if (state.user.role == User.Role.Admin) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(
                            Screen.AddUpdateScreen.passId(
                                null,
                                state.category
                            )
                        )
                    },
                    modifier = Modifier.padding(bottom = 60.dp, end = 10.dp),
                    backgroundColor = MaterialTheme.colors.secondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .background(LocalAppGradient.current.gradient)
                .padding(vertical = 12.dp)
        ) {
            when (state.screenState) {
                is ScreenState.Error -> ErrorScreen {
                    viewModel.onEvent(
                        ItemListEvent.GetItemList(
                            state.category
                        )
                    )
                }
                is ScreenState.Loading -> LoadingScreen()
                is ScreenState.Success -> {
                    SearchRow(
                        searchField = state.searchField,
                        onValueChanged = { viewModel.onEvent(ItemListEvent.UpdateSearchField(it)) }
                    )
                    LazyHorizontalStaggeredGrid(
                        rows = StaggeredGridCells.Fixed(2),
                        modifier = Modifier
                            .height(100.dp)
                            .padding(horizontal = 4.dp)
                            .padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalItemSpacing = 8.dp
                    ) {
                        val types = state.menuItems.map { it.type }.toSet()
                        items(items = types.toList()) {
                            TypeItem(title = it, isChosen = it == chosen, onClick = { type ->
                                chosen = type
                            }, onChosenClick = {
                                chosen = ""
                            })
                        }
                    }
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 40.dp)
                            .padding(horizontal = 8.dp),
                        columns = GridCells.Fixed(2)
                    ) {
                        items(
                            if (chosen == "") state.menuItems else state.menuItems.filter { it.type == chosen },
                            key = { it.UID }) { item ->
                            MenuItem(
                                item = item,
                                isItemLiked = state.likedItems.contains(item.UID),
                                onChangeClick = {
                                    onChangeClick(it)
                                },
                                onDeleteClick = {
                                    onDeleteClick(it)
                                },
                                onItemClick = {
                                    navController.navigate(Screen.ItemInfoScreen.passId(it))
                                },
                                showAdminIcons = state.user.role == User.Role.Admin,
                                onFavClick = { viewModel.onEvent(ItemListEvent.OnFavClick(it)) },
                                inCartItem = state.inCartItems.find { inCartItem -> inCartItem.UID == item.UID }
                                    ?: InCartItem(),
                                onAddToCartClick = {
                                    viewModel.onEvent(
                                        ItemListEvent.AddItemToCart(
                                            it
                                        )
                                    )
                                },
                                onMinusClick = {
                                    viewModel.onEvent(
                                        ItemListEvent.OnMinusClick(
                                            it
                                        )
                                    )
                                },
                                onPlusClick = {
                                    viewModel.onEvent(
                                        ItemListEvent.OnPlusClick(
                                            it
                                        )
                                    )
                                },
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
    }
}