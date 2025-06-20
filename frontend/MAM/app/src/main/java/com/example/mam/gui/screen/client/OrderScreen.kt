package com.example.mam.gui.screen.client

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentPasteOff
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HowToReg
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.dto.cart.CartItemResponse
import com.example.mam.dto.order.OrderDetailResponse
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.OrderItemContainer
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.QuantitySelectionButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.gui.screen.management.OrderItem
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyAvaDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.CheckOutViewModel
import com.example.mam.viewmodel.client.OrderViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun OrderScreen(
    onBackClicked: () -> Unit = {},
    viewModel: OrderViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val order = viewModel.order.collectAsStateWithLifecycle().value
    val shipper = viewModel.shipper.collectAsStateWithLifecycle().value
    val user = viewModel.user.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.loadOrder()
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = OrangeDefault)
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
                icon = Icons.Default.ArrowBack,
                shadow = "outer",
                onClick = onBackClicked,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 16.dp, start = 16.dp)
            )
            Text(
                text = "Đơn hàng của bạn",
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                Modifier.fillMaxWidth(0.9f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .background(OrangeLight, RoundedCornerShape(50))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.Center)
                            .zIndex(1f)
                    ) {
                        CircleIconButton(
                            backgroundColor = when (order.orderStatus) {
                                "CONFIRMED", "PROCESSING", "SHIPPING", "COMPLETED" -> OrangeDefault
                                "CANCELED" -> GreyDefault
                                else -> WhiteDefault
                            },
                            foregroundColor = when (order.orderStatus) {
                                "CONFIRMED", "PROCESSING", "SHIPPING", "COMPLETED", "CANCELED" -> WhiteDefault
                                else -> OrangeDefault
                            },
                            icon = if (order.orderStatus == "CANCELED") Icons.Default.ContentPasteOff else Icons.Outlined.Inventory,
                            shadow = "outer",
                            onClick = onBackClicked,
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                        )
                        CircleIconButton(
                            backgroundColor = when (order.orderStatus) {
                                "PROCESSING", "SHIPPING", "COMPLETED" -> OrangeDefault
                                else -> WhiteDefault
                            },
                            foregroundColor = when (order.orderStatus) {
                                "PROCESSING", "SHIPPING", "COMPLETED" -> WhiteDefault
                                else -> OrangeDefault
                            },
                            icon = Icons.Outlined.LocalFireDepartment,
                            shadow = "outer",
                            onClick = onBackClicked,
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                        )
                        CircleIconButton(
                            backgroundColor = when (order.orderStatus) {
                                "SHIPPING", "COMPLETED" -> OrangeDefault
                                else -> WhiteDefault
                            },
                            foregroundColor = when (order.orderStatus) {
                                "SHIPPING", "COMPLETED" -> WhiteDefault
                                else -> OrangeDefault
                            },
                            icon = Icons.Outlined.LocalShipping,
                            shadow = "outer",
                            onClick = onBackClicked,
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                        )
                        CircleIconButton(
                            backgroundColor = when (order.orderStatus) {
                                "COMPLETED" -> OrangeDefault
                                else -> WhiteDefault
                            },
                            foregroundColor = when (order.orderStatus) {
                                "COMPLETED" -> WhiteDefault
                                else -> OrangeDefault
                            },
                            icon = Icons.Outlined.HowToReg,
                            shadow = "outer",
                            onClick = onBackClicked,
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(10.dp)
                            .background(GreyLight, RoundedCornerShape(50))
                            .align(Alignment.Center)
                    ) {

                    }
                }
                Text(
                    text = when (order.orderStatus) {
                        "PENDING" -> "Đơn hàng chờ được xác nhận"
                        "CONFIRMED" -> "Đơn hàng đã được tiếp nhận"
                        "PROCESSING" -> "Đơn hàng đang được chế biến"
                        "SHIPPING" -> "Đơn hàng đang được giao tới bạn.\n Thời gian giao hàng dự kiến: " +
                                Instant.parse(order.expectedDeliveryTime)
                                    .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
                        "COMPLETED" -> "Đơn hàng đã được giao tới bạn.\n Thời gian giao hàng: " +
                                Instant.parse(order.actualDeliveryTime)
                                    .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
                        "CANCELED" -> "Đơn hàng đã bị hủy"
                        else -> "Đơn hàng không xác định"
                    },
                    fontSize = 16.sp,
                    color = WhiteDefault,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                )
            }
            if (shipper != null) {
                Column(
                    Modifier.fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "Người giao hàng:",
                        fontSize = 16.sp,
                        color = WhiteDefault,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                    )
                    Text(
                        text = shipper.fullname + " - " + shipper.phone,
                        fontSize = 14.sp,
                        color = WhiteDefault,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                    )
                    Text(
                        text = "Biển số xe: " + shipper.licensePlate,
                        fontSize = 14.sp,
                        color = WhiteDefault,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                    )
                }
            }
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
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
        )
        {
            item {
                Spacer(Modifier.height(20.dp))
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
                        text = user.fullname + " " + user.phone,
                        fontSize = 16.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = "Địa chỉ giao hàng: " + order.shippingAddress,
                        fontSize = 14.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = "Phương thức thanh toán: ",
                        fontSize = 14.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Text(
                        text = "Ghi chú: \n" + if (order.note.isNullOrEmpty()) {
                            "Không có ghi chú"
                        } else {
                            order.note
                        },
                        fontSize = 14.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)

                    )
                }
            }
            item {
                Box(
                    Modifier
                        .outerShadow()
                        .padding(bottom = 5.dp)
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .background(OrangeLight, shape = RoundedCornerShape(50))

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        modifier = Modifier.padding(10.dp).fillMaxWidth()

                    ) {
                        Column {
                            Text(
                                text = "Tổng cộng",
                                fontSize = 16.sp,
                                color = BrownDefault,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = order.getPriceToString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangeDefault,
                            )
                        }
                        if (order.orderStatus == "SHIPPING") {
                            OuterShadowFilledButton(
                                text = "Xác nhận nhận hàng",
                                icon = Icons.Outlined.CheckCircle,
                                onClick = {
                                    scope.launch {
                                        if (viewModel.maskAsDeliveried() == 1) {
                                            Toast.makeText(
                                                context,
                                                "Đã xác nhận đã nhận hàng. Chúc bạn ngon miệng!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Không thể xác nhận đã nhận hàng",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .wrapContentSize()
                            )
                        } else if (order.orderStatus == "PENDING") {
                            OuterShadowFilledButton(
                                text = "Hủy đơn hàng",
                                icon = Icons.Default.Delete,
                                onClick = {
                                    scope.launch {
                                        if (viewModel.cancelOrder() == 1) {
                                            Toast.makeText(
                                                context,
                                                "Đã hủy đơn hàng thành công",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Không thể hủy đơn hàng này",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .wrapContentSize()
                            )
                        }
                    }
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
            items(order.orderDetails) { item ->
                OrderItem(
                    item = item
                )
            }
        }
    }
}

    @Composable
    fun OrderItem(
        item: OrderDetailResponse,
        modifier: Modifier = Modifier,
    ) {
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
                        Column(
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
