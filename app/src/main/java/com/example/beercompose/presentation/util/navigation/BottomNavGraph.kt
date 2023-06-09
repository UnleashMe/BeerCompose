package com.example.beercompose.presentation.util.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.beercompose.presentation.fragments.addbeer.components.AddUpdateScreen
import com.example.beercompose.presentation.fragments.cart.components.CartScreen
import com.example.beercompose.presentation.fragments.favourites.components.FavouritesScreen
import com.example.beercompose.presentation.fragments.itemlist.components.ItemListScreen
import com.example.beercompose.presentation.fragments.login.components.LoginScreen
import com.example.beercompose.presentation.fragments.menu_categories_list.components.MenuCategoriesListScreen
import com.example.beercompose.presentation.fragments.profile.components.ProfileScreen
import com.example.beercompose.presentation.fragments.register.components.RegisterScreen
import com.example.beercompose.presentation.fragments.singleItem.components.ItemInfoScreen
import com.example.beercompose.util.Constants

@Composable
fun BottomNavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = "main_route") {
        navigation(startDestination = BottomBarScreen.ProfileScreen.route, route = "user_route") {
            composable(route = BottomBarScreen.LoginScreen.route) {
                LoginScreen(navController)
            }
            composable(route = Screen.RegistrationScreen.route) {
                RegisterScreen(navController)
            }
            composable(route = BottomBarScreen.ProfileScreen.route) {
                ProfileScreen(navController = navController)
            }
        }
        composable(route = BottomBarScreen.CartScreen.route) {
            CartScreen(navController)
        }
        composable(route = BottomBarScreen.FavouritesScreen.route) {
            FavouritesScreen(navController)
        }
        navigation(
            startDestination = BottomBarScreen.MenuCategoriesListScreen.route,
            route = "main_route"
        ) {
            composable(route = BottomBarScreen.MenuCategoriesListScreen.route) {
                MenuCategoriesListScreen(navController)
            }
            composable(route = Screen.AddUpdateScreen.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("category") {
                    type = NavType.StringType
                }
            )) {
                AddUpdateScreen(navController = navController)
            }
            composable(route = Screen.ItemInfoScreen.route) {
                ItemInfoScreen(navController = navController)
            }
            composable(
                route = Screen.MenuItemsListScreen.route,
                arguments = listOf(
                    navArgument(Constants.MENU_LIST_CATEGORY_ARG) {
                        type = NavType.StringType
                    }
                )
            ) {
                ItemListScreen(navController)
            }
        }
    }
}