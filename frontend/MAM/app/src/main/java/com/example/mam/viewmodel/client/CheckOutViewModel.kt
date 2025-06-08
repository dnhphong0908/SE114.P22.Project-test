package com.example.mam.viewmodel.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.cart.CartResponse
import com.example.mam.entity.OrderItem
import com.example.mam.entity.PaymentType
import com.example.mam.entity.Promotion
import com.example.mam.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class CheckOutViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
):  ViewModel(){
    private val _cart = MutableStateFlow(CartResponse())
    val cart = _cart.asStateFlow()

    private val _user = MutableStateFlow<User>(User())
    val user = _user.asStateFlow()

    private val _orderItems = MutableStateFlow<MutableList<OrderItem>>(mutableListOf())
    val orderItems = _orderItems.asStateFlow()

    private val _address = MutableStateFlow("")
    val address = _address.asStateFlow()

    private val _discount = MutableStateFlow(Promotion())
    var discount = _discount.asStateFlow()

    private val _discountList = MutableStateFlow<MutableList<Promotion>>(mutableListOf())
    val discountList = _discountList.asStateFlow()

    private val _note = MutableStateFlow("")
    var note = _note.asStateFlow()

    private val _paymentOptions = MutableStateFlow<MutableList<PaymentType>>(mutableListOf())
    var paymentOptions = _paymentOptions.asStateFlow()
    var paymentOption: PaymentType = PaymentType()

    fun getPriceToString(price: Int): String{
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(price)} VND"
    }

    fun setDiscount(code: Promotion){
        _discount.value = code
    }

    fun setAddress(address: String){
        _address.value = address
        viewModelScope.launch {
            userPreferencesRepository.saveAddress(_address.value)
        }
    }

    fun setupPaymentOption(option: PaymentType): Unit{
        paymentOption = option
    }

    fun setNote(string: String){
        _note.value = string
    }

    fun loadAddress(){
        viewModelScope.launch {
            if(userPreferencesRepository.address.map { it }.first().isNotEmpty()) _address.value = userPreferencesRepository.address.map { it }.first()
        }
    }

    fun loadUser() {
        _user.value = User("001", "Đinh Thanh Tùng", "0904599204", "dinhthanhtung0312@gmail.com","ThanhTungDepTrai", "123456")
    }

    fun getUser(): User {
        return _user.value
    }
    fun loadCart() {

    }
    fun loadOrderItems(){

    }

    fun loadPaymentOptions() {
        _paymentOptions.value = mutableListOf(
            PaymentType("PT001", "Tiền mặt" ),
            PaymentType("PT002", "MoMo" ),
            PaymentType("PT003", "VNPay" ),
            PaymentType("PT004", "Scribe" )
        )
    }

    fun checkOut(){
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                CheckOutViewModel(
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }

}