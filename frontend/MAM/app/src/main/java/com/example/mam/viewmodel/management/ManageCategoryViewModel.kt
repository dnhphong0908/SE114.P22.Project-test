package com.example.mam.viewmodel.management

import android.content.Context
import android.net.Uri
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
import com.example.mam.services.BaseService
import com.example.mam.viewmodel.ImageViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.time.Instant

class ManageCategoryViewModel(
    savedStateHandle: SavedStateHandle?,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val imageViewModel: ImageViewModel
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val categoryId = savedStateHandle?.get<Long>("categoryId") ?: 0L

    private val _categoryID = MutableStateFlow<Long>(categoryId)
    val categoryID: StateFlow<Long> = _categoryID

    private val _categoryName = MutableStateFlow<String>("")
    val categoryName: StateFlow<String> = _categoryName

    private val _categoryDescription = MutableStateFlow<String>("")
    val categoryDescription: StateFlow<String> = _categoryDescription

    private val _categoryImage = MutableStateFlow<String>("")
    val categoryImage: StateFlow<String> = _categoryImage

    private val _categoryImageFile = MutableStateFlow<MultipartBody.Part?>(null) // Lưu ảnh dạng Multipart

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
        return if (name.length > 20) {
            "Tên quá dài (>20 ký tự)" // Name is too long
        } else if (name.length < 3) {
            "Tên quá ngắn (<3 ký tự)" // Name is too short
        } else if (!regex.matches(name)) {
            "Tên không hợp lệ" // Name contains invalid characters
        } else ""
    }

    fun setCategoryDescription(description: String) {
        _categoryDescription.value = description
    }

    fun isCategoryDescriptionValid(): String {
        val description = _categoryDescription.value.trim()
        return if (description.length > 100) {
            "Mô tả quá dài (>100 ký tự)" // Description is too long
        } else if (description.length < 5) {
            "Mô tả quá ngắn (<5 ký tự)" // Description is too short
        } else ""
    }

    fun setCategoryImage(image: String) {
        _categoryImage.value = image
    }

    fun setCategoryImageFile(context: Context, uri: Uri) {
        _categoryImageFile.value = imageViewModel.getMultipartFromUri(context, uri)
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("Category", "Bắt đầu lấy Danh mục")
                Log.d(
                    "Category",
                    "DSAccessToken: ${userPreferencesRepository.accessToken.first()}"
                )
                val response =
                    BaseService(userPreferencesRepository).productCategoryService.getCategory(_categoryID.value)
                Log.d("Category", "Status code: ${response.code()}")
                if (response.isSuccessful) {
                    val category = response.body()
                    if (category != null) {
                        _categoryName.value = category.name
                        _categoryDescription.value = category.description
                        _createdAt.value = Instant.parse(category.createdAt)
                        _updatedAt.value = Instant.parse(category.updatedAt)
                        _categoryImage.value = category.imageUrl
                    }

                } else {
                    Log.d("Category", "Lấy Danh mục thất bại: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.d("Category", "Không thể lấy Danh mục: ${e.message}")
            } finally {
                _isLoading.value = false
                Log.d("Category", "Kết thúc lấy Danh mục")
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

    fun createCategory() {
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
        _categoryID.value = 1
        _categoryName.value = "Category Name"
        _categoryDescription.value = "Category Description"
        _categoryImage.value = "https://mars.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg"
        _createdAt.value = Instant.now()
        _updatedAt.value = Instant.now()
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                val savedStateHandle = this.createSavedStateHandle()
                ManageCategoryViewModel(
                    savedStateHandle = savedStateHandle,
                    userPreferencesRepository = application.userPreferencesRepository,
                    imageViewModel = ImageViewModel()
                )
            }
        }
    }
}