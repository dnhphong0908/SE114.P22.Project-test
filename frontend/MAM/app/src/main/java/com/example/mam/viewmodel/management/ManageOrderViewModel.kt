package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.Constant
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.order.OrderRequest
import com.example.mam.dto.order.OrderResponse
import com.example.mam.dto.shipper.ShipperResponse
import com.example.mam.dto.user.UserResponse
import com.example.mam.entity.Order
import com.example.mam.entity.OrderItem
import com.example.mam.entity.Shipper
import com.example.mam.entity.User
import com.example.mam.gui.screen.management.ManageOrderScreen
import com.example.mam.services.BaseService
import com.example.mam.viewmodel.ImageViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Instant

class ManageOrderViewModel(
    savedStateHandle: SavedStateHandle?,
    private val userPreferencesRepository: UserPreferencesRepository,
): ViewModel() {
    private val orderId: Long? = savedStateHandle?.get<Long>("orderId")

    private val _orderID = MutableStateFlow<Long>(orderId?: 0L)
    val orderID = _orderID.asStateFlow()

    private val _orderStatuses = MutableStateFlow<List<String>>(listOf())
    val orderStatuses = _orderStatuses.asStateFlow()

    private val _orderStatus = MutableStateFlow<String>("")
    val orderStatus = _orderStatus.asStateFlow()

    private val _shipper = MutableStateFlow<ShipperResponse?>(null)
    val shipper = _shipper.asStateFlow()

    private val _user = MutableStateFlow<UserResponse>(UserResponse())
    val user = _user.asStateFlow()

    private val _order = MutableStateFlow<OrderResponse>(OrderResponse())
    val order = _order.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isStatusLoading = MutableStateFlow(false)
    val isStatusLoading = _isStatusLoading.asStateFlow()

    fun getNextStatus(): String {
        val values = _orderStatuses.value
        val current = values.indexOfFirst { it == _orderStatus.value }
        return if (current in 0 until values.lastIndex) {
            values[current + 1]
        } else {
            values.firstOrNull() ?: ""
        }
    }

    suspend fun updateStatus(): Int {
        _isLoading.value = true
        try {
            val nextStatus = getNextStatus()
            Log.d("Order", "Bắt đầu cap nhat Don hang")
            Log.d(
                "Order",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val response = BaseService(userPreferencesRepository)
                .orderService
                .getOrderStatus(_orderID.value, nextStatus)
            Log.d("Order", "${_orderID.value}, ${_orderStatus.value}")

            Log.d("Order", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                loadData()
                return 1
            } else {
                Log.d("Order", "Cap nhat Don hang thất bại: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("Order", "Không thể cap nhat Don hang: ${e.message}")
            return 0
        } finally {
            _isLoading.value = false
            Log.d("Order", "Kết thúc cap nhat Don hang")
        }
    }

    suspend fun loadOrderStatus() {
        _isLoading.value = true
        try {
            val response = BaseService(userPreferencesRepository).authPublicService.getMetadata(
                listOf(Constant.metadata.ORDER_STATUS.name)
            )
            Log.d("OrderViewModel", "Response Code: ${response.code()}")
            if (response.isSuccessful) {
                _orderStatuses.value = response.body()?.get(Constant.metadata.ORDER_STATUS.name) ?: listOf()
                Log.d("OrderViewModel", "Order status loaded successfully: ${_orderStatuses.value.size} statuses")
            } else {
                Log.d("OrderViewModel", "Failed to load order status: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("OrderViewModel", "Failed to load order status: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun loadData() {
        _isLoading.value = true
        try {
            Log.d("Order", "Bắt đầu lấy Don hang")
            Log.d(
                "Order",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            Log.d("Order", "${_orderID.value}")
            val response =
                BaseService(userPreferencesRepository).orderService.getOrderById(_orderID.value)
            Log.d("Order", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val order = response.body()
                if (order != null) {
                    _order.value = order
                    _orderStatus.value = order.orderStatus
                    Log.d("Order", "Lấy Don hang thành công: ${order.orderStatus}")
                    Log.d("Order", "Lấy Don hang thành công: ${order.orderDetails.size} items")
                    Log.d("Order", "Lấy Don hang thành công: ${order.createdAt} items")

                    val user = BaseService(userPreferencesRepository)
                        .userService
                        .getUserById(order.userId)
                    if (user.isSuccessful) {
                        _user.value = user.body() ?: UserResponse()
                    } else {
                        Log.d("Order", "Lấy thông tin người dùng thất bại: ${user.errorBody()?.string()}")
                    }
                    if (order.shipperId != null) {
                        val shipperResponse = BaseService(userPreferencesRepository)
                            .shipperService
                            .getShipperById(order.shipperId)
                        if (shipperResponse.isSuccessful) {
                            _shipper.value = shipperResponse.body()
                        } else {
                            Log.d("Order", "Lấy thông tin Shipper thất bại: ${shipperResponse.errorBody()?.string()}")
                        }
                    } else {
                        _shipper.value = null
                    }
                }

            } else {
                Log.d("Order", "Lấy Don hang thất bại: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.d("Order", "Không thể lấy Don hang: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Order", "Kết thúc lấy Don hang")
        }

    }
    suspend fun cancelOrder() :Int {
        try {
            val response = BaseService(userPreferencesRepository).orderService.cancelOrder(_orderID.value)
            Log.d("OrderViewModel", "Canceling order with ID: $orderId, Response Code: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("OrderViewModel", "Order canceled successfully")
                loadData()
                return 1
            } else {
                Log.d("OrderViewModel", "Failed to cancel order: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("OrderViewModel", "Exception while canceling order: ${e.message}")
            return 0
            // Handle exception
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                val savedStateHandle = this.createSavedStateHandle()
                ManageOrderViewModel(
                    savedStateHandle = savedStateHandle,
                    userPreferencesRepository = application.userPreferencesRepository,
                )
            }
        }
    }

}