package com.example.datastoreapplication.screens

import android.R.attr.id
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.datastoreapplication.data.Item
import com.example.datastoreapplication.data.ItemsRepository
import java.text.NumberFormat


    public class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

        var itemUiState by mutableStateOf(ItemUiState())
            private set

        /**
         * Updates the [itemUiState] with the value provided in the argument. This method also triggers
         * a validation for input values.
         */
        fun updateUiState(itemDetails: ItemDetails) {
            itemUiState =
                ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
        }


        suspend fun saveItem() {
            if (validateInput()) {
                itemsRepository.insertItem(itemUiState.itemDetails.toItem())
            }
        }


        private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
            return with(uiState) {
                name.isNotBlank() && quantity.isNotBlank()
            }
        }
    }

    /**
     * Represents Ui State for an Item.
     */
    data class ItemUiState(
        val itemDetails: ItemDetails = ItemDetails(),
        val isEntryValid: Boolean = false
    )

    data class ItemDetails(
        val name: String = "",
        val age: Int = 0,
        val quantity: String = ""
    )

    /**
     * Extension function to convert [ItemDetails] to [Item]. If the value of [ItemDetails.price] is
     * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
     * [ItemDetails.quantity] is not a valid [Int], then the quantity will be set to 0
     */
    fun ItemDetails.toItem(): Item = Item(
        name = name, age = age, quantity = quantity
    )
    /*
    fun Item.formatedPrice(): String {
        return NumberFormat.getCurrencyInstance().format(price)
    }
 */
    /**
     * Extension function to convert [Item] to [ItemUiState]
     */
    fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
        itemDetails = this.toItemDetails(),
        isEntryValid = isEntryValid
    )

    /**
     * Extension function to convert [Item] to [ItemDetails]
     */
    fun Item.toItemDetails(): ItemDetails = ItemDetails(
        name = name, age = age, quantity = quantity
    )


/*
 id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString()
 */