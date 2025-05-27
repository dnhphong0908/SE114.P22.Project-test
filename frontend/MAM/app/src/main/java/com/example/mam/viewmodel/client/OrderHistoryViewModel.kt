package com.example.mam.viewmodel.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.Order
import com.example.mam.services.APIservice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

enum class OrderStatusFilter(val code: Int) {
    ALL(-1),
    PENDING(0),
    DELIVERED(1),
    CANCELED(2);

    companion object {
        fun fromCode(code: Int): OrderStatusFilter {
            return values().firstOrNull { it.code == code } ?: ALL
        }
    }
}

open class OrderHistoryViewModel(
    private val api: APIservice
): ViewModel() {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _statusFilter = MutableStateFlow(OrderStatusFilter.ALL)
    val statusFilter: StateFlow<OrderStatusFilter> = _statusFilter

    private val _dateFilter = MutableStateFlow<LocalDate?>(null)
    val dateFilter: StateFlow<LocalDate?> = _dateFilter

    val filteredOrder = combine(_orders, _dateFilter) { orders, dateFilter ->
        orders.filter { order ->
            dateFilter?.let { selectedDate ->
                val localDate = order.actualDeliveryTime
                    ?.atZone(ZoneId.systemDefault())
                    ?.toLocalDate()
                localDate == selectedDate
            } ?: true
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadOrders() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = api.getOrders()
                _orders.value = response
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun setStatusFilter(status: OrderStatusFilter) {
        _statusFilter.value = status
    }

    fun setDateFilter(date: LocalDate?) {
        _dateFilter.value = date
    }
}