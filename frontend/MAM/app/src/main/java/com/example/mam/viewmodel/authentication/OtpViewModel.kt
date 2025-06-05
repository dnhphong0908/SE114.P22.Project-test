package com.plcoding.composeotpinput

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.Constant
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.authentication.ForgetPasswordRequest
import com.example.mam.dto.authentication.VerifyOTPRequest
import com.example.mam.services.BaseService
import com.example.mam.viewmodel.authentication.otp.OtpAction
import com.example.mam.viewmodel.authentication.otp.OtpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val VALID_OTP_CODE = "1414"

class OtpViewModel(
    savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private val _otp = MutableStateFlow<String>("")
    val otp: StateFlow<String> = _otp

    private val _email = MutableStateFlow<String>(savedStateHandle.get<String>("email") ?: "")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow<String>(savedStateHandle.get<String>("password") ?: "")



    suspend fun verifyOtp(otp: String): Int {
        try {
            val metadata = BaseService(userPreferencesRepository).authPublicService.getMetadata(listOf(
                Constant.metadata.OTP_ACTION.name
            ))
            Log.d("OtpViewModel", "Get metadata: ${metadata.code()}")
            if (!metadata.isSuccessful) {
                Log.d("OtpViewModel", "Error getting metadata: ${metadata.errorBody()?.string()}")
                return 0
            }
            Log.d("OtpViewModel", "Metadata: ${metadata.body()?.get(Constant.metadata.OTP_ACTION.name)?.get(0) ?: ""}")
            val otpRequest = VerifyOTPRequest(
                otp = otp,
                email = _email.value,
                action = metadata.body()?.get(Constant.metadata.OTP_ACTION.name)?.get(0) ?: ""
            )
            Log.d("OtpViewModel", "Verifying OTP: $otpRequest")
            val otpResponse = BaseService(userPreferencesRepository).authPublicService.verifyOtp(otpRequest)
            Log.d("OtpViewModel", "Verify OTP response: ${otpResponse.code()}")
            if (otpResponse.isSuccessful) {
                val code = otpResponse.body()?.code ?: return 0
                val request = ForgetPasswordRequest(
                    code, _password.value
                )
                val response = BaseService(userPreferencesRepository).authPublicService.forgetPassword(request)
                Log.d("OtpViewModel", "Forget password response: ${response.code()}")
                if (response.isSuccessful) {
                    Log.d("OtpViewModel", "Password changed successfully")
                    return 1
                } else {
                    Log.d("OtpViewModel", "Error changing password: ${response.errorBody()?.string()}")
                    return 0
                }
            } else {
                Log.d("OtpViewModel", "Error verifying OTP: ${otpResponse.errorBody()?.string()}")
                return 0
            }
        }
        catch (e: Exception) {
            // Handle the exception, e.g., log it or show a message to the user
            e.printStackTrace()
            Log.d("OtpViewModel", "Error verifying OTP: ${e.message}")
            return 0
        }
    }

    fun onAction(action: OtpAction) {
        when(action) {
            is OtpAction.OnChangeFieldFocused -> {
                _state.update { it.copy(
                    focusedIndex = action.index
                ) }
            }
            is OtpAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }
            OtpAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(state.value.focusedIndex)
                _state.update { it.copy(
                    code = it.code.mapIndexed { index, number ->
                        if(index == previousIndex) {
                            null
                        } else {
                            number
                        }
                    },
                    focusedIndex = previousIndex
                ) }
            }
        }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if(currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        _state.update { it.copy(
            code = newCode,
            focusedIndex = if(wasNumberRemoved || it.code.getOrNull(index) != null) {
                it.focusedIndex
            } else {
                getNextFocusedTextFieldIndex(
                    currentCode = it.code,
                    currentFocusedIndex = it.focusedIndex
                )
            },
            isValid = if(newCode.none { it == null }) {
                newCode.joinToString("") == VALID_OTP_CODE
            } else null
        ) }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if(currentFocusedIndex == null) {
            return null
        }

        if(currentFocusedIndex == 7) {
            return currentFocusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if(index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if(number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }

    fun setOTP(s: String) {
        _otp.value = s
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                val savedStateHandle = this.createSavedStateHandle()
                OtpViewModel(
                    savedStateHandle = savedStateHandle,
                    application.userPreferencesRepository)
            }
        }
    }
}