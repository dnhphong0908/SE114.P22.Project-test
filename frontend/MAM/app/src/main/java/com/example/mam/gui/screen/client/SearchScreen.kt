package com.example.mam.gui.screen.client

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.dto.product.ProductResponse
import com.example.mam.gui.component.ProductClientListItem
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.SearchViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClicked: () -> Unit = {},
    onItemClicked: (ProductResponse) -> Unit = { ProductResponse -> },
    viewModel: SearchViewModel = viewModel()
){
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val listProduct = viewModel.products.collectAsStateWithLifecycle().value
    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle().value
    val searchHistory = viewModel.searchHistory.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }
    val sortOption = viewModel.sortingOptions.collectAsStateWithLifecycle().value
    val selectedSortOption = viewModel.selectedSortingOption.collectAsStateWithLifecycle().value
    val asc = viewModel.asc.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    val focusManager = LocalFocusManager.current
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeLighter)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        item {
            Box{
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    viewModel.setSearch(it)
                    if (searchHistory.isNotEmpty()) {
                        expanded = true // Chỉ mở nếu có lịch sử tìm kiếm
                    }},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = WhiteDefault,  // Màu nền khi focus
                    unfocusedContainerColor = WhiteDefault, // Màu nền khi không focus
                    focusedIndicatorColor = WhiteDefault,  // Màu viền khi focus
                    unfocusedIndicatorColor = WhiteDefault,  // Màu viền khi không focus
                    focusedTextColor = BrownDefault,       // Màu chữ khi focus
                    unfocusedTextColor = BrownDefault,      // Màu chữ khi không focus
                    cursorColor = BrownDefault             // Màu con trỏ nhập liệu
                ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        colors = IconButtonColors(
                            containerColor = GreyLight,
                            contentColor = BrownDefault,
                            disabledContentColor = BrownDefault,
                            disabledContainerColor = WhiteDefault
                        ),
                        onClick = onBackClicked ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                trailingIcon = {
                    Row {
                        IconButton(
                            colors = IconButtonColors(
                                containerColor = WhiteDefault,
                                contentColor = BrownDefault,
                                disabledContentColor = BrownDefault,
                                disabledContainerColor = WhiteDefault
                            ),
                            onClick = { viewModel.setSearch("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                        IconButton(
                            colors = IconButtonColors(
                                containerColor = GreyLight,
                                contentColor = BrownDefault,
                                disabledContentColor = BrownDefault,
                                disabledContainerColor = WhiteDefault
                            ),
                            onClick = {
                                focusManager.clearFocus()
                                scope.launch {
                                    viewModel.searchProduct()
                                    expanded = false // Đóng menu khi tìm kiếm
                                }
                            }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done),

                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { if(it.isFocused && searchHistory.isNotEmpty()) expanded = true },
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = WhiteDefault,
                properties = PopupProperties(focusable = false),
                modifier = Modifier
                    .zIndex(1f)
                    .fillParentMaxWidth()
            ) {
                searchHistory.forEach() { query ->
                    DropdownMenuItem(
                        text = { Text(query, color = BrownDefault) } ,
                        leadingIcon = {
                            Icon(Icons.Default.History, contentDescription = "History", tint = BrownDefault) },
                        contentPadding = PaddingValues(3.dp),
                        onClick = {
                            viewModel.setSearch(query)
                            expanded = false
                        },
                    )
                }
            }
                }
            HorizontalDivider(
                modifier = Modifier,
                color = BrownDefault
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(start = 8.dp)) {
                Box(
                    Modifier
                        .padding(start = 8.dp)
                ) {
                    FilterChip(
                        selected = sortExpanded,
                        onClick = { sortExpanded = !sortExpanded },
                        label = { Text(selectedSortOption) },
                        leadingIcon = {
                            Icon(Icons.Default.Sort, contentDescription = "Sort")
                        },
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand")
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = WhiteDefault,
                            labelColor = BrownDefault,
                            iconColor = BrownDefault,
                            selectedContainerColor = OrangeDefault,
                            selectedLabelColor = WhiteDefault,
                            selectedLeadingIconColor = WhiteDefault,
                            selectedTrailingIconColor = WhiteDefault
                        ),
                        modifier = Modifier
                    )

                    DropdownMenu(
                        expanded = sortExpanded,
                        onDismissRequest = { sortExpanded = false },
                        containerColor = WhiteDefault,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        sortOption.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = BrownDefault) },
                                onClick = {
                                    scope.launch {
                                        viewModel.setSelectedSortingOption(option)
                                        viewModel.sortProduct()
                                        sortExpanded = false
                                    }
                                }
                            )
                        }
                    }
                }
                IconButton(
                    colors = IconButtonColors(
                        containerColor = WhiteDefault,
                        contentColor = BrownDefault,
                        disabledContentColor = BrownDefault,
                        disabledContainerColor = WhiteDefault
                    ),
                    onClick = {
                        scope.launch {
                            viewModel.setASC()
                            viewModel.sortProduct()
                        }
                    },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        if (asc) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                        contentDescription = "ASC/DESC"
                    )
                }
            }
        }
        if (isLoading){
            item{
                CircularProgressIndicator(
                    color = OrangeDefault,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                )
            }
        }
        else items(listProduct){product ->
            ProductClientListItem(
                item = product,
                onClick = {
                    onItemClicked(it)
                    Log.d("ProductContainer", "Clicked on: ${product.name}")
                    },
                color = WhiteDefault,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            )
        }
    }
}

@Preview
@Composable
fun SearchPreview(){
    SearchScreen()
}