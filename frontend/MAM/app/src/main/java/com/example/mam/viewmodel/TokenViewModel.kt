package com.example.mam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.DataStoreManager
import kotlinx.coroutines.launch

class TokenViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    fun saveTokens(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            dataStoreManager.saveTokens(accessToken, refreshToken)
        }
    }

    fun getAccessToken(callback: (String?) -> Unit) {
        viewModelScope.launch {
            val token = dataStoreManager.getAccessToken()
            callback(token)
        }
    }

    fun getRefreshToken(callback: (String?) -> Unit) {
        viewModelScope.launch {
            val token = dataStoreManager.getRefreshToken()
            callback(token)
        }
    }
}