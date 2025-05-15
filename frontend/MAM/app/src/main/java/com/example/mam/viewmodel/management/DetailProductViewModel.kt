package com.example.mam.viewmodel.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.Product
import com.example.mam.entity.ProductCategory
import com.example.mam.entity.Variance
import com.example.mam.entity.VarianceOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale.Category

class DetailProductViewModel(savedStateHandle: SavedStateHandle): ViewModel() {
    private val _productId = savedStateHandle.get<String>("productId")
        ?: throw IllegalArgumentException("Product ID is required")

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _category = MutableStateFlow<ProductCategory?>(null)
    val category: StateFlow<ProductCategory?> = _category

    private val _variants = MutableStateFlow<Map<Variance, List<VarianceOption>>>(emptyMap())
    val variants: StateFlow<Map<Variance, List<VarianceOption>>> = _variants

    fun loadData(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call to fetch product details
                // For example, you can use a repository to fetch data from a database or API
                // _product.value = repository.getProductById(_productId)
                // _category.value = repository.getCategoryByProductId(_productId)
                // _variants.value = repository.getVariantsByProductId(_productId)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

}