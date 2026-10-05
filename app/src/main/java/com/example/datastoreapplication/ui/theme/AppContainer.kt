package com.example.datastoreapplication.ui.theme


import android.content.Context
import com.example.datastoreapplication.data.InventoryDatabase
import com.example.datastoreapplication.data.ItemsRepository
import com.example.datastoreapplication.data.OfflineItemsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
}

/**
 * [AppContainer] implementation that provides instance of [com.example.datastoreapplication.data.OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }
}