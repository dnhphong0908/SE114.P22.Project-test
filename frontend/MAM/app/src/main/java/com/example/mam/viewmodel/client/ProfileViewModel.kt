package com.example.mam.viewmodel.client

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.authentication.RefreshTokenRequest
import com.example.mam.dto.user.UserResponse
import com.example.mam.entity.User
import com.example.mam.services.BaseService
import com.example.mam.viewmodel.ImageViewModel
import com.example.mam.viewmodel.authentication.SignUpViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import kotlin.math.log

class ProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val imageViewModel: ImageViewModel
) : ViewModel() {
    private val _user = MutableStateFlow<UserResponse>(UserResponse())
    val user: StateFlow<UserResponse> = _user.asStateFlow()

    private val _userAvatarFile = MutableStateFlow<File?>(null)
    val userAvatarFile = _userAvatarFile.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing

    private var _userId = 0L

    fun setEditing(value: Boolean) {
        _isEditing.value = value
    }

    fun setFullName(fullName: String) {
        _user.update { it.copy(fullname = fullName) }
    }

    fun setPhoneNumber(phoneNumber: String){
        _user.update { it.copy(phone = phoneNumber) }
    }
    fun setEmail(email: String){
        _user.update { it.copy(email = email) }
    }
    fun setUserName(username: String){
        _user.update { it.copy(username = username) }
    }
    fun setUserAvatar(image: String) {
        _user.update { it.copy(avatarUrl = image) }
    }
    fun setUserAvatarFile(context: Context, uri: Uri){
        val file = imageViewModel.uriToFile(context, uri)
        _userAvatarFile.value = file
    }

    suspend fun fetchUser() {
        try {
            val response = BaseService(userPreferencesRepository).authPrivateService.getUserInfo()
            Log.d("ME","Status code: ${response.code()}")
            if(response.isSuccessful){
                if(response.body()!=null) {
                    _user.value = response.body()!!
                    _userId = _user.value.id
                    Log.d("ME", "Lay nguoi dung thanh cong ${_user.value.id}")
                    Log.d("ME", "Avatar nguoi dung thanh cong ${_user.value.avatarUrl}")
                }
                else{
                    Log.d("ME", "Nguoi dung null")
                }
            }
            else {
                Log.d("ME","Loi lay nguoi dung ${response.errorBody()?.string()}")
            }
        } catch (e: Exception){
            Log.d("ME", "Loi ket noi server: ${e.message}")
        }
    }

    suspend fun updateUser(): Int{
        Log.d("ME","bat dau chinh sua")
        try{
            val currentUser = _user.value
            Log.d("ME", "Ma nguoi dung ${_user.value.id}, ${currentUser.id}")
            val fullname = currentUser.fullname.toRequestBody("text/plain".toMediaType())
            Log.d("ME", "ID: ${currentUser.fullname}")
            val username = currentUser.username.toRequestBody("text/plain".toMediaType())
            Log.d("ME", "ID: ${currentUser.username}")
            val email = currentUser.email.toRequestBody("text/plain".toMediaType())
            Log.d("ME", "ID: ${currentUser.email}")
            val phone = currentUser.phone.replace("+84", "0").toRequestBody("text/plain".toMediaType())
            Log.d("ME", "ID: ${currentUser.phone}")
            val roleId = currentUser.role.id.toString().toRequestBody("text/plain".toMediaType())
            Log.d("ME", "ID: ${currentUser.role.id}")

            val imageFile = _userAvatarFile.value
            val requestFile = imageFile?.asRequestBody("image/*".toMediaType())
            val imagePart = requestFile?.let { MultipartBody.Part.createFormData("image", imageFile.name, it) }

            val response = BaseService(userPreferencesRepository).userService.updateUser(
                _userId,
                fullname, username, email, phone, imagePart, roleId
            )

            Log.d("ME", "Status update code: ${response.code()}")

            return if (response.isSuccessful) {
                Log.d("ME", "Cập nhật user thành công")
                1
            } else {
                Log.d("ME", "Lỗi cập nhật user: ${response.errorBody()?.string()}")
                0
            }        }catch (e:Exception){
            Log.d("ME", "Loi ket noi server: ${e.message}")
            return 0
        }

    }
    suspend fun logOut(): Int{
        Log.d("ME","bat dau dang xuat")
        try {
            val response = BaseService(userPreferencesRepository).authPrivateService.logOut(
                RefreshTokenRequest(userPreferencesRepository.refreshToken.map { it }.first())
            )
            Log.d("ME","Status logout code: ${response.code()}")
            if (response.isSuccessful){
                userPreferencesRepository.saveAccessToken("","")
                userPreferencesRepository.saveAddress("")
                return 1
            }
            else {
                Log.d("ME","Loi logout ${response.errorBody()?.string()}")
                return 0
            }
        }
        catch (e: Exception){
            Log.d("ME","Loi ket noi server ${e.message}")
            return 0
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                ProfileViewModel(
                    application.userPreferencesRepository,
                    ImageViewModel())
            }
        }
    }
}