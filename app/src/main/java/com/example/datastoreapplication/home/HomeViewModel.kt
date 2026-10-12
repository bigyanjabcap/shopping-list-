package com.example.datastoreapplication.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datastoreapplication.data.Item
import com.example.datastoreapplication.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel (private val itemsRepository: ItemsRepository): ViewModel(){

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val homeUiState: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState())





    var selectedNames by mutableStateOf(setOf<String>())
        private set

    fun toggleSelection(name: String) {
        selectedNames = if (name in selectedNames) selectedNames - name else selectedNames + name
    }

    fun clearSelection() {
        selectedNames = emptySet()
    }

    fun deleteSelected() {
        val names = selectedNames.toList()
        viewModelScope.launch {
            itemsRepository.deleteItems(names)
            selectedNames = emptySet()
        }
    }
}







data class HomeUiState(val itemList: List<Item> = listOf())