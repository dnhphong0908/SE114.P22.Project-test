package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.order.OrderRequest
import com.example.mam.dto.order.OrderResponse
import com.example.mam.dto.shipper.ShipperResponse
import com.example.mam.dto.user.UserResponse
import com.example.mam.entity.Order
import com.example.mam.entity.OrderItem
import com.example.mam.entity.Shipper
import com.example.mam.entity.User
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
    private val imageViewModel: ImageViewModel
): ViewModel() {
    private val orderId: String? = savedStateHandle?.get<String>("orderId")

    private val _orderID = MutableStateFlow<Long>(0L)
    val orderID = _orderID.asStateFlow()

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

    fun setStatus() {
        _orderStatus.value
    }

    suspend fun updateStatus(): Int {
        _isLoading.value = true
        try {
            Log.d("Order", "Bắt đầu cap nhat Don hang")
            Log.d(
                "Order",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val response = BaseService(userPreferencesRepository)
                .orderService
                .getOrderStatus(_orderID.value, _orderStatus.value)
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

    suspend fun loadData() {
        _isLoading.value = true
        try {
            Log.d("Order", "Bắt đầu lấy Don hang")
            Log.d(
                "Order",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val response =
                BaseService(userPreferencesRepository).orderService.getOrderById(_orderID.value)
            Log.d("Order", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val order = response.body()
                if (order != null) {
                    _orderStatus.value = order.orderStatus
                }

            } else {
                Log.d("Order", "Lấy Don hang thất bại: ${response.errorBody().toString()}")
            }
        } catch (e: Exception) {
            Log.d("Order", "Không thể lấy Don hang: ${e.message}")
        } finally {
            _isLoading.value = false
            Log.d("Order", "Kết thúc lấy Don hang")
        }

    }

//    fun mockData(){
//        _order.value = Order(
//            id = "orderId",
//            userId = "userId",
//            orderDate = Instant.now(),
//            paymentId = "paymentId",
//            shippingAddress = "shippingAddress",
//            orderItems = mutableListOf(OrderItem(
//                name = "Bánh mì",
//                image = "https://example.com/image.jpg",
//                id = "productId",
//                quantity = 1,
//                options = "Thịt nguội, dưa leo, rau thơm",
//                price = 20000
//            ), OrderItem(
//                name = "Pizza hải sản",
//                image = "https://example.com/image.jpg",
//                id = "productId",
//                quantity = 2,
//                options = "25cm, Hành tây",
//                price = 120000 * 2 + 10000 + 5000)
//            ),
//            totalPrice = 0,
//            note = "note",
//            orderStatus = 0,
//            expectDeliveryTime = null,
//            actualDeliveryTime = null,
//            shipperId = "1"
//        )
//        _user.value = User(
//                id = "userId",
//        fullName = "Nguyễn Văn A",
//        email = "",
//        phoneNumber = "0123456789",
//        address = "Hàn Thuyên, khu phố 6 P, Thủ Đức, Hồ Chí Minh.",
//        avatarUrl = "",
//        )
//        _shipper.value = Shipper(
//            id = "shipperId",
//            name = "Nguyễn Văn B",
//            phoneNumber = "0123456789",
//            licensePlate = "55-C1 123.45",
//        )
//    }


}