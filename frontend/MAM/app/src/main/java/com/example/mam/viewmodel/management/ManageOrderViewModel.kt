package com.example.mam.viewmodel.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.Order
import com.example.mam.entity.OrderItem
import com.example.mam.entity.Shipper
import com.example.mam.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class ManageOrderViewModel(savedStateHandle: SavedStateHandle?): ViewModel() {
    private val orderId: String? = savedStateHandle?.get<String>("orderId")

    private val _orderID = MutableStateFlow<String>("")
    val orderID = _orderID.asStateFlow()

    private val _orderStatus = MutableStateFlow<Int>(0)
    val orderStatus = _orderStatus.asStateFlow()

    private val _shipper = MutableStateFlow<Shipper?>(null)
    val shipper = _shipper.asStateFlow()

    private val _user = MutableStateFlow<User>(User())
    val user = _user.asStateFlow()

    private val _order = MutableStateFlow<Order>(Order())
    val order = _order.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isStatusLoading = MutableStateFlow(false)
    val isStatusLoading = _isStatusLoading.asStateFlow()

    fun setStatus(status: Int) {
        _orderStatus.value++
    }

    fun updateStatus() {
        viewModelScope.launch {
            try {
                _isStatusLoading.value = true
                // Simulate network call
            } catch (e: Exception) {
                // Handle error
            } finally {
                // Hide loading indicator
                _isStatusLoading.value = false
            }
        }
    }

    fun loadData(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call
                _order.value = Order(
                    id = orderId ?: "",
                    usedId = "userId",
                    orderDate = null,
                    paymentId = "paymentId",
                    shippingAddress = "shippingAddress",
                    orderItems = mutableListOf(OrderItem()),
                    totalPrice = 0,
                    note = "note",
                    orderStatus = 0,
                    expectDeliveryTime = null,
                    actualDeliveryTime = null,
                    shipperId = null
                )
            } catch (e: Exception) {
                // Handle error
            } finally {
                // Hide loading indicator
                _isLoading.value = false
            }
        }
    }

    fun mockData(){
        _order.value = Order(
            id = "orderId",
            usedId = "userId",
            orderDate = Instant.now(),
            paymentId = "paymentId",
            shippingAddress = "shippingAddress",
            orderItems = mutableListOf(OrderItem(
                name = "Bánh mì",
                image = "https://example.com/image.jpg",
                id = "productId",
                quantity = 1,
                options = "Thịt nguội, dưa leo, rau thơm",
                price = 20000
            ), OrderItem(
                name = "Pizza hải sản",
                image = "https://example.com/image.jpg",
                id = "productId",
                quantity = 2,
                options = "25cm, Hành tây",
                price = 120000 * 2 + 10000 + 5000)
            ),
            totalPrice = 0,
            note = "note",
            orderStatus = 0,
            expectDeliveryTime = null,
            actualDeliveryTime = null,
            shipperId = "1"
        )
        _user.value = User(
                id = "userId",
        fullName = "Nguyễn Văn A",
        email = "",
        phoneNumber = "0123456789",
        address = "Hàn Thuyên, khu phố 6 P, Thủ Đức, Hồ Chí Minh.",
        avatarUrl = "",
        )
        _shipper.value = Shipper(
            id = "shipperId",
            name = "Nguyễn Văn B",
            phoneNumber = "0123456789",
            licensePlate = "55-C1 123.45",
        )
    }


}