package com.example.beercompose.presentation.util.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.beercompose.data.common.MenuCategory
import com.example.beercompose.util.Constants.ITEM_INFO_ID_ARG
import com.example.beercompose.util.Constants.MENU_LIST_CATEGORY_ARG

sealed class Screen(val route: String) {
    object MenuItemsListScreen :
        Screen(route = "menu_items_list_screen/{$MENU_LIST_CATEGORY_ARG}") {
        fun passCategory(category: MenuCategory): String {
            val strCategory = category.toString()
            return "menu_items_list_screen/$strCategory"
        }
    }

    object AddUpdateScreen : Screen(route = "add_update_screen?id={id}/{category}") {
        fun passId(id: String?, category: MenuCategory): String {
            val strCategory = category.toString()
            return "add_update_screen?id=$id/$strCategory"
        }
    }

    object ItemInfoScreen : Screen(route = "item_info_screen/{$ITEM_INFO_ID_ARG}") {
        fun passId(id: String): String {
            return "item_info_screen/$id"
        }
    }

    object RegistrationScreen : Screen(route = "registration_screen")
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
) {
    object LoginScreen : BottomBarScreen(
        route = "login",
        title = "login",
        icon = Icons.Default.Login
    )

    object ProfileScreen : BottomBarScreen(
        route = "profile",
        title = "profile",
        icon = Icons.Default.Person
    )

    object MenuCategoriesListScreen : BottomBarScreen(
        route = "menu_categories",
        title = "menu",
        icon = Icons.Default.LocalDrink
    )

    object FavouritesScreen : BottomBarScreen(
        route = "favourites",
        title = "favourites",
        icon = Icons.Default.Favorite
    )

    object CartScreen : BottomBarScreen(
        route = "cart",
        title = "cart",
        icon = Icons.Default.ShoppingCart
    )
}