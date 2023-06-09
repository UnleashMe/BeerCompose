package com.example.beercompose.presentation.util.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.beercompose.presentation.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            if (navBackStackEntry?.destination?.route == BottomBarScreen.CartScreen.route) {
                CartScreenBottomBar(state.price) {
                    viewModel.onOrderClick()
                }
            } else {
                BottomBar(
                    navController = navController,
                    onItemClick = { navController.navigate(it.route) },
                    cartItemsCount = state.itemsInCart
                )
            }
        }
    ) {
        BottomNavGraph(navController = navController)
    }
}