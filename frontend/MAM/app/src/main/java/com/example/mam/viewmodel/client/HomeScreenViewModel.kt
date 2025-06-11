package com.example.mam.viewmodel.client

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.product.CategoryResponse
import com.example.mam.dto.product.ProductResponse
import com.example.mam.dto.user.UserResponse
import com.example.mam.repository.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class HomeScreenViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val accessToken = userPreferencesRepository.accessToken.map { it }
    private val refreshToken = userPreferencesRepository.refreshToken.map { it }

    private val _listCategory = MutableStateFlow<MutableList<CategoryResponse>>(mutableListOf())
    private val _listProduct = MutableStateFlow<MutableList<ProductResponse>>(mutableListOf())
    private val _user = MutableStateFlow<UserResponse>(UserResponse())

    private val _productMap = MutableStateFlow<Map<Long, List<ProductResponse>>>(emptyMap())
    val productMap: StateFlow<Map<Long, List<ProductResponse>>> = _productMap
    fun getListCategory(): List<CategoryResponse> {
        return _listCategory.value.toList()
    }

    fun getListProduct(categoryId: Long): List<ProductResponse> {
        val listProduct: MutableList<ProductResponse> = mutableListOf()
        for(product in _listProduct.value.toList()){
            if (product.categoryId.equals(categoryId)) listProduct.add(product)
        }
        return listProduct
    }

    suspend fun loadListCategory(){
        var currentPage = 0
        val allCategories = mutableListOf<CategoryResponse>()
        try {
            Log.d("Category", "Bắt đầu lấy Danh mục")

            while (true) { // Loop until the last page
                val response = BaseRepository(userPreferencesRepository)
                    .productCategoryRepository
                    .getCategories(filter = "", page = currentPage)

                Log.d("Category", "Status code: ${response.code()}")

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        allCategories.addAll(page.content)
                        if (page.page >= (page.totalPages - 1)) {
                            break // Stop looping when the last page is reached
                        }
                        currentPage++ // Move to the next page
                        Log.d("Category", "Lấy trang ${page.page}")
                        _listCategory.value = allCategories.toMutableList()

                    }
                    else break
                } else {
                    Log.d("Category", "Lấy Danh mục thất bại: ${response.errorBody()}")
                    break // Stop loop on failure
                }
            }

            _listCategory.value = allCategories.toMutableList() // Update UI with all categories

        } catch (e: Exception) {
            Log.d("Category", "Không thể lấy Danh mục: ${e.message}")
        }
    }

    fun clearListCategory(){
        _listCategory.value.clear()
    }

    suspend fun loadListProduct(id: Long): List<ProductResponse>{
        try {
            Log.d("Category", "Bắt đầu lấy Danh mục")
                val response = BaseRepository(userPreferencesRepository)
                    .productRepository
                    .getProductsByCategory(id,filter= "", page = 0)
                Log.d("Category", "Status code: ${response.code()}")

                if (response.isSuccessful) {
                    val page = response.body()
                    if (page != null){
                        return page.content
                    }
                    else return listOf()
                } else {
                    Log.d("Category", "Lấy Danh mục thất bại: ${response.errorBody()}")
                    return listOf()
                }
        } catch (e: Exception) {
            Log.d("Product", "Không thể lấy Sản phẩm: ${e.message}")
            return listOf()
        }
    }
    suspend fun loadAllProductsFromCategories(categories: List<CategoryResponse>){
        val result = mutableMapOf<Long, List<ProductResponse>>()
        for(category in categories){
            val products = loadListProduct(category.id)
            result[category.id] = products
        }
        _productMap.value = result
    }
    fun clearListProduct(){
        _listProduct.value.clear()
    }

    suspend fun loadUser() {
        try {
            _user.value = BaseRepository(userPreferencesRepository).authPrivateRepository.getUserInfo().body() ?: UserResponse()
            Log.d("USER", "User loaded: ${_user.value.username}")
        }
        catch (e: Exception) {
            Log.d("USER", "Error loading user: ${e.message}")
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MAMApplication)
                HomeScreenViewModel(application.userPreferencesRepository)
            }
        }
    }
}