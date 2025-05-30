package com.example.mam.viewmodel.authentication

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.authentication.SignInRequest
import com.example.mam.services.BaseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext


class SignInViewModel(
    // Nhận UserPreferencesRepository từ MAMApplication
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    // Lấy access token và refresh token từ UserPreferencesRepository
    private val accessToken = userPreferencesRepository.accessToken.map { it }
    private val refreshToken = userPreferencesRepository.refreshToken.map { it }

    private val _signInState = MutableStateFlow(SignInRequest())
    val signInState = _signInState.asStateFlow()

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
            val response = BaseService(userPreferencesRepository).authPublicService.login(request)
            val statusCode = response.code()
            Log.d("LOGIN", "Status Code: $statusCode")

            if (response.isSuccessful) {
                val token = response.body()
                Log.d("LOGIN", "AccessToken: ${token?.accessToken}")
                Log.d("LOGIN", "RefreshToken: ${token?.refreshToken}")
                // Lưu access token và refresh token vào DataStore
                userPreferencesRepository.saveAccessToken(token?.accessToken ?: "", token?.refreshToken ?:"")
                Log.d("LOGIN", "DSAccessToken: ${accessToken.first()}")
                Log.d("LOGIN", "DSRefreshToken: ${refreshToken.first()}")
                return 1
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