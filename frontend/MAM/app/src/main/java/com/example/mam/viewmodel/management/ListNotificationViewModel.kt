package com.example.mam.viewmodel.management

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class ListNotificationViewModel(): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notiList = MutableStateFlow<MutableList<Notification>>(mutableListOf())
    val notiList: StateFlow<List<Notification>> = _notiList

    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf())
    val sortingOptions: StateFlow<List<String>> = _sortingOptions

    private val _selectedSortingOption = MutableStateFlow<String>("")
    val selectedSortingOption: StateFlow<String> = _selectedSortingOption

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchHistory = MutableStateFlow<List<String>>(mutableListOf())
    val searchHistory: StateFlow<List<String>> get() = _searchHistory

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting

    //update search history if search query is not blank and not in history
    fun setSearchHistory(query: String) {
        if (query.isNotBlank() && !_searchHistory.value.contains(query)) {
            val updatedHistory = _searchHistory.value.toMutableList().apply { add(query) }
            _searchHistory.value = updatedHistory.takeLast(10).reversed() // Giữ tối đa 10 lần tìm kiếm
        }
    }

    fun setSearch(query: String) {
        _searchQuery.value = query
    }

    fun searchNotification() {
        _notiList.value = _notiList.value.filter {
            it.title.contains(searchQuery.value, ignoreCase = true) }.toMutableList()
        setSearchHistory(searchQuery.value)
    }

    fun setSelectedSortingOption(option: String) {
        _selectedSortingOption.value = option
    }

    fun loadSortingOptions() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call
                _notiList.value = mutableListOf(
                    Notification(
                        id = "1",
                        title = "Test Notification",
                        content = "This is a test notification",
                        timestamp = Instant.now(),
                        isRead = false,
                        icon = Icons.Default.DoneAll,
                        createAt = Instant.now(),
                        updateAt = Instant.now(),
                        type = "ORDER_DELIVERING"
                    ),
                    Notification(
                        id = "2",
                        title = "Test Notification 2",
                        content = "This is a test notification 2",
                        timestamp = Instant.now(),
                        isRead = false,
                        icon = Icons.Default.DoneAll,
                        createAt = Instant.now(),
                        updateAt = Instant.now(),
                        type = "ORDER_DELIVERING"
                    )
                )
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}