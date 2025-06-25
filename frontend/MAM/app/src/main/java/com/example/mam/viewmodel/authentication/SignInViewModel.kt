package com.example.mam.viewmodel.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.authentication.FirebaseLoginRequest
import com.example.mam.dto.authentication.SendVerifyEmailRequest
import com.example.mam.dto.authentication.SignInRequest
import com.example.mam.dto.user.UserResponse
import com.example.mam.repository.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


class SignInViewModel(
    // Nhận UserPreferencesRepository từ MAMApplication
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    // Lấy access token và refresh token từ UserPreferencesRepository
    private val accessToken = userPreferencesRepository.accessToken.map { it }
    private val refreshToken = userPreferencesRepository.refreshToken.map { it }

    private val _signInState = MutableStateFlow(SignInRequest())
    val signInState = _signInState.asStateFlow()

    private val _me = MutableStateFlow(UserResponse())

    fun setUsername(it: String) {
        _signInState.update { state -> state.copy(credentialId = it) }
    }

    fun setSIPassword(it: String) {
        _signInState.update { state -> state.copy(password = it.trim()) }
    }

    suspend fun SignIn(): Int {
        try {
            val request = _signInState.value
            Log.d("LOGIN", "CredentialID: ${request.credentialId}")
            Log.d("LOGIN", "Password: ${request.password}")

            // Gọi service đăng nhập
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.login(request)
            val statusCode = response.code()
            Log.d("LOGIN", "Status Code: $statusCode")

            if (response.isSuccessful) {
                val token = response.body()
                if (token != null) {
                    _me.value = token.user
                }
                Log.d("LOGIN", "AccessToken: ${token?.accessToken}")
                Log.d("LOGIN", "RefreshToken: ${token?.refreshToken}")
                // Lưu access token và refresh token vào DataStore
                userPreferencesRepository.saveAccessToken(token?.accessToken ?: "", token?.refreshToken ?:"")
                Log.d("LOGIN", "DSAccessToken: ${accessToken.first()}")
                Log.d("LOGIN", "DSRefreshToken: ${refreshToken.first()}")

                return if (_me.value.status == "DELETED") -1
                else if (_me.value.status == "BLOCKED") -2
                else if (_me.value.status == "PENDING") -3
                else if (_me.value.role.name == "ADMIN") 1
                else if (_me.value.role.name == "USER") 2
                else 0
            }
            else{
                Log.e("LOGIN", "Đăng nhập thất bại với mã lỗi: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Lỗi khi đăng nhập: ${e.message}")
            return 0
        }
    }

    suspend fun signInWithFirebase(idToken: String): Int {
        try {
            val request = FirebaseLoginRequest(idToken)
            // Gọi service đăng nhập với Firebase
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.loginFirebase(request)
            val statusCode = response.code()
            Log.d("LOGIN", "Status Code: $statusCode")

            if (response.isSuccessful) {
                val token = response.body()
                if (token != null) {
                    _me.value = token.user
                }
                Log.d("LOGIN", "AccessToken: ${token?.accessToken}")
                Log.d("LOGIN", "RefreshToken: ${token?.refreshToken}")
                Log.d("LOGIN", "User: ${_me.value}")
                // Lưu access token và refresh token vào DataStore
                userPreferencesRepository.saveAccessToken(token?.accessToken ?: "", token?.refreshToken ?:"")
                Log.d("LOGIN", "DSAccessToken: ${accessToken.first()}")
                Log.d("LOGIN", "DSRefreshToken: ${refreshToken.first()}")

                return if (_me.value.status == "DELETED") -1
                else if (_me.value.status == "BLOCKED") -2
                else if (_me.value.status == "PENDING") -3
                else if (_me.value.role.name == "ADMIN") 1
                else if (_me.value.role.name == "USER") 2
                else 0
            } else {
                Log.e("LOGIN", "Đăng nhập thất bại với mã lỗi: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Lỗi khi đăng nhập với Firebase: ${e.message}")
            return 0
        }
    }

    suspend fun resendVerificationEmail() : Int {
        try {
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.sendVerifyEmail(
                SendVerifyEmailRequest(
                    email = _me.value.email
                )
            )
            if (response.isSuccessful) {
                Log.d("LOGIN", "Email xác thực đã được gửi lại thành công.")
                return 1
            } else {
                Log.e("LOGIN", "Gửi lại email xác thực thất bại: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "Lỗi khi gửi lại email xác thực: ${e.message}")
            return 0
        }
    }
    // Factory để khởi tạo ViewModel với tham số là UserPreferencesRepository
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                SignInViewModel(application.userPreferencesRepository)
            }
        }
    }
}