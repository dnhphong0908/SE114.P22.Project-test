package com.example.mam.viewmodel.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.authentication.RefreshTokenRequest
import com.example.mam.services.BaseService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class StartViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val accessToken = userPreferencesRepository.accessToken.map { it }
    private val refreshToken = userPreferencesRepository.refreshToken.map { it }

    suspend fun getAccessToken(): Int {
        Log.d("REFRESH", "DSRefreshToken: ${refreshToken.first()}")
        if (refreshToken.first().isEmpty()) {
            Log.d("REFRESH", "No refresh token found, returning 0")
            return 0
        }
        else {
            try {
                val response =
                    BaseService(userPreferencesRepository).authPublicService.refreshToken(
                        RefreshTokenRequest(refreshToken = refreshToken.first()))
                val statusCode = response.code()
                if (response.isSuccessful) {
                    val token = response.body()
                    Log.d("REFRESH", "Status Code: $statusCode")
                    userPreferencesRepository.saveAccessToken(
                        token?.accessToken ?: "",
                        token?.refreshToken ?: ""
                    )
                    Log.d("REFRESH", "DSAccessToken: ${accessToken.first()}")
                    Log.d("REFRESH", "DSRefreshToken: ${refreshToken.first()}")
                    val me = BaseService(userPreferencesRepository).authPrivateService.getUserInfo().body()!!
                    return if (me.role.name == "ADMIN") 1
                    else 2
                } else {
                    Log.d("REFRESH", "Status Code: $statusCode")
                    return 0
                }
            } catch (e: Exception) {
                Log.e("REFRESH", "Lỗi khi refresh: ${e.message}")
                return 0
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                StartViewModel(application.userPreferencesRepository)
            }
        }
    }
}