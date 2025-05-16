package com.example.mam.viewmodel.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class ManageUserViewModel(savedStateHandle: SavedStateHandle?): ViewModel() {
    private val userId: String? = savedStateHandle?.get<String>("userId")

    private val _userID = MutableStateFlow<String>("")
    val userID = _userID.asStateFlow()

    private val _userName = MutableStateFlow<String>("")
    val userName = _userName.asStateFlow()

    private val _imgUrl = MutableStateFlow<String>("")
    val imgUrl = _imgUrl.asStateFlow()

    private val _fullName = MutableStateFlow<String>("")
    val fullName = _fullName.asStateFlow()

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _role = MutableStateFlow<String>("")
    val role = _role.asStateFlow()

    private val _phone = MutableStateFlow<String>("")
    val phone = _phone.asStateFlow()

    private val _statusList = MutableStateFlow<List<String>>(listOf("Active", "Inactive", "Blocked" , "Deleted"))
    val statusList = _statusList.asStateFlow()

    private val _status = MutableStateFlow<String>("")
    val status = _status.asStateFlow()

    private val _createAt = MutableStateFlow<Instant>(Instant.now())
    val createAt = _createAt.asStateFlow()

    private val _updateAt = MutableStateFlow<Instant>(Instant.now())
    val updateAt = _updateAt.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun setUserName(userName: String) {
        _userName.value = userName
    }

    fun isUserNameValid(): String {
        val name = _userName.value.trim()
        val regex = "^[\\p{L}0-9 ]+$".toRegex()
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

    fun setImgUrl(imgUrl: String) {
        _imgUrl.value = imgUrl
    }

    fun setFullName(fullName: String) {
        _fullName.value = fullName
    }

    fun isFullNameValid(): String {
        val name = _fullName.value.trim()
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

    fun setEmail(email: String) {
        _email.value = email
    }

    fun isEmailValid(): String {
        val email = _email.value.trim()
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!regex.matches(email)) {
            return "Email không hợp lệ" // Email contains invalid characters
        }
        return "" // Email is valid
    }
    fun setRole(role: String) {
        _role.value = role
    }

    fun setPhone(phone: String) {
        _phone.value = phone
    }

    fun isPhoneValid(): String {
        val phone = _phone.value.trim()
        val regex = "^0\\d{9}$".toRegex()
        if (!regex.matches(phone)) {
            return "Số điện thoại không hợp lệ" // Phone contains invalid characters
        }
        return "" // Phone is valid
    }

    fun setStatus(status: String) {
        _status.value = status
    }

    fun loadData(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate a network call or data loading
                // Replace this with your actual data loading logic
                // For example, you can fetch data from a repository or API
                // and update the state variables accordingly.
                //_userID.value = userId ?: ""
                //_userName.value = "John Doe"
                //_imgUrl.value = "https://example.com/image
            } catch (e: Exception) {
                // Handle any errors that occur during data loading
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun mockData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate a network call or data loading
                _userID.value = "1"
                _userName.value = "Joe Mama"
                _imgUrl.value = "https://mars.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg"
                _fullName.value = "Đinh Thanh Tùng"
                _email.value = "dinhthanhtung0312@gmail.com"
                _role.value = "Admin"
                _phone.value = "0123456789"
                _status.value = "Active"
                _createAt.value = Instant.now()
                _updateAt.value = Instant.now()

            } catch (e: Exception) {
                // Handle any errors that occur during data loading
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clear() {
        _userID.value = ""
        _userName.value = ""
        _imgUrl.value = ""
        _fullName.value = ""
        _email.value = ""
        _role.value = ""
        _phone.value = ""
        _status.value = ""
        _createAt.value = Instant.now()
        _updateAt.value = Instant.now()
    }

    fun updateUser() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate a network call or data loading
                // Replace this with your actual data loading logic
                // For example, you can fetch data from a repository or API
                // and update the state variables accordingly.
            } catch (e: Exception) {
                // Handle any errors that occur during data loading
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun addUser() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate a network call or data loading
                // Replace this with your actual data loading logic
                // For example, you can fetch data from a repository or API
                // and update the state variables accordingly.
            } catch (e: Exception) {
                // Handle any errors that occur during data loading
            } finally {
                _isLoading.value = false
            }
        }
    }
}