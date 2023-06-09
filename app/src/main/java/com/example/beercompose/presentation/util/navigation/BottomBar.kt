package com.example.beercompose.presentation.util.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.beercompose.domain.model.User
import com.example.beercompose.presentation.MainViewModel
import com.example.beercompose.ui.theme.AmberLight

@Composable
fun BottomBar(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onItemClick: (BottomBarScreen) -> Unit,
    cartItemsCount: Int
) {
    val screens = if (mainViewModel.state.value.user.role is User.Role.NoUser) listOf(
        BottomBarScreen.MenuCategoriesListScreen,
        BottomBarScreen.FavouritesScreen,
        BottomBarScreen.CartScreen,
        BottomBarScreen.LoginScreen
    ) else listOf(
        BottomBarScreen.MenuCategoriesListScreen,
        BottomBarScreen.FavouritesScreen,
        BottomBarScreen.CartScreen,
        BottomBarScreen.ProfileScreen
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
        )
        BottomNavigation {
            screens.forEach {
                BottomNavigationItem(
                    selected = it.route == currentDestination?.route,
                    onClick = { onItemClick(it) },
                    selectedContentColor = AmberLight,
                    unselectedContentColor = Color.Gray,
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (it.title == "cart" && cartItemsCount > 0) {
                                BadgedBox(badge = {
                                    Badge(
                                        backgroundColor = MaterialTheme.colors.secondary,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Text(
                                            text = cartItemsCount.toString(), color = Color.White
                                        )
                                    }
                                }) {
                                    Icon(
                                        imageVector = it.icon,
                                        contentDescription = null
                                    )
                                }
                            } else {
                                Icon(imageVector = it.icon, contentDescription = null)
                            }
                        }
                    })
            }
        }
    }
}