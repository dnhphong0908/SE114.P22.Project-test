package com.example.mam.viewmodel.management

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.promotion.PromotionRequest
import com.example.mam.services.BaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.math.BigDecimal
import java.time.Instant

class ManagePromotionViewModel(
    savedStateHandle: SavedStateHandle?,
    private val userPreferencesRepository: UserPreferencesRepository,
): ViewModel() {
    private val _code = MutableStateFlow("")
    val code = _code.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _value = MutableStateFlow(0)
    val value = _value.asStateFlow()

    private val _startDate = MutableStateFlow<Instant>(Instant.now())
    val startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<Instant>(Instant.now())
    val endDate = _endDate.asStateFlow()

    private val _minValue = MutableStateFlow(BigDecimal.ZERO)
    val minValue = _minValue.asStateFlow()

    private val _createAt = MutableStateFlow(Instant.now())
    val createAt = _createAt.asStateFlow()

    private val _updateAt = MutableStateFlow(Instant.now())
    val updateAt = _updateAt.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun setCode(code: String) {
        _code.value = code
    }

    fun setDescription(description: String) {
        _description.value = description
    }

    fun setValue(value: Int) {
        _value.value = value
    }

    fun setStartDate(startDate: Instant) {
        _startDate.value = startDate
    }

    fun isStartDateValid(): Boolean {
        return (_startDate.value.isBefore(_endDate.value) && _startDate.value.isAfter(Instant.now())) || _startDate.value == _endDate.value
    }

    fun setEndDate(endDate: Instant) {
        _endDate.value = endDate
    }

    fun isEndDateValid(): Boolean {
        return (_endDate.value.isAfter(_startDate.value) && _endDate.value.isAfter(Instant.now())) || _endDate.value == _startDate.value
    }

    fun setMinValue(minValue: BigDecimal) {
        _minValue.value = minValue
    }

    suspend fun createPromotion(): Int{
        _isLoading.value = true
        try {
            Log.d("Promotion", "Bắt đầu them Khuyen mai")
            Log.d(
                "Promotion",
                "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
            )
            val request = PromotionRequest(
                code = _code.value,
                description = _description.value,
                startDate = _startDate.value.toString(),
                endDate = _endDate.value.toString(),
                minValue = _minValue.value,
                discountValue = _value.value.toBigDecimal()
            )

            val response = BaseService(userPreferencesRepository)
                .promotionService
                .createPromotion(request)
            Log.d("Promotion", "Status code: ${response.code()}")
            if (response.isSuccessful) {
                val promotion = response.body()
                if (promotion != null) {
                    _code.value = promotion.code
                    _description.value = promotion.description
                    _startDate.value = Instant.parse(promotion.startDate)
                    _endDate.value = Instant.parse(promotion.endDate)
                    _minValue.value = promotion.minValue
                }
                return 1
            } else {
                Log.d("Promotion", "Them khuyen mai thất bại: ${response.errorBody()?.string()}")
                return 0
            }
        } catch (e: Exception) {
            Log.d("Promotion", "Không thể them khuyen mai: ${e.message}")
            return 0
        } finally {
            _isLoading.value = false
            Log.d("Promotion", "Kết thúc them khuyen mai")
        }
    }

    fun clearData() {
        _code.value = ""
        _description.value = ""
        _value.value = 0
        _startDate.value = Instant.now()
        _endDate.value = Instant.now()
        _minValue.value = BigDecimal.ZERO
        _createAt.value = Instant.now()
        _updateAt.value = Instant.now()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                val savedStateHandle = this.createSavedStateHandle()
                ManagePromotionViewModel(
                    savedStateHandle = savedStateHandle,
                    userPreferencesRepository = application.userPreferencesRepository,
                )
            }
        }
    }
}