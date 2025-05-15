package com.example.mam.viewmodel.management

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mam.entity.ProductCategory
import com.example.mam.entity.Variance
import com.example.mam.entity.VarianceOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Instant

class ManageProductViewModel(savedStateHandle: SavedStateHandle? ): ViewModel() {
    private val productId: String? = savedStateHandle?.get<String>("productId")

    private val _categoryList =  MutableStateFlow<List<ProductCategory>>(emptyList())
    val categoryList = _categoryList.asStateFlow()

    private val _productID = MutableStateFlow("")
    val productID = _productID.asStateFlow()

    private val _creatAt = MutableStateFlow(Instant.now())
    val createAt = _creatAt.asStateFlow()
    private val _updateAt = MutableStateFlow(Instant.now())
    val updateAt = _updateAt.asStateFlow()

    private val _productName = MutableStateFlow("")
    val productName = _productName.asStateFlow()

    private val _productShortDescription = MutableStateFlow("")
    val productShortDescription = _productShortDescription.asStateFlow()

    private val _productLongDescription = MutableStateFlow("")
    val productLongDescription = _productLongDescription.asStateFlow()

    private val _productPrice = MutableStateFlow(0)
    val productPrice = _productPrice.asStateFlow()

    private val _productCategory = MutableStateFlow<ProductCategory>(ProductCategory())
    val productCategory = _productCategory.asStateFlow()

    private val _productImageUrl = MutableStateFlow("https://static.vecteezy.com/system/resources/previews/056/202/171/non_2x/add-image-or-photo-icon-vector.jpg")
    val productImageUrl = _productImageUrl.asStateFlow()

    private val _isAvailable = MutableStateFlow(false)
    val isAvailable = _isAvailable.asStateFlow()

    private val _variants = MutableStateFlow<Map<Variance, List<VarianceOption>>>(emptyMap())
    val variants = _variants.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isSetting = MutableStateFlow(false)
    val isSetting = _isSetting.asStateFlow()

    fun setProductName(name: String) {
        _productName.value = name
    }

    fun isProductNameValid(): Int {
        val name = _productName.value.trim()
        val regex = "^[\\p{L}]+$".toRegex()
        if (name.length > 50) {
            return 1 // Name is too long
        }
        else if (name.length < 5) {
            return 2 // Name is too short
        }
        else if (!regex.matches(name)) {
            return 3 // Name contains invalid characters
        }
        return 0
    }

    fun setProductShortDescription(shortDescription: String) {
        _productShortDescription.value = shortDescription
    }

    fun isProductShortDescriptionValid(): Int {
        val shortDescription = _productShortDescription.value.trim()
        if (shortDescription.length > 100) {
            return 1 // Short description is too long
        }
        else if (shortDescription.length < 10) {
            return 2 // Short description is too short
        }
        return 0
    }

    fun setProductLongDescription(longDescription: String) {
        _productLongDescription.value = longDescription
    }

    fun isProductLongDescriptionValid(): Int {
        val longDescription = _productLongDescription.value.trim()
        if (longDescription.length > 500) {
            return 1 // Long description is too long
        }
        else if (longDescription.length < 20) {
            return 2 // Long description is too short
        }
        return 0
    }

    fun setProductPrice(price: Int) {
        _productPrice.value = price
    }

    fun isProductPriceValid(): Int {
        val price = _productPrice.value
        if (price < 1000) {
            return 1 // Price is negative
        }
        else if (price > 1000000000) {
            return 2 // Price is too high
        }
        return 0
    }

    fun setProductCategory(category: ProductCategory) {
        _productCategory.value = category
    }

    fun setProductImageUrl(imageUrl: String) {
        _productImageUrl.value = imageUrl
    }

    fun createImagePartFromUri(context: Context, uri: Uri): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)!!
        val fileBytes = inputStream.readBytes()
        val requestBody = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("avatar", "avatar.jpg", requestBody)
    }

    fun setIsAvailable(available: Boolean) {
        _isAvailable.value = available
    }

    fun addVariant(variant: Variance) {
        _variants.value += (variant to emptyList())
    }
    fun updateVariantIsMultipleChoice(variant: Variance) {
        val updateMap = _variants.value.toMutableMap().apply {
            val currentOptions = this.remove(variant)
            if (currentOptions != null) {
                this[variant.copy(isMutipleChoice = !variant.isMutipleChoice)] = currentOptions
            }
        }
        _variants.value = updateMap
    }
    fun addVariantOption(variant: Variance, option: VarianceOption) {
        _variants.value[variant]?.let {
            _variants.value = _variants.value + (variant to (it + option))
        }
    }

    fun removeVariant(variant: Variance) {
        _variants.value -= variant
    }

    fun removeVariantOption(variant: Variance, option: VarianceOption) {
        _variants.value[variant]?.let {
            _variants.value = _variants.value + (variant to (it - option))
        }
    }

    fun setCategoryList() {
        _categoryList.value = listOf(
            ProductCategory("1", "Category 1"),
            ProductCategory("2", "Category 2"),
            ProductCategory("3", "Category 3")
        )
        _productCategory.value = _categoryList.value[0]
    }

    fun loadData() {
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



    fun addProduct() {
        viewModelScope.launch {
            try {
                _isSetting.value = true
                // Simulate network call to add product
                // For example, you can use a repository to add data to a database or API
                // repository.addProduct(
                //     name = _productName.value,
                //     shortDescription = _productShortDescription.value,
                //     longDescription = _productLongDescription.value,
                //     price = _productPrice.value,
                //     category = _productCategory.value,
                //     imageUrl = _productImageUrl.value,
                //     isAvailable = _isAvailable.value,
                //     variants = _variants.value
                // )
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isSetting.value = false
            }
        }
    }

    fun updateProduct() {
        viewModelScope.launch {
            try {
                _isSetting.value = true
                // Simulate network call to update product
                // For example, you can use a repository to update data in a database or API
                // repository.updateProduct(
                //     id = productId,
                //     name = _productName.value,
                //     shortDescription = _productShortDescription.value,
                //     longDescription = _productLongDescription.value,
                //     price = _productPrice.value,
                //     category = _productCategory.value,
                //     imageUrl = _productImageUrl.value,
                //     isAvailable = _isAvailable.value,
                //     variants = _variants.value
                // )
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isSetting.value = false
            }
        }
    }

    fun mockData(){
        _productID.value = "1"
        _creatAt.value = Instant.now()
        _updateAt.value = Instant.now()
        _productName.value = "Product Name"
        _productShortDescription.value = "Short Description"
        _productLongDescription.value = "Long Description"
        _productPrice.value = 100000
        _productCategory.value = ProductCategory("1", "Category 1")
        _productImageUrl.value = "https://images.squarespace-cdn.com/content/v1/5ec1febb58a4890157c8fbeb/19ebb9ed-4862-46e1-9f7c-4e5876730227/Beetroot-Burger.jpg"
        _isAvailable.value = true
        _categoryList.value = listOf(
            ProductCategory("1", "Category 1"),
            ProductCategory("2", "Category 2"),
            ProductCategory("3", "Category 3")
        )
        _variants.value = mapOf(
            Variance("1", "Variant 1", "1", false) to listOf(
                VarianceOption("1", "1", "Option 1"),
                VarianceOption("2", "2", "Option 2")
            )
        )
    }
}