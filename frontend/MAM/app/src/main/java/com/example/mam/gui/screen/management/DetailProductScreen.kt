package com.example.mam.gui.screen.management

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
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
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.entity.Product
import com.example.mam.entity.ProductCategory
import com.example.mam.entity.Variance
import com.example.mam.entity.VarianceOption
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreenDefault
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.viewmodel.management.DetailProductViewModel
import com.mapbox.maps.extension.style.expressions.dsl.generated.product
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DetailProductScreen(
    viewModel: DetailProductViewModel,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isPreview: Boolean = false,
    isAdd: Boolean = false,
    isEdit: Boolean = false,
) {
    val scrollState = rememberScrollState()
    val coroutine = rememberCoroutineScope()
    val lazyState = rememberLazyListState()
    var spacerHeight by remember { mutableIntStateOf(170) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (spacerHeight != 0) coroutine.launch {
                    lazyState.dispatchRawDelta(0f)
                }
                if (lazyState.firstVisibleItemIndex == 0) {
                    val delta = available.y.toInt()
                    val newSpacerHeight = spacerHeight + delta
                    val oldSpacerHeight = spacerHeight
                    spacerHeight = newSpacerHeight.coerceIn(0, 170)
                    return Offset(0f, (oldSpacerHeight - spacerHeight).toFloat())
                }
                return Offset(0f, 0f)
            }
        }
    }

    val isLoading = viewModel.isLoading.collectAsState()
    val product = if(!isPreview) viewModel.product.collectAsState().value else Product(
        id = "B001",
        name = "Burger Phô Mai Truyền Thống",
        shortDescription = "Bánh hamburger bò với phô mai tan chảy",
        longDescription = "Classic Cheeseburger với bò nướng mọng nước, phô mai Cheddar tan chảy, rau diếp tươi, cà chua, dưa chuột muối, và nước sốt đặc biệt. Tất cả kẹp giữa hai lát bánh mì mềm thơm ngon.",
        originalPrice = 89000,
        isAvailable = true,
        idCategory = "C002",
        img = R.drawable.bacon_and_cheese_heaven, // Giả sử có hình ảnh trong drawable
        createAt = Instant.now(),
        updateAt = Instant.now()
    )
    val category = if(!isPreview) viewModel.category.collectAsState().value else ProductCategory(
        id = "C002",
        name = "Burgers",
        description = "Nhóm các sản phẩm burger, từ classic cheeseburger đến các phiên bản đặc biệt.",
        icon = R.drawable.ic_hamburger, // Giả sử có biểu tượng burger trong tài nguyên drawable
        createAt = Instant.now(),
        updateAt = Instant.now()
    )
    val variants = if(!isPreview) viewModel.variants.collectAsState().value else mapOf(
        Variance(id = "V001", name = "Kích thước", idProduct = "B001") to listOf(
            VarianceOption(id = "VO001", idVariance = "V001", value = "Nhỏ", additionalPrice = 0),
            VarianceOption(id = "VO002", idVariance = "V001", value = "Vừa", additionalPrice = 10000),
            VarianceOption(id = "VO003", idVariance = "V001", value = "Lớn", additionalPrice = 20000)
        ),
        Variance(id = "V002", name = "Phô mai", idProduct = "B001") to listOf(
            VarianceOption(id = "VO004", idVariance = "V002", value = "Không có", additionalPrice = 0),
            VarianceOption(id = "VO005", idVariance = "V002", value = "Cheddar", additionalPrice = 5000),
            VarianceOption(id = "VO006", idVariance = "V002", value = "Mozzarella", additionalPrice = 7000)
        )
    )

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Box(Modifier.fillMaxSize().background(OrangeDefault).padding(WindowInsets.statusBars.asPaddingValues())) {


        AsyncImage(
            model = "",
            placeholder = painterResource(R.drawable.ic_mam_logo),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)

        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .scrollable(scrollState, Orientation.Vertical)
                .nestedScroll(nestedScrollConnection)
        ) {
            Row(
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Filled.ArrowBack,
                    shadow = "outer",
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                )
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Default.Edit,
                    shadow = "outer",
                    onClick = onEditClick,
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp)
                )
            }
            Spacer(Modifier.height(spacerHeight.dp))
            LazyColumn(
                state = lazyState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .background(
                        OrangeLighter, RoundedCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
            ) {
                if (isLoading.value) {
                    item {
                        Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = OrangeDefault,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(40.dp))
                    }
                    }
                } else if(product == null || category == null || variants.isEmpty()) {
                    item {
                        Text(
                            text = "Không tìm thấy sản phẩm",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrownDefault,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )
                    }
                }
                else {
                    item {
                        Spacer(Modifier.height(25.dp))
                        Text(
                            text = "ID: " + product.id,
                            textAlign = TextAlign.Start,
                            color = GreyDefault,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = product.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrownDefault,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Giá gốc: ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = OrangeDefault,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(product.getPriceToString())
                                }
                            },
                            fontSize = 18.sp,
                            color = BrownDefault,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Danh mục: ")
                                }
                                append(category.name)
                            },
                            fontSize = 18.sp,
                            color = BrownDefault,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Trạng thái: ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = if (product.isAvailable) GreenDefault else ErrorColor,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(if (product.isAvailable) "Còn hàng" else "Hết hàng")
                                }
                            },
                            fontSize = 18.sp,
                            color = BrownDefault,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Mô tả ngắn: ")
                                }
                                append(product.shortDescription)
                            },
                            fontSize = 18.sp,
                            color = BrownDefault,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Mô tả đầy đủ: ")
                                }
                                append(product.longDescription)
                            },
                            fontSize = 18.sp,
                            color = BrownDefault,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        product.createAt.atZone(ZoneId.systemDefault()).let {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Ngày tạo: ")
                                    }
                                    append(it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                                },
                                fontSize = 18.sp,
                                color = GreyDefault,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            )
                        }
                        product.updateAt.atZone(ZoneId.systemDefault()).let {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Ngày cập nhật: ")
                                    }
                                    append(it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                                },
                                fontSize = 18.sp,
                                color = GreyDefault,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            )
                        }
                    }
                    if(variants.isNotEmpty()){
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .background(
                                        color = OrangeLight,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Danh sách tùy chọn",
                                    color = BrownDefault,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                )
                                variants.forEach { (variance, options) ->
                                    HorizontalDivider(
                                        color = BrownDefault,
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    )
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(variance.name + ": ")
                                            }
                                            options.forEach() {
                                                append("\n" + it.value)
                                            }
                                        },
                                        fontSize = 18.sp,
                                        color = BrownDefault,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDetailProductScreen() {
val viewModel = DetailProductViewModel(savedStateHandle = SavedStateHandle(mapOf("productId" to "B001")))
    DetailProductScreen(
        viewModel = viewModel,
        onBackClick = {},
        onEditClick = {},
        onDeleteClick = {},
        isPreview = true
    )
}