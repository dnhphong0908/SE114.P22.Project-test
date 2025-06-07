package com.example.mam.viewmodel.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManageNotificationViewModel():  ViewModel() {
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _typeList = MutableStateFlow(listOf("GENERAL",              // Thông báo chung
        "ORDER_PLACED",         // Đơn hàng đã được đặt
        "ORDER_RECEIVED",       // Đơn hàng đã được tiếp nhận
        "ORDER_PREPARING",      // Đơn hàng đang được chế biến
        "ORDER_DELIVERING",     // Đơn hàng đang được giao
        "ORDER_DELIVERED",      // Đơn hàng đã giao xong
        "PROMOTION" ))
    val typeList = _typeList.asStateFlow()

    private val _type = MutableStateFlow(typeList.value[0])
    val type = _type.asStateFlow()

    private val _userList = MutableStateFlow(listOf<User>())
    val userList = _userList.asStateFlow()

    private val _receiverList = MutableStateFlow(listOf<String>())
    val receiverList = _receiverList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun setTitle(title: String) {
        _title.value = title
    }
    fun setMessage(message: String) {
        _message.value = message
    }
    fun setReceiverList(receiverList: List<String>) {
        _receiverList.value = receiverList
    }

    fun setType(type: String) {
        _type.value = type
    }
    fun loadData() {
        // Simulate loading data
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call
                _userList.value = listOf(User("1", "John Doe"), User("2", "Jane Doe"))
            } catch (e: Exception) {
                // Handle error
            } finally {
                // Hide loading indicator
                _isLoading.value = false
            }
        }
    }
    fun sendNotification() {
        // Simulate sending notification
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call
                println("Notification sent to ${_receiverList.value.joinToString(", ")}")
            } catch (e: Exception) {
                // Handle error
            } finally {
                // Hide loading indicator
                _isLoading.value = false
            }
        }
    }

}