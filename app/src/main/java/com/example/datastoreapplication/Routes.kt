package com.example.datastoreapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datastoreapplication.home.InventoryScreen
import com.example.datastoreapplication.screens.ItemEntryScreen

object Routes {
    const val INVENTORY = "inventory"
    const val ADD_ITEM = "add_item"
}


@Composable
fun AppNavHost() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.INVENTORY
    ) {
        composable(Routes.INVENTORY) {
            InventoryScreen(
                navController = navController,
                onAddClick = {
                    // Navigate to the new screen when FAB is tapped
                    navController.navigate(Routes.ADD_ITEM)
                }, navigateToItemUpdate = {}
            )
        }

        composable(Routes.ADD_ITEM) {
            ItemEntryScreen(
navigateBack ={navController.popBackStack()},
onNavigateUp ={navController.navigateUp()})
                }
    }
}


