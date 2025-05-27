package com.example.mam.viewmodel.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class ManagePromotionViewModel(): ViewModel() {
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

    private val _minValue = MutableStateFlow(0)
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

    fun setMinValue(minValue: Int) {
        _minValue.value = minValue
    }

    fun addPromotion() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
            } catch (e: Exception) {
                // Handle error
            } finally {
                // Hide loading indicator
                clearData()
                _isLoading.value = false
            }
        }
    }

    fun clearData() {
        _code.value = ""
        _description.value = ""
        _value.value = 0
        _startDate.value = Instant.now()
        _endDate.value = Instant.now()
        _minValue.value = 0
        _createAt.value = Instant.now()
        _updateAt.value = Instant.now()
    }
}