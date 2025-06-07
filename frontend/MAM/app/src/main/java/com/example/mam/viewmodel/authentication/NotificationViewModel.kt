package com.example.mam.viewmodel.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.notification.NotificationResponse
import com.example.mam.entity.Notification
import com.example.mam.services.BaseService
import com.example.mam.viewmodel.client.HomeScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class NotificationViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val _notifications = MutableStateFlow<List<NotificationResponse>>(emptyList())
    val notifications = _notifications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun loadNotifications() {
        viewModelScope.launch {
            var currentPage = 0
            _isLoading.value = true
            val allNotifications = mutableListOf<NotificationResponse>()
            try {
                Log.d("NotificationViewModel", "Loading notifications")
                while (true) { // Loop until the last page
                    Log.d("NotificationViewModel", "Fetching page $currentPage")
                    val response = BaseService(userPreferencesRepository).notificationService.getMyNotifications(
                        page = currentPage,
                        filter = "")
                    Log.d("NotificationViewModel", "Status code: ${response.code()}")
                    if (response.isSuccessful) {
                        val page = response.body()
                        if (page != null){
                            allNotifications.addAll(page.content)
                            if (page.page >= (page.totalPages - 1)) {
                                break // Stop looping when the last page is reached
                            }
                            currentPage++ // Move to the next page
                            Log.d("NotificationViewModel", "Lấy trang ${page.page}")
                            _notifications.value = allNotifications.toList() // Update UI with new notifications

                        }
                        else break
                    } else {
                        Log.d("NotificationViewModel", "Tai thong bao thất bại: ${response.errorBody()?.string()}")
                        break // Stop loop on failure
                    }
                    _notifications.value = allNotifications.toList()
                }
            } catch (e: Exception) {
                Log.d("NotificationViewModel", "Không thể lấy thông báo: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    suspend fun markAllAsRead() {
        try{
            Log.d("NotificationViewModel", "Marking all notifications as read")
            val response = BaseService(userPreferencesRepository).notificationService.markAllMyNotificationsAsRead()
            if (response.isSuccessful) {
                Log.d("NotificationViewModel", "All notifications marked as read successfully")
                // Optionally, reload notifications to reflect changes
                loadNotifications()
            } else {
                Log.d("NotificationViewModel", "Failed to mark all notifications as read: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("NotificationViewModel", "Error marking all notifications as read: ${e.message}")
        }
    }

    suspend fun markAsRead(id: Long) {
        try {
            Log.d("NotificationViewModel", "Marking notification $id as read")
            val response = BaseService(userPreferencesRepository).notificationService.markNotificationAsRead(id)
            if (response.isSuccessful) {
                Log.d("NotificationViewModel", "Notification $id marked as read successfully")
                // Optionally, reload notifications to reflect changes
                loadNotifications()
            } else {
                Log.d("NotificationViewModel", "Failed to mark notification $id as read: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("NotificationViewModel", "Error marking notification $id as read: ${e.message}")
        }

    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                NotificationViewModel(application.userPreferencesRepository)
            }
        }
    }
}