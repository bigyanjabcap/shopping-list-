package com.example.datastoreapplication.ui.theme

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.datastoreapplication.home.HomeViewModel
import com.example.datastoreapplication.screens.ItemEntryViewModel

object AppViewModelProviider {
    val Factory = viewModelFactory {
        // Other Initializers
        // Initializer for ItemEntryViewModel
        initializer {
            ItemEntryViewModel(inventoryApplication().container.itemsRepository)
        }

        //...
        initializer {
            HomeViewModel(inventoryApplication().container.itemsRepository)
        }
    }
}

/**
 * Extension function to queries for application object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.inventoryApplication(): InventoryApplication =
    (this[APPLICATION_KEY] as InventoryApplication)