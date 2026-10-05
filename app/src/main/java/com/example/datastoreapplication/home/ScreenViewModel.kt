package com.example.datastoreapplication.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScreenViewModel: ViewModel() {

    private val _results = MutableStateFlow<List<String>>(emptyList())
    val results = _results.asStateFlow()
    private val allItems = listOf("Apple", "Banana", "Orange", "Mango", "Pineapple", "Grapes")
    fun search(query: String) {
        viewModelScope.launch {
            // replace with your real data source / API call
            _results.value = allItems.filter { it.contains(query, ignoreCase = true) }
        }
    }
}