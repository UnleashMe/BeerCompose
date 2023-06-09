package com.example.beercompose.presentation.fragments.menu_categories_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.presentation.util.navigation.Screen
import com.example.beercompose.util.LocalAppGradient

@Composable
fun MenuCategoriesListScreen(
    navController: NavController
) {
    val categories = listOf(
        MenuCategory.BeerCategory, MenuCategory.SnackCategory
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.surface)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAppGradient.current.gradient)
        ) {
            items(categories) {
                CategoryItem(menuCategory = it) {
                    navController.navigate(Screen.MenuItemsListScreen.passCategory(it))
                }
            }
        }
    }
}