package com.example.mam.viewmodel.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class ManageCategoryViewModel(savedStateHandle: SavedStateHandle?): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val categoryId = savedStateHandle?.get<String>("categoryId") ?: ""

    private val _categoryID = MutableStateFlow<String>(categoryId)
    val categoryID: StateFlow<String> = _categoryID

    private val _categoryName = MutableStateFlow<String>("")
    val categoryName: StateFlow<String> = _categoryName

    private val _categoryDescription = MutableStateFlow<String>("")
    val categoryDescription: StateFlow<String> = _categoryDescription

    private val _categoryImage = MutableStateFlow<String>("")
    val categoryImage: StateFlow<String> = _categoryImage

    private val _createdAt = MutableStateFlow<Instant>(Instant.now())
    val createdAt: StateFlow<Instant> = _createdAt

    private val _updatedAt = MutableStateFlow<Instant>(Instant.now())
    val updatedAt: StateFlow<Instant> = _updatedAt

    fun setCategoryName(name: String) {
        _categoryName.value = name
    }

    fun isCategoryNameValid(): String {
        val name = _categoryName.value.trim()
        val regex = "^[\\p{L} ]+$".toRegex()
        if (name.length > 20) {
            return "Tên quá dài (>20 ký tự)" // Name is too long
        }
        else if (name.length < 3) {
            return "Tên quá ngắn (<3 ký tự)" // Name is too short
        }
        else if (!regex.matches(name)) {
            return "Tên không hợp lệ" // Name contains invalid characters
        }
        else return ""
    }

    fun setCategoryDescription(description: String) {
        _categoryDescription.value = description
    }

    fun isCategoryDescriptionValid(): String {
        val description = _categoryDescription.value.trim()
        if (description.length > 100) {
            return "Mô tả quá dài (>100 ký tự)" // Description is too long
        }
        else if (description.length < 5) {
            return "Mô tả quá ngắn (<5 ký tự)" // Description is too short
        }
        else return ""
    }

    fun setCategoryImage(image: String) {
        _categoryImage.value = image
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call
                // Load data from repository or database
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateCategory() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call
                // Update category in repository or database
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCategory() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call
                // Add category to repository or database
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun mockData() {
        _categoryID.value = "1"
        _categoryName.value = "Category Name"
        _categoryDescription.value = "Category Description"
        _categoryImage.value = "https://mars.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg"
        _createdAt.value = Instant.now()
        _updatedAt.value = Instant.now()
    }
}