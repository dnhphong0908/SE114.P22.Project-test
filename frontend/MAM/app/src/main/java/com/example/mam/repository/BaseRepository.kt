package com.example.mam.repository

import com.example.mam.data.UserPreferencesRepository

//BaseService để khởi tạo Retrofit client và gọi các service
class BaseRepository(userPreferencesRepository: UserPreferencesRepository) {
    private val privateRetrofit = RetrofitClient.createPrivateRetrofit(userPreferencesRepository)

    private val publicRetrofit = RetrofitClient.createPublicRetrofit()
    // Các service sẽ được khởi tạo ở đây, sử dụng lazy để chỉ khởi tạo khi cần thiết
    val authPublicRepository: AuthPublicRepository by lazy {
        publicRetrofit.create(AuthPublicRepository::class.java) }

    val authPrivateRepository: AuthPrivateRepository by lazy {
        privateRetrofit.create(AuthPrivateRepository::class.java) }

    val productCategoryRepository: ProductCategoryRepository by lazy {
        privateRetrofit.create(ProductCategoryRepository::class.java)
    }

    val productRepository: ProductRepository by lazy {
        privateRetrofit.create(ProductRepository::class.java)
    }

    val variationRepository: VariationRepository by lazy {
        privateRetrofit.create(VariationRepository::class.java)
    }

    val variationOptionRepository: VariationOptionRepository by lazy {
        privateRetrofit.create(VariationOptionRepository::class.java)
    }

    val cartRepository: CartRepository by lazy {
        privateRetrofit.create(CartRepository::class.java)
    }

    val cartItemRepository: CartItemRepository by lazy {
        privateRetrofit.create(CartItemRepository::class.java)
    }

    val shipperRepository: ShipperRepository by lazy {
        privateRetrofit.create(ShipperRepository::class.java)
    }
    val userRepository: UserRepository by lazy {
        privateRetrofit.create(UserRepository::class.java)
    }
    val notificationRepository: NotificationRepository by lazy {
        privateRetrofit.create(NotificationRepository::class.java)
    }
    val orderRepository: OrderRepository by lazy {
        privateRetrofit.create(OrderRepository::class.java)
    }
    val promotionRepository: PromotionRepository by lazy {
        privateRetrofit.create(PromotionRepository::class.java)
    }
    val userPromotionRepository: UserPromotionRepository by lazy {
        privateRetrofit.create(UserPromotionRepository::class.java)
    }
    val stastiticRepository: StastiticRepository by lazy {
        privateRetrofit.create(StastiticRepository::class.java)
    }
}