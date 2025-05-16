package com.example.mam.viewmodel.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _processingOrderNum = MutableStateFlow(0)
    val processingOrderNum: StateFlow<Int> = _processingOrderNum

    private val _notProcessingOrderNum = MutableStateFlow(0)
    val notProcessingOrderNum: StateFlow<Int> = _notProcessingOrderNum

    fun loadData() {
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
}