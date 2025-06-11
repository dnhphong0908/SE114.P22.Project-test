package com.example.mam.gui.screen.management

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPasteOff
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.yml.charts.common.extensions.isNotNull
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.dto.order.OrderDetailResponse
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.CustomDialog
import com.example.mam.gui.component.OrderItemContainer
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.gui.screen.client.OrderScreen
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.ManageOrderViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("SuspiciousIndentation")
@Composable
fun ManageOrderScreen(
    viewModel: ManageOrderViewModel,
    onBackClick: () -> Unit,
) {
    val order = viewModel.order.collectAsStateWithLifecycle().value
    val shipper = viewModel.shipper.collectAsStateWithLifecycle().value
    val user = viewModel.user.collectAsStateWithLifecycle().value
    val orderStatus = viewModel.orderStatus.collectAsStateWithLifecycle().value

    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val isStatusLoading = viewModel.isStatusLoading.collectAsStateWithLifecycle().value
    var isShowDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.loadOrderStatus()
        viewModel.loadData()
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
            if (order.orderStatus == "PENDING")
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Default.ContentPasteOff,
                    shadow = "outer",
                    onClick = {
                        scope.launch {
                            viewModel.cancelOrder()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 16.dp)
                )
        }
        if (isShowDialog) {
            CustomDialog(
                title = "Xác nhận",
                message = "Bạn có chắc chắn muốn cập nhật trạng thái đơn hàng này không?",
                onDismiss = { isShowDialog = false },
                onConfirm = {
                    scope.launch {
                        isShowDialog = false
                        if (viewModel.updateStatus() == 0) {
                            Toast.makeText(
                                context,
                                "Cập nhật trạng thái đơn hàng thất bại",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Toast.makeText(
                            context,
                            "Cập nhật trạng thái đơn hàng thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
                if (isStatusLoading) {
                    CircularProgressIndicator(
                        color = OrangeDefault,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                    )
                } else if (orderStatus == "PENDING" || orderStatus == "CONFIRMED" || orderStatus == "PROCESSING") {
                    OuterShadowFilledButton(
                        text = getStatusUpdateMessage(order.orderStatus),
                        onClick = { isShowDialog = true },
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
                                fontSize = 16.sp,
                                color = BrownDefault,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .fillMaxWidth()
                            )
                            Text(
                                text = shipper.fullname + " - " + shipper.phone,
                                fontSize = 14.sp,
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
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    ) {
                                        append("Biển số xe: ")
                                    }
                                    append(shipper.licensePlate)
                                },
                                fontSize = 14.sp,
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
                            fontSize = 16.sp,
                            color = BrownDefault,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = user.fullname + " - " + user.phone,
                            fontSize = 14.sp,
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
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Địa chỉ: ")
                                }
                                append(order.shippingAddress)
                            },
                            fontSize = 14.sp,
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
                            fontSize = 16.sp,
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
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Mã đơn hàng: ")
                                }
                                append(order.id.toString())
                            },
                            fontSize = 14.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        if (order.createdAt.isNotEmpty())
                        Instant.parse(order.createdAt).atZone(ZoneId.systemDefault()).let {
                            Text(
                                text = "Ngày đặt: " + it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                                textAlign = TextAlign.Start,
                                color = OrangeDefault,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                            )
                        }

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Hình thức thanh toán: ")
                                }
                                append(order.paymentMethod)
                            },
                            fontSize = 14.sp,
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
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Ghi chú: ")
                                }
                                append(order.note ?: "Không có ghi chú")
                            },
                            fontSize = 14.sp,
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
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Tổng tiền: ")
                                }
                                append(order.totalPrice.toString() + " VNĐ")
                            },
                            fontSize = 14.sp,
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
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Trạng thái đơn hàng: ")
                                }
                                append(order.orderStatus)
                            },
                            fontSize = 14.sp,
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
                        fontSize = 16.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                }
                items(order.orderDetails) { item ->
                   OrderItem(
                        item = item
                    )
                }
            }
        }
    }
}

@Composable
fun OrderItem(
    item: OrderDetailResponse,
    modifier: Modifier = Modifier,
){
    Surface(
        shadowElevation = 4.dp, // Elevation applied here instead
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = WhiteDefault
            ),
            modifier = Modifier
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
            ) {
                AsyncImage(
                    model = item.getRealUrl(), // Đây là URL từ API
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_mam_logo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Column (
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 8.dp, 8.dp, 8.dp)
                    ) {
                        Text(
                            text = item.productName + " *" + item.quantity,
                            textAlign = TextAlign.Start,
                            color = BrownDefault,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (item.variationInfo != null)
                            Text(
                                text = item.variationInfo,
                                textAlign = TextAlign.Start,
                                color = GreyDefault,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Text(
                            text = item.getPrice(),
                            textAlign = TextAlign.End,
                            color = OrangeDefault,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
fun getStatusUpdateMessage(status: String): String {
    return when (status) {
        "PENDING" -> "Xác nhận đơn hàng"
        "CONFIRMED" -> "Chế biến"
        "PROCESSING" -> "Giao hàng"
        "SHIPPING" -> "Đang giao hàng"
        "COMPLETED" -> "Đã giao hàng"
        else -> "Không xác định"
    }
}

//@Preview
//@Composable
//fun ManageOrderScreenPreview() {
//    ManageOrderScreen(
//        viewModel = ManageOrderViewModel(savedStateHandle = SavedStateHandle(mapOf("orderId" to "orderId"))),
//        onBackClick = {},
//        isPreview = true,
//    )
//}