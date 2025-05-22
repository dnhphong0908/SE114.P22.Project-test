package com.example.mam.viewmodel.client

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mam.R
import com.example.mam.entity.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel(): ViewModel() {
    private var _originListProduct = MutableStateFlow<MutableList<Product>>(mutableListOf())
    private var _listProduct = MutableStateFlow<MutableList<Product>>(mutableListOf())
    val listProduct: StateFlow<List<Product>> get() = _listProduct
    private val _searchText = MutableStateFlow("")
    var searchText = _searchText.asStateFlow()

    private  val _sortOption = MutableStateFlow("Tất cả")
    val sortOption = _sortOption.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> get() = _searchHistory


    fun loadListProduct(){
        _originListProduct.value = mutableListOf(
            Product("P000", "Burger thịt hun khói phô mai", "", "", 100000, true, "PC002", ""),
            Product("P001", "Burger thịt hun khói phô mai", "", "", 100000, true, "PC002", ""),
            Product("P002", "Burger thịt hun khói phô mai", "", "", 100000, true, "PC002", ""),
            Product("P003", "Pizza truyền thống", "", "", 100000, true, "PC001", ""),
            Product("P004", "Pizza truyền thống", "", "", 100000, true, "PC001", ""),
            Product("P005", "Pizza truyền thống", "", "", 100000, true, "PC001", ""),
            Product("P006", "Hotdog truyền thống", "", "", 100000, true, "PC003", ""),
            Product("P007", "Hotdog truyền thống", "", "", 100000, false, "PC003", ""),
            Product("P008", "Hotdog truyền thống", "", "", 100000, true, "PC003", ""),
            Product("P009", "Gà rán", "", "", 100000, true, "PC004", ""),
            Product("P010", "Gà rán", "", "", 100000, true, "PC004", ""),
            Product("P011", "Gà rán", "", "", 100000, true, "PC004", ""),
            Product("P012", "Bít tết áp chảo", "", "", 100000, true, "PC005", ""),
            Product("P013", "Bít tết áp chảo", "", "", 100000, true, "PC005", ""),
            Product("P014", "Bít tết áp chảo", "", "", 100000, true, "PC005", ""),
            Product("P015", "Nước chanh", "", "", 100000, true, "PC006", ""),
            Product("P016", "Nước chanh", "", "", 100000, true, "PC006", ""),
            Product("P017", "Nước chanh", "", "", 100000, true, "PC006", ""),
            Product("P018", "Khoai tây chiên", "", "", 100000, true, "PC007", ""),
            Product("P019", "Khoai tây chiên", "", "", 100000, true, "PC007", ""),
            Product("P020", "Khoai tây chiên", "", "", 100000, true, "PC007", "") )
        _listProduct.value = _originListProduct.value
    }

    fun setSearchText(query: String){
        _searchText.value = query
    }

    fun getSearchHistory(): List<String>{
        return _searchHistory.value
    }

    fun search() {
        val query = _searchText.value.lowercase()
        if (query.isNotBlank() && !_searchHistory.value.contains(query)) {
            val updatedHistory = _searchHistory.value.toMutableList().apply { add(query) }
            _searchHistory.value = updatedHistory.takeLast(10) // Giữ tối đa 10 lần tìm kiếm
        }

        _listProduct.value = _originListProduct.value.filter { it.name.lowercase().contains(query) }.toMutableList()
    }

    fun setSortOption(option: String){
        _sortOption.value = option
    }
    fun sortChange(){
        _listProduct.value = when (_sortOption.value) {
            SortOptions.NameIncr.value -> _listProduct.value.sortedBy { it.name }.toMutableList()
            SortOptions.NameDesc.value -> _listProduct.value.sortedByDescending { it.name }.toMutableList()
            SortOptions.PriceIncr.value -> _listProduct.value.sortedBy { it.originalPrice }.toMutableList()
            SortOptions.PriceDesc.value -> _listProduct.value.sortedByDescending { it.originalPrice }.toMutableList()
            SortOptions.All.value -> _listProduct.value.toMutableList()
            else -> _listProduct.value
        }
    }
     fun clear(){
         loadListProduct()
     }

}
enum class SortOptions(val value: String) {
    All("Tất cả"),
    NameIncr("Tên A-Z"),
    NameDesc("Tên Z-A"),
    PriceIncr("Giá thấp nhất"),
    PriceDesc("Giá đắt nhất"),
}