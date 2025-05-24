package com.example.mam.viewmodel.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
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

    suspend fun SignIn(): Int{
        return withContext(Dispatchers.IO) {
            try {
                val request = _signInState.value
                Log.d("LOGIN", "CredentialID: ${request.credentialId}")
                Log.d("LOGIN", "Password: ${request.password}")
                val response = BaseService(accessToken = accessToken.first()).authPublicService.login(request)
                Log.d("LOGIN", "AccessToken: ${response.accessToken}")
                Log.d("LOGIN", "RefreshToken: ${response.refreshToken}")
                // Lưu access token và refresh token vào DataStore
                userPreferencesRepository.saveAccessToken(response.accessToken, response.refreshToken)
                Log.d("LOGIN", "DSAccessToken: ${accessToken.first()}")
                Log.d("LOGIN", "DSRefreshToken: ${refreshToken.first()}")
                return@withContext if (response.accessToken.isEmpty()) 0 else 1
            } catch (e: Exception) {
                Log.e("LOGIN", "Lỗi khi đăng nhập: ${e.message}")
                0
            }
            finally {
                Log.d("LOGIN", "Kết thúc đăng nhập")
            }
        }
    }

    fun notifySignInFalse(){

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                SignInViewModel(application.userPreferencesRepository)
            }
        }
    }
}