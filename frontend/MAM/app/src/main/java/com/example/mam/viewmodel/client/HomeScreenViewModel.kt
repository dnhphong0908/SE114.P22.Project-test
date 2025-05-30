package com.example.mam.viewmodel.client

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mam.MAMApplication
import com.example.mam.R
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.user.UserResponse
import com.example.mam.entity.Product
import com.example.mam.entity.ProductCategory
import com.example.mam.services.BaseService
import com.example.mam.viewmodel.authentication.StartViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class HomeScreenViewModel(private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private val accessToken = userPreferencesRepository.accessToken.map { it }
    private val refreshToken = userPreferencesRepository.refreshToken.map { it }

    private val _listCategory = MutableStateFlow<MutableList<ProductCategory>>(mutableListOf())
    private val _listProduct = MutableStateFlow<MutableList<Product>>(mutableListOf())
    private val _user = MutableStateFlow<UserResponse>(UserResponse())
    fun getListCategory(): List<ProductCategory> {
        return _listCategory.value.toList()
    }

    fun getListProduct(idCategory: String): List<Product> {
        val listProduct: MutableList<Product> = mutableListOf()
        for(product in _listProduct.value.toList()){
            if (product.idCategory.equals(idCategory)) listProduct.add(product)
        }
        return listProduct
    }

    fun loadListCategory(){
        _listCategory.value = mutableListOf(
            ProductCategory("PC000", "Đề xuất", "", "R.drawable.ic_glowing_star"),
            ProductCategory("PC001", "Pizza", "", "R.drawable.ic_pizza"),
            ProductCategory("PC002", "Burger", "", "R.drawable.ic_hamburger"),
            ProductCategory("PC003", "Hotdog", "", "R.drawable.ic_hotdog"),
            ProductCategory("PC004", "Gà rán", "", "R.drawable.ic_chicken"),
            ProductCategory("PC005", "Thịt", "", "R.drawable.ic_meat"),
            ProductCategory("PC006", "Thức uống", "", "R.drawable.ic_drink"),
            ProductCategory("PC007", "Khác", "", "R.drawable.ic_other")
        )
    }

    fun clearListCategory(){
        _listCategory.value.clear()
    }

    fun loadListProduct(){
        _listProduct.value = mutableListOf(
            Product("P000", "Burger thịt hun khói phô mai", "", "", 100000, true, "PC002", ""),
            Product("P001", "Burger thịt hun khói phô mai", "", "", 100000, true, "PC002", ""),
            Product("P002", "Burger thịt hun khói phô mai", "", "", 100000, true, "PC002", ""),
            Product("P003", "Pizza truyền thống", "", "", 100000, true, "PC001", ""),
            Product("P004", "Pizza truyền thống", "", "", 100000, true, "PC001", ""),
            Product("P005", "Pizza truyền thống", "", "", 100000, true, "PC001", ""),
            Product("P006", "Hotdog truyền thống", "", "", 100000, true, "PC003", ""),
            Product("P007", "Hotdog truyền thống", "", "", 100000, false, "PC003", ""),
            Product("P008", "Hotdog truyền thống", "", "", 100000, true, "PC003", ""),
            Product("P009", "Gà rán", "", "", 100000, true, "PC004", "")
        )
    }

    fun clearListProduct(){
        _listProduct.value.clear()
    }

    suspend fun loadUser() {
        try {
            _user.value = BaseService(userPreferencesRepository).authPrivateService.getUserInfo().body() ?: UserResponse()
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