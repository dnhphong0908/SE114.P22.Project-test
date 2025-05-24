package com.example.mam.viewmodel.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _processingOrderNum = MutableStateFlow(0)
    val processingOrderNum: StateFlow<Int> = _processingOrderNum

    private val _notProcessingOrderNum = MutableStateFlow(0)
    val notProcessingOrderNum: StateFlow<Int> = _notProcessingOrderNum

    private val _revenueList = MutableStateFlow(listOf<Point>())
    val revenueList = _revenueList.asStateFlow()

    private val _revenueQuarterList = MutableStateFlow(listOf<Point>())
    val revenueQuarterList = _revenueQuarterList.asStateFlow()

    private val _isLoadingRevenue = MutableStateFlow(false)
    val isLoadingRevenue: StateFlow<Boolean> = _isLoadingRevenue

    private val _categorySoldList = MutableStateFlow(listOf<Pair<String,Float>>())
    val categorySoldList = _categorySoldList.asStateFlow()

    private val _categorySoldQuarterList = MutableStateFlow(listOf<Pair<String,Float>>())
    val categorySoldQuarterList = _categorySoldQuarterList.asStateFlow()

    private val _isLoadingCategory = MutableStateFlow(false)
    val isLoadingCategory: StateFlow<Boolean> = _isLoadingCategory

    fun loadData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Simulate network call
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRevenueMonthlyList(year: Int) {
        viewModelScope.launch {
            try {
                _isLoadingRevenue.value = true
                // Simulate network call
                _revenueList.value = listOf(
                    Point(1f, 100f),
                    Point(2f, 200f),
                    Point(3f, 300f),
                    Point(4f, 400f),
                    Point(5f, 500f),
                    Point(6f, 600f),
                    Point(7f, 700f),
                    Point(8f, 800f),
                    Point(9f, 900f),
                    Point(10f, 1000f),
                    Point(11f, 1100f),
                    Point(12f, 1200f)
                )
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoadingRevenue.value = false
            }
        }
    }

    fun loadRevenueQuarterList(year: Int) {
        viewModelScope.launch {
            try {
                _isLoadingRevenue.value = true
                // Simulate network call
                _revenueList.value =
                    listOf(
                        Point(1f, 1000f),
                        Point(2f, 2000f),
                        Point(3f, 3000f),
                        Point(4f, 4000f)
                    )
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoadingRevenue.value = false
            }
        }
    }

    fun loadCategorySoldMonthlyList(month: Int, year: Int) {
        viewModelScope.launch {
            try {
                _isLoadingCategory.value = true
                // Simulate network call
                _categorySoldList.value =
                    listOf("Category1" to 100f, "Category2" to 200f, "Category3" to 300f)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoadingCategory.value = false
            }
        }
    }

    fun loadCategorySoldQuarterList(quarter: Int, year: Int) {
        viewModelScope.launch {
            try {
                _isLoadingCategory.value = true
                // Simulate network call
                _categorySoldList.value =
                    listOf("Category1" to 1000f, "Category2" to 2000f, "Category3" to 3000f)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoadingCategory.value = false
            }
        }
    }
}