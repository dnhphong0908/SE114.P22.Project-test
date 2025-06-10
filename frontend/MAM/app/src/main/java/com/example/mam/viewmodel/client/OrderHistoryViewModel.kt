package com.example.mam.viewmodel.client

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.order.OrderResponse
import com.example.mam.entity.Order
import com.example.mam.services.BaseService
import com.example.mam.viewmodel.authentication.NotificationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId


class OrderHistoryViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _orders = MutableStateFlow<List<OrderResponse>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _asc = MutableStateFlow(true)
    val asc: StateFlow<Boolean> = _asc

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun loadOrders() {
        var currentPage = 0
        val allOrders = mutableListOf<OrderResponse>()
        try {
            _isLoading.value = true
            while (true) {
                val response = BaseService(userPreferencesRepository).orderService.getMyOrders(
                    page = currentPage,
                    sort = listOf("createdAt,${if (_asc.value) "asc" else "desc"}"),
                    filter = "",
                )
                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null) {
                        allOrders.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // No more pages to load
                        }
                        currentPage++
                        _orders.value = allOrders
                        _isLoading.value = false
                        Log.d(
                            "OrderHistoryViewModel",
                            "Orders loaded successfully: ${_orders.value.size} orders"
                        )
                    }
                } else {
                    Log.e(
                        "OrderHistoryViewModel",
                        "Failed to load orders: ${response.errorBody()?.string()}"
                    )
                    break
                    _isLoading.value = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("OrderHistoryViewModel", "Failed to load orders: ${e.message}")

        } finally {
            _isLoading.value = false
        }
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                OrderHistoryViewModel(application.userPreferencesRepository)
            }
        }
    }
}