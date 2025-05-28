package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.R
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.product.CategoryResponse
import com.example.mam.entity.ProductCategory
import com.example.mam.services.BaseService
import com.example.mam.viewmodel.authentication.SignInViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListCategoryViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _categories = MutableStateFlow<MutableList<CategoryResponse>>(mutableListOf())
    val categories: StateFlow<List<CategoryResponse>> = _categories

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

    fun searchCategory() {
        //search and save history
        _categories.value = _categories.value.filter {
            it.name.contains(searchQuery.value, ignoreCase = true)
        }.toMutableList()
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
        var isContinue: Boolean = true
        while (isContinue) {
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    // Simulate network call
                    Log.d("Category", "Bắt đầu lấy Danh mục")
                    Log.d(
                        "Category",
                        "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
                    )
                    val response =
                        BaseService(userPreferencesRepository).productCategoryService.getCategories(
                            specification = ""
                        )
                    Log.d("Category", "Status code: ${response.code()}")
                    if (response.isSuccessful) {
                        val page = response.body()
                        page?.let {
                            _categories.value = (_categories.value + page.content).toMutableList()
                            isContinue = (page.page != (page.totalPages - 1))
                        }
                    } else {
                        Log.d("Category", "Lấy Danh mục thất bại: ${response.errorBody()}")
                        isContinue = false
                    }
                } catch (e: Exception) {
                    Log.d("Category", "Không thể lấy Danh mục: ${e.message}")
                } finally {
                    _isLoading.value = false
                    Log.d("Category", "Kết thúc lấy Danh mục")
                }
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ListCategoryViewModel(application.userPreferencesRepository)
            }
        }
    }
}