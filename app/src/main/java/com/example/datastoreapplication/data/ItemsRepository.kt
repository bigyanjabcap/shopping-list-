package com.example.datastoreapplication.data

import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getAllItemsStream(): Flow<List<Item>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItemStream(name: String): Flow<Item?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: Item)

    /**
     * Delete item from the data source
     */

    suspend fun deleteItems(names: List<String>)

    suspend fun updateItem(item: Item)
}



class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()
    override fun getItemStream(name: String): Flow<Item?> = itemDao.getItem(name)
    override suspend fun insertItem(item: Item) = itemDao.insert(item)
    override suspend fun deleteItems(names: List<String>) = itemDao.deleteByNames(names)
    override suspend fun updateItem(item: Item) = itemDao.update(item)
}
