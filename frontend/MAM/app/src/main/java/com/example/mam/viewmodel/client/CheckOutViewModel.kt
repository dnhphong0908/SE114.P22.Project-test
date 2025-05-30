package com.example.mam.viewmodel.client

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.R
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.entity.Cart
import com.example.mam.entity.CartItem
import com.example.mam.entity.Order
import com.example.mam.entity.OrderItem
import com.example.mam.entity.PaymentType
import com.example.mam.entity.Product
import com.example.mam.entity.Promotion
import com.example.mam.entity.User
import com.example.mam.entity.VarianceOption
import com.example.mam.viewmodel.ImageViewModel
import com.example.mam.viewmodel.management.ManageCategoryViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent
import java.text.DecimalFormat
import java.time.Instant

class CheckOutViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
):  ViewModel(){
    private var _cart: Cart = Cart()

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

    fun getTotalPrice(): Int{
        return _cart.total - _discount.value.value
    }

    fun getCartTotalToString(): String{
        return _cart.getTotalToString()
    }

    fun getTotalPriceToString(): String {
        return getPriceToString(getTotalPrice())
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
        val newCart = Cart()
        val product1 = Product("P003", "Pizza truyền thống", "", "", 120000, true, "PC001", "")
        val product2 = Product("P006", "Hotdog truyền thống", "", "", 80000, true, "PC003", "")

        val option1 = VarianceOption("V007P003", "V007", "25cm", 10000)
        val option2 = VarianceOption("V004P003", "V004", "Hành tây", 5000)
        val option3 = VarianceOption("V008P003", "V007", "30cm", 15000)

        newCart.addItem(CartItem(product1, quantity = 2, options = mutableListOf(option1, option2)))
        newCart.addItem(CartItem(product2, quantity = 1, options = mutableListOf(option3)))

        _cart = newCart
    }
    fun loadOrderItems(){
        val tmpList: MutableList<OrderItem> = mutableListOf()
        _cart.items.forEach{ item ->
            tmpList.add(OrderItem(item.product.name, "item.product.img",item.product.id,item.quantity, item.getOptionsToString(), item.getPrice()))
        }
        _orderItems.value = tmpList
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
        val order: Order = Order("",user.value.id, Instant.now(),paymentOption.id,address.value, orderItems.value, getTotalPrice(),_note.value )
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