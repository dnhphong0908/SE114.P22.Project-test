package com.example.mam.viewmodel.management

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManageShipperViewModel(savedStateHandle: SavedStateHandle?): ViewModel() {
    private val shipperId = savedStateHandle?.get<String>("shipperId")

    private val _shipperID = MutableStateFlow<String>("")
    val shipperID: MutableStateFlow<String> get() = _shipperID

    private val _shipperName = MutableStateFlow<String>("")
    val shipperName: MutableStateFlow<String> get() = _shipperName

    private val _shipperPhone = MutableStateFlow<String>("")
    val shipperPhone: MutableStateFlow<String> get() = _shipperPhone

    private val _shipperLicense = MutableStateFlow<String>("")
    val shipperLicense: MutableStateFlow<String> get() = _shipperLicense

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun setShipperName(name: String) {
        _shipperName.value = name
    }

    fun isShipperNameValid(): String {
        val name = _shipperName.value.trim()
        val regex = "^[\\p{L} ]+$".toRegex()
        if (name.length > 50) {
            return "Tên quá dài (>50 ký tự)" // Name is too long
        }
        else if (name.length < 5) {
            return "Tên quá ngắn (<5 ký tự)" // Name is too short
        }
        else if (!regex.matches(name)) {
            return "Tên không hợp lệ" // Name contains invalid characters
        }
        return "" // Name is valid
    }

    fun setShipperPhone(phone: String) {
        _shipperPhone.value = phone
    }

    fun isPhoneValid(): String {
        val phone = _shipperPhone.value.trim()
        val regex = "^0\\d{9}$".toRegex()
        if (!regex.matches(phone)) {
            return "Số điện thoại không hợp lệ" // Phone contains invalid characters
        }
        return "" // Phone is valid
    }

    fun setShipperLicense(license: String) {
        _shipperLicense.value = license
    }

    fun isLicenseValid(): String {
        val license = _shipperLicense.value.trim()
        val regex = "^([1-9][0-9])-[ABCDEFGHKLM][1-9] \\d{3}\\.\\d{2}\$".toRegex()
        if (!regex.matches(license)) {
            return "Biển số không hợp lệ" // License contains invalid characters
        }
        return "" // License is valid
    }

    fun loadData(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate a network call or data fetching
                // Replace with actual data fetching logic

            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun mockData() {
        _shipperID.value = "123456"
        _shipperName.value = "Nguyễn Văn A"
        _shipperPhone.value = "0123456789"
        _shipperLicense.value = "55-A1 123.45"
    }

    fun clearData() {
        _shipperID.value = ""
        _shipperName.value = ""
        _shipperPhone.value = ""
        _shipperLicense.value = ""
    }

    fun updateShipper() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate a network call or data fetching
                // Replace with actual data fetching logic

            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addShipper() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate a network call or data fetching
                // Replace with actual data fetching logic

            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}