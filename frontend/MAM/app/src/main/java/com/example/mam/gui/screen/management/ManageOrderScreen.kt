package com.example.mam.gui.screen.management

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.CustomDialog
import com.example.mam.gui.component.OrderItemContainer
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.ManageOrderViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ManageOrderScreen(
    viewModel: ManageOrderViewModel,
    onBackClick: () -> Unit,
    isPreview: Boolean = false,
) {
    val order = viewModel.order.collectAsStateWithLifecycle().value
    val shipper = viewModel.shipper.collectAsStateWithLifecycle().value
    val user = viewModel.user.collectAsStateWithLifecycle().value
    val orderStatus = viewModel.orderStatus.collectAsStateWithLifecycle().value

    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val isStatusLoading = viewModel.isStatusLoading.collectAsStateWithLifecycle().value
    var isShowDialog by remember { mutableStateOf(false)}
    var context = LocalContext.current
    LaunchedEffect(key1 = order) {
        if (isPreview) {
            viewModel.mockData()
        }
        else {
            viewModel.loadData()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeDefault)
            .padding(WindowInsets.statusBars.asPaddingValues())
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
            Text(
                text = "Chi tiết đơn hàng",
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
        }
        if(isShowDialog) {
            CustomDialog(
                title = "Xác nhận",
                message = "Bạn có chắc chắn muốn cập nhật trạng thái đơn hàng này không?",
                onDismiss = { isShowDialog = false },
                onConfirm = {
                    viewModel.setStatus()
                    isShowDialog = false
                    viewModel.updateStatus()
                    Toast.makeText(
                        context,
                        "Cập nhật trạng thái đơn hàng thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                if (isStatusLoading)
                {
                    CircularProgressIndicator(
                        color = OrangeDefault,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                    )
                }
                else if (orderStatus <= 3) {
                    OuterShadowFilledButton(
                        text = getStatusUpdateMessage(order.orderStatus),
                        onClick = {isShowDialog = true},
                        textColor = WhiteDefault,
                        color = OrangeDefault,
                        shadowColor = GreyDark,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(5.dp)
                    )
                }
            }
            if (isLoading) {
                item {
                    CircularProgressIndicator(
                        color = OrangeDefault,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                    )
                }
            } else {
            if (shipper != null) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(
                                OrangeLight,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(5.dp)
                    ) {
                        Text(
                            text = "Người giao hàng",
                            fontSize = 20.sp,
                            color = BrownDefault,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = shipper.name + " - " + shipper.phoneNumber,
                            fontSize = 18.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = BrownDefault,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Biển số xe: ")
                                }
                                append(shipper.licensePlate)
                            },
                            fontSize = 18.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(
                            OrangeLight,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(5.dp)
                ) {
                    Text(
                        text = "Người đặt hàng",
                        fontSize = 20.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = user.fullName + " - " + user.phoneNumber,
                        fontSize = 18.sp,
                        color = BrownDefault,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = BrownDefault,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Địa chỉ: ")
                            }
                            append(user.address)
                        },
                        fontSize = 18.sp,
                        color = BrownDefault,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(
                            OrangeLight,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(5.dp)
                ) {
                    Text(
                        text = "Thông tin đơn hàng",
                        fontSize = 20.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = BrownDefault,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Mã đơn hàng: ")
                            }
                            append(order.id)
                        },
                        fontSize = 18.sp,
                        color = BrownDefault,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                    order.orderDate?.atZone(ZoneId.systemDefault()).let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = BrownDefault,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Ngày đặt hàng: ")
                                }
                                append(it?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                            },
                            fontSize = 18.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                    }

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = BrownDefault,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Hình thức thanh toán: ")
                            }
                            append(order.paymentId)
                        },
                        fontSize = 18.sp,
                        color = BrownDefault,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = BrownDefault,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Ghi chú: ")
                            }
                            append(order.note)
                        },
                        fontSize = 18.sp,
                        color = BrownDefault,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = BrownDefault,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Tổng tiền: ")
                            }
                            append(order.totalPrice.toString() + " VNĐ")
                        },
                        fontSize = 18.sp,
                        color = BrownDefault,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = BrownDefault,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Trạng thái đơn hàng: ")
                            }
                            append(getStatusMessage(order.orderStatus))
                        },
                        fontSize = 18.sp,
                        color = OrangeDefault,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                }
            }
                item {
                    Text(
                        text = "Danh sách sản phẩm",
                        fontSize = 20.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                }
            items(order.orderItems){ item ->
                OrderItemContainer(
                    item= item
                )
            }
        }
    }
    }
}

fun getStatusMessage(status: Int): String {
    return when (status) {
        0 -> "Chờ xác nhận"
        1 -> "Đã xác nhận"
        2 -> "Đang chế biến"
        3 -> "Đang giao hàng"
        4 -> "Đã giao hàng"
        else -> "Không xác định"
    }
}

fun getStatusUpdateMessage(status: Int): String {
    return when (status) {
        0 -> "Xác nhận đơn hàng"
        1 -> "Đang chế biến"
        2 -> "Đang giao hàng"
        3 -> "Đã giao hàng"
        else -> "Không xác định"
    }
}

@Preview
@Composable
fun ManageOrderScreenPreview() {
    ManageOrderScreen(
        viewModel = ManageOrderViewModel(savedStateHandle = SavedStateHandle(mapOf("orderId" to "orderId"))),
        onBackClick = {},
        isPreview = true,
    )
}