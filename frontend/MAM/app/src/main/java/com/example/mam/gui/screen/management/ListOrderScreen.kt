package com.example.mam.gui.screen.management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.mam.entity.Order
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.ListOrderViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ListOrderScreen(
    viewModel: ListOrderViewModel,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onEditOrderClick: (String) -> Unit = {},
    mockData: List<Order>? = null,
) {
    val sortOptions = viewModel.sortingOptions.collectAsStateWithLifecycle().value
    val selectedSortingOption = viewModel.selectedSortingOption.collectAsStateWithLifecycle()
    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle()
    val orderList = viewModel.order.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val searchHistory = viewModel.searchHistory.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit){
        viewModel.loadSortingOptions()
        viewModel.loadData()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(OrangeDefault)
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Outlined.ArrowBack,
                    shadow = "outer",
                    onClick = onBackClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 16.dp, start = 16.dp)
                )
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Outlined.Home,
                    shadow = "outer",
                    onClick = onHomeClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 16.dp)
                )
                Text(
                    text = "Đơn hàng",
                    style = Typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 17.dp)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .outerShadow(
                        color = GreyDark,
                        bordersRadius = 50.dp,
                        blurRadius = 4.dp,
                        offsetX = 0.dp,
                        offsetY = -4.dp,
                    )
                    .background(
                        color = OrangeLighter,
                        shape = RoundedCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
            ) {
                item {
                    Spacer(modifier = Modifier.size(20.dp))
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                    ) {
                        val expanded = remember { mutableStateOf(false) }
                        val focusManager = LocalFocusManager.current
                        OutlinedTextField(
                            value = searchQuery.value,
                            onValueChange = {
                                viewModel.setSearch(it)
                                if (searchHistory.isNotEmpty()) {
                                    expanded.value = true // Chỉ mở nếu có lịch sử tìm kiếm
                                }
                            },
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
                            placeholder = {
                                Text(
                                    text = "Tìm kiếm đơn hàng",
                                    color = GreyDefault,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                )
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
                                        onClick = {
                                            viewModel.setSearch("")
                                        }) {
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
                                            viewModel.searchOrder()
                                            focusManager.clearFocus()
                                        }) {
                                        Icon(Icons.Default.Search, contentDescription = "Search")
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { if(it.isFocused && searchHistory.isNotEmpty()) expanded.value = true },
                        )
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false },
                            containerColor = WhiteDefault,
                            properties = PopupProperties(focusable = false),
                            modifier = Modifier
                                .zIndex(1f)
                                .fillMaxWidth()
                        ) {
                            searchHistory.forEach { query ->
                                DropdownMenuItem(
                                    text = { Text(query, color = BrownDefault) } ,
                                    leadingIcon = {
                                        Icon(Icons.Default.History, contentDescription = "History", tint = BrownDefault) },
                                    contentPadding = PaddingValues(3.dp),
                                    onClick = {
                                        viewModel.setSearch(query)
                                        expanded.value = false
                                    },
                                )
                            }
                        }
                    }
                }
                item {
                    Box(Modifier
                        .fillMaxWidth(0.9f)
                        .padding(start = 8.dp)) {
                        var sortExpanded by remember { mutableStateOf(false) }
                        FilterChip(
                            selected = sortExpanded,
                            onClick = { sortExpanded = !sortExpanded },
                            label = { selectedSortingOption.value },
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
                            modifier = Modifier
                        ) {
                            sortOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = BrownDefault) },
                                    onClick = {
                                        viewModel.setSelectedSortingOption(option)
                                        sortExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                if (mockData != null) {
                    items(mockData) { order ->
                        com.example.mam.gui.screen.management.OrderItem(
                            order = order,
                            onEditClick = onEditOrderClick,
                        )
                    }
                }
                else {
                    if (isLoading.value) {
                        item {
                            CircularProgressIndicator(
                                color = OrangeDefault,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(40.dp)
                            )
                        }
                    } else
                        if (orderList.isEmpty()) {
                            item {
                                Text(
                                    text = "Không có đơn hàng nào",
                                    color = GreyDefault,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        } else
                            items(orderList) { order ->
                                com.example.mam.gui.screen.management.OrderItem(
                                    order = order,
                                    onEditClick = onEditOrderClick,
                                )
                            }

                }


            }
        }
    }
}

@Composable
fun OrderItem(
    order: Order,
    isViewOnly: Boolean = false,
    onEditClick: (String) -> Unit,
) {
    val viewModel: ListOrderViewModel = ListOrderViewModel()
    val owner = viewModel.loadOwnerOfOrder(order.userId)
    Card(
        onClick = { },
        colors = CardDefaults.cardColors(
            containerColor = WhiteDefault
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            AsyncImage(
                model = owner.avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(GreyLight)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            ) {
                Text(
                    text = getStatusMessage(order.orderStatus),
                    textAlign = TextAlign.Start,
                    color = OrangeDefault,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = owner.fullName,
                    textAlign = TextAlign.Start,
                    color = BrownDefault,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                order.orderDate?.atZone(ZoneId.systemDefault())?.let {
                    Text(
                        text = it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                        textAlign = TextAlign.Start,
                        color = BrownDefault,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = order.getTotalToString(),
                    textAlign = TextAlign.End,
                    color = OrangeDefault,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (!isViewOnly) {
                if (order.orderStatus<4)IconButton(onClick = { onEditClick(order.id) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BrownDefault)
                }
//                IconButton(onClick = { onDeleteClick(order.id) }) {
//                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = BrownDefault)
//                }
            }
        }
    }
}



@Preview
@Composable
fun PreviewOrderItem() {
    val order = Order(
        id = "1",
        userId = "1",
        orderDate = java.time.Instant.now(),
        paymentId = "1",
        shippingAddress = "123 Street",
        orderItems = mutableListOf(),
        totalPrice = 100000,
        note = "Note",
        orderStatus = 1,
        expectDeliveryTime = java.time.Instant.now(),
        actualDeliveryTime = java.time.Instant.now(),
        shipperId = "1"
    )
    OrderItem(
        order = order,
        onEditClick = {},
    )
}

@Preview
@Composable
fun PreviewListOrderScreen() {
    ListOrderScreen(
        viewModel = ListOrderViewModel(),
        onBackClick = {},
        onEditOrderClick = {},
        mockData = listOf(
            Order(
                id = "1",
                userId = "1",
                orderDate = java.time.Instant.now(),
                paymentId = "1",
                shippingAddress = "123 Street",
                orderItems = mutableListOf(),
                totalPrice = 100000,
                note = "Note",
                orderStatus = 4,
                expectDeliveryTime = java.time.Instant.now(),
                actualDeliveryTime = java.time.Instant.now(),
                shipperId = "1"
            ),
            Order(
                id = "1",
                userId = "1",
                orderDate = java.time.Instant.now(),
                paymentId = "1",
                shippingAddress = "123 Street",
                orderItems = mutableListOf(),
                totalPrice = 100000,
                note = "Note",
                orderStatus = 1,
                expectDeliveryTime = java.time.Instant.now(),
                actualDeliveryTime = java.time.Instant.now(),
                shipperId = "1"
            ),
            Order(
                id = "1",
                userId = "1",
                orderDate = java.time.Instant.now(),
                paymentId = "1",
                shippingAddress = "123 Street",
                orderItems = mutableListOf(),
                totalPrice = 100000,
                note = "Note",
                orderStatus = 1,
                expectDeliveryTime = java.time.Instant.now(),
                actualDeliveryTime = java.time.Instant.now(),
                shipperId = "1"
            )
        )
    )
}
