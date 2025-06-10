package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.order.OrderRequest
import com.example.mam.dto.order.OrderResponse
import com.example.mam.dto.product.CategoryResponse
import com.example.mam.dto.user.UserResponse
import com.example.mam.services.BaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ListOrderViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _orders = MutableStateFlow<MutableList<OrderResponse>>(mutableListOf())
    val orders: StateFlow<List<OrderResponse>> = _orders

    private val _sortingOptions = MutableStateFlow<MutableList<String>>(mutableListOf(
        "Tất cả",
        "Ngày đặt hàng",
        "Giá tiền",
    ))
    val sortingOptions: StateFlow<List<String>> = _sortingOptions

    private val _asc = MutableStateFlow<Boolean>(true)
    val asc = _asc.asStateFlow()

    private val _orderState = MutableStateFlow(OrderRequest())
    val orderState = _orderState.asStateFlow()

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

    suspend fun searchOrder() {
        _isLoading.value = true
        var currentPage = 0
        val allOrders = mutableListOf<OrderResponse>()

        try {
            while (true) { // Loop until the last page
                val response = BaseService(userPreferencesRepository)
                    .orderService.getAllOrders(filter = "actualDeliveryTime ~~ '*${_searchQuery.value}*' or shippingAddress ~~ '*${_searchQuery.value}*' or note ~~ '*${_searchQuery.value}*' or expectedDeliveryTime ~~ '*${_searchQuery.value}*' or orderStatus ~~ '*${_searchQuery.value}*' or orderDetails ~~ '*${_searchQuery.value}*'", page = currentPage)
                if (response.isSuccessful) {
                    setSearchHistory(_searchQuery.value)
                    val page = response.body()
                    if (page != null){
                        allOrders.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _orders.value = allOrders.toMutableList()
                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }

            _orders.value = allOrders.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun sortOrder(){
        _isLoading.value = true
        var currentPage = 0
        val allOrders = mutableListOf<OrderResponse>()
        val sortOption = when(_selectedSortingOption.value){
            "Ngày đặt hàng" -> "actualDeliveryTime"
            "Giá tiền" -> "totalPrice"
            else -> "id"
        }
        try {
            while (true) { // Loop until the last page
                val response = BaseService(userPreferencesRepository)
                    .orderService.getAllOrders(
                        filter = "",
                        page = currentPage,
                        sort = listOf("${sortOption}," + if (_asc.value) "asc" else "desc"))

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allOrders.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _orders.value = allOrders.toMutableList()
                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }

            _orders.value = allOrders.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }


    fun setSelectedSortingOption(option: String) {
        _selectedSortingOption.value = option
    }

    fun loadOwnerOfOrder(id: Long): UserResponse {
        return UserResponse()
    }

    fun setASC(){
        _asc.value = !_asc.value
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

    suspend fun loadData() {
        _isLoading.value = true
        var currentPage = 0
        val allOrders = mutableListOf<OrderResponse>()

        try {
            while (true) { // Loop until the last page
                val response = BaseService(userPreferencesRepository)
                    .orderService.getAllOrders(filter = "", page = currentPage)

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allOrders.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        _orders.value = allOrders.toMutableList()

                    }
                    else break
                } else {
                    break // Stop loop on failure
                }
            }
            _orders.value = allOrders.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
        } finally {
            _isLoading.value = false
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ListOrderViewModel(application.userPreferencesRepository)
            }
        }
    }
}