package com.example.mam.viewmodel.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.Shipper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListShipperViewModel(): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _shipper = MutableStateFlow<MutableList<Shipper>>(mutableListOf())
    val shipper: StateFlow<List<Shipper>> = _shipper

    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf())
    val sortingOptions: StateFlow<List<String>> = _sortingOptions

    private val _selectedSortingOption = MutableStateFlow<String>("")
    val selectedSortingOption: StateFlow<String> = _selectedSortingOption

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchHistory = MutableStateFlow<List<String>>(mutableListOf())
    val searchHistory: StateFlow<List<String>> get() = _searchHistory

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

    fun searchShipper() {
        _shipper.value = _shipper.value.filter {
            it.name.contains(searchQuery.value, ignoreCase = true) ||
                    it.phoneNumber.contains(searchQuery.value, ignoreCase = true)}.toMutableList()
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
                _shipper.value = mutableListOf(
                    Shipper("Nguyễn Văn A", "0123456789", "59-A1 999.99", "1"),
                    Shipper("Trần Thị B", "0987654321", "59-A1 999.99", "2"),
                    Shipper("Lê Văn C", "0912345678", "59-A1 999.99", "3")
                )
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}