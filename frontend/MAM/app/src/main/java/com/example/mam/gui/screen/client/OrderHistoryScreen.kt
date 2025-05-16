package com.example.mam.gui.screen.client

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mam.entity.Order
import com.example.mam.entity.OrderItem
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.NormalButtonWithIcon
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.OrderHistoryViewModel
import com.example.mam.viewmodel.client.OrderStatusFilter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import androidx.compose.runtime.getValue

@Composable
fun OrderHistoryScreen(
    viewModel: OrderHistoryViewModel? = null,
    mockOrder: List<Order>? = null,
    onBackClicked: () -> Unit = {}
    ){
    val localOrder = remember { mutableStateListOf<Order>() }

    if (mockOrder != null && localOrder.isEmpty()) {
        localOrder.addAll(mockOrder)
    }

    val isLoading = mockOrder == null &&
            (viewModel?.isLoading?.collectAsState()?.value ?: true)

    val scrollState = rememberScrollState()

    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    val filteredMockOrder = remember(selectedDate.value, localOrder) {
        localOrder.filter { order ->
            selectedDate.value?.let { date ->
                order.actualDeliveryTime
                    ?.atZone(ZoneId.systemDefault())
                    ?.toLocalDate() == date
            } ?: true
        }
    }

    val orders = if (mockOrder != null) filteredMockOrder else viewModel?.filteredOrder?.collectAsState()?.value

    LaunchedEffect(Unit) {
        if (mockOrder == null) {
            viewModel?.loadOrders()
        }
    }
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else{
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = OrangeDefault)
                .padding(WindowInsets.statusBars.asPaddingValues())
                .verticalScroll(scrollState),
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                // Icon nằm trái
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Filled.ArrowBack,
                    shadow = "outer",
                    onClick = onBackClicked,
                    modifier = Modifier
                        .focusable(false)
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp) // padding nếu cần
                )
                // Text nằm giữa
                Text(
                    text = "Lịch sử đơn hàng",
                    style = TextStyle(
                        fontSize = Variables.HeadlineMediumSize,
                        lineHeight = Variables.HeadlineMediumLineHeight,
                        fontWeight = FontWeight(700),
                        color = WhiteDefault,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .outerShadow(
                        color = GreyDark,
                        bordersRadius = 50.dp,
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
                    .height(300.dp)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 12.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Spacer(modifier = Modifier.width(12.dp))
                        FilterBar(
                            selectedDate = selectedDate,
                            onDateSelected = { selectedDate.value = it
                                viewModel?.setDateFilter(it) // optional nếu dùng viewmodel
                            }
                        )
                    }
                }
                items(orders.orEmpty()) { order ->
                    OrderHistoryItem(order)
                }
            }
        }
    }
}
@Composable
fun FilterBar(
    selectedDate: MutableState<LocalDate?>,
    onDateSelected: (LocalDate?) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        DatePickerButton(
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )
    }
}

@Composable
fun DatePickerButton(
    selectedDate: MutableState<LocalDate?>,
    onDateSelected: (LocalDate?) -> Unit
) {
    val context = LocalContext.current

    Button(
        onClick = {
            val calendar = Calendar.getInstance()

            DatePickerDialog(
                context,
                { _, year, month, day ->
                    val date = LocalDate.of(year, month + 1, day)
                    selectedDate.value = date
                    onDateSelected(date)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        colors = ButtonDefaults.buttonColors(OrangeDefault)
    ) {
        val text = selectedDate.value?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "Chọn ngày"
        Text(text, fontSize = 16.sp)
    }
}


@Composable
fun OrderHistoryItem(order: Order) {
    val formattedTime = order.actualDeliveryTime?.let { formatInstantToDisplay(it) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .outerShadow(
                color = GreyDark,
                bordersRadius = 21.dp,
                offsetX = 15.dp,
                offsetY = 15.dp,
            )
            .background(
                color = WhiteDefault,
                shape = RoundedCornerShape(21.dp)
            )
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = "Đơn hàng ${order.id}",
                color = BrownDefault,
                fontSize = Variables.BodySizeBig,
                fontWeight = FontWeight.Bold,
            )
            if (formattedTime != null) {
                Text(
                    text = formattedTime,
                    color = BrownDefault,
                    fontSize = Variables.BodySizeMedium,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic
                )
            }
            val orderStatusText = when (order.orderStatus) {
                0 -> "Đã giao"
                1 -> "Đang giao"
                2 -> "Đã hủy"
                else -> "Không xác định"
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$orderStatusText",
                    color = if (order.orderStatus == 1) GreyDefault else OrangeDefault,
                    fontSize = Variables.BodySizeMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = order.getTotalToString(),
                    color = BrownDefault,
                    fontSize = Variables.BodySizeMedium,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

fun parseToInstant(dateStr: String): Instant {
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
    val localDateTime = LocalDateTime.parse(dateStr, formatter)
    return localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant()
}
fun formatInstantToDisplay(instant: Instant): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")
    return instant.atZone(ZoneId.systemDefault()).format(formatter)
}
@Preview(showBackground = true)
@Composable
fun PreviewOrderHistoryScreen(){
    OrderHistoryScreen(
        mockOrder = listOf(
            Order(
                id = "Pizza001",
                orderStatus = 0,
                orderItems = listOf(OrderItem(price = 170000, quantity = 1)).toMutableList(),
                actualDeliveryTime = parseToInstant("12:00 15/04/2025")
            ),
            Order(
                id = "Pizza002",
                orderStatus = 0,
                orderItems = listOf(OrderItem(price = 200000, quantity = 1)).toMutableList(),
                actualDeliveryTime = parseToInstant("09:00 11/04/2025")
            )
        )
    )
}