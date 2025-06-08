package com.example.mam.services

import androidx.navigation.NavController
import com.example.mam.data.UserPreferencesRepository
import com.mapbox.common.MapboxOptions.accessToken

//BaseService để khởi tạo Retrofit client và gọi các service
class BaseService(userPreferencesRepository: UserPreferencesRepository) {
    private val privateRetrofit = RetrofitClient.createPrivateRetrofit(userPreferencesRepository)

    private val publicRetrofit = RetrofitClient.createPublicRetrofit()
    // Các service sẽ được khởi tạo ở đây, sử dụng lazy để chỉ khởi tạo khi cần thiết
    val authPublicService: AuthPublicService by lazy {
        publicRetrofit.create(AuthPublicService::class.java) }

    val authPrivateService: AuthPrivateService by lazy {
        privateRetrofit.create(AuthPrivateService::class.java) }

    val productCategoryService: ProductCategoryService by lazy {
        privateRetrofit.create(ProductCategoryService::class.java)
    }

    val productService: ProductService by lazy {
        privateRetrofit.create(ProductService::class.java)
    }

    val variationService: VariationService by lazy {
        privateRetrofit.create(VariationService::class.java)
    }

    val variationOptionService: VariationOptionService by lazy {
        privateRetrofit.create(VariationOptionService::class.java)
    }

    val cartService: CartService by lazy {
        privateRetrofit.create(CartService::class.java)
    }

    val cartItemService: CartItemService by lazy {
        privateRetrofit.create(CartItemService::class.java)
    }

    val shipperService: ShipperService by lazy {
        privateRetrofit.create(ShipperService::class.java)
    }
    val userService: UserService by lazy {
        privateRetrofit.create(UserService::class.java)
    }
    val notificationService: NotificationService by lazy {
        privateRetrofit.create(NotificationService::class.java)
    }
    val orderService: OrderService by lazy {
        privateRetrofit.create(OrderService::class.java)
    }
    val promotionService: PromotionService by lazy {
        privateRetrofit.create(PromotionService::class.java)
    }
    val userPromotionService: UserPromotionService by lazy {
        privateRetrofit.create(UserPromotionService::class.java)
    }
}