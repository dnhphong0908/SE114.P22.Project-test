package com.example.mam.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mam.gui.screen.authentication.ForgetPasswordScreen
import com.example.mam.gui.screen.authentication.OTPScreen
import com.example.mam.gui.screen.authentication.SignInScreen
import com.example.mam.gui.screen.authentication.SignUpScreen
import com.example.mam.gui.screen.authentication.StartScreen
import com.example.mam.gui.screen.client.CartScreen
import com.example.mam.gui.screen.client.CheckOutScreen
import com.example.mam.gui.screen.client.HomeScreen
import com.example.mam.gui.screen.client.ItemScreen
import com.example.mam.gui.screen.client.MapScreen
import com.example.mam.gui.screen.client.OrderScreen
import com.example.mam.gui.screen.client.ProfileScreen
import com.example.mam.gui.screen.client.SearchScreen
import com.example.mam.gui.screen.management.DashboardScreen
import com.example.mam.gui.screen.management.ListCategoryScreen
import com.example.mam.gui.screen.management.ListNotificationScreen
import com.example.mam.gui.screen.management.ListOrderScreen
import com.example.mam.gui.screen.management.ListProductScreen
import com.example.mam.gui.screen.management.ListPromotionScreen
import com.example.mam.gui.screen.management.ListShipperScreen
import com.example.mam.gui.screen.management.ListUserScreen
import com.example.mam.gui.screen.management.ManageCategoryScreen
import com.example.mam.gui.screen.management.ManageNotificationScreen
import com.example.mam.gui.screen.management.ManageOrderScreen
import com.example.mam.gui.screen.management.ManageProductScreen
import com.example.mam.gui.screen.management.ManagePromotionScreen
import com.example.mam.gui.screen.management.ManageShipperScreen
import com.example.mam.gui.screen.management.ManageUserScreen
import com.example.mam.viewmodel.authentication.ForgetPasswordViewModel
import com.example.mam.viewmodel.authentication.NotificationViewModel
import com.example.mam.viewmodel.authentication.SignInViewModel
import com.example.mam.viewmodel.authentication.SignUpViewModel
import com.example.mam.viewmodel.authentication.StartViewModel
import com.example.mam.viewmodel.client.CartViewModel
import com.example.mam.viewmodel.client.CheckOutViewModel
import com.example.mam.viewmodel.client.HomeScreenViewModel
import com.example.mam.viewmodel.client.ItemViewModel
import com.example.mam.viewmodel.client.OrderViewModel
import com.example.mam.viewmodel.client.ProfileViewModel
import com.example.mam.viewmodel.client.SearchViewModel
import com.example.mam.viewmodel.management.DashboardViewModel
import com.example.mam.viewmodel.management.ListCategoryViewModel
import com.example.mam.viewmodel.management.ListNotificationViewModel
import com.example.mam.viewmodel.management.ListOrderViewModel
import com.example.mam.viewmodel.management.ListProductViewModel
import com.example.mam.viewmodel.management.ListPromotionViewModel
import com.example.mam.viewmodel.management.ListShipperViewModel
import com.example.mam.viewmodel.management.ListUserViewModel
import com.example.mam.viewmodel.management.ManageCategoryViewModel
import com.example.mam.viewmodel.management.ManageNotificationViewModel
import com.example.mam.viewmodel.management.ManageOrderViewModel
import com.example.mam.viewmodel.management.ManageProductViewModel
import com.example.mam.viewmodel.management.ManagePromotionViewModel
import com.example.mam.viewmodel.management.ManageShipperViewModel
import com.example.mam.viewmodel.management.ManageUserViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.yourapp.ui.notifications.NotificationScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "Authentication",
) {
    val unAuthorize by AuthEventManager.unauthorizedEvent.collectAsStateWithLifecycle()
    LaunchedEffect(unAuthorize){
        if (unAuthorize) {
            Log.d("AUTH", "Unauthorized access detected, navigating to StartScreen")
            navController.navigate(AuthenticationScreen.Start.name) {
                popUpTo(AuthenticationScreen.Start.name) { inclusive = true }
                launchSingleTop = true
            }
            AuthEventManager.resetUnauthorized() // Reset the event
        }
    }
    val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navigation(
            startDestination = AuthenticationScreen.Start.name,
            route = "Authentication"
        ) {
            composable(
                route = AuthenticationScreen.Start.name,
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val startVM: StartViewModel = viewModel(backStackEntry, factory = StartViewModel.Factory)
                StartScreen(
                    viewModel = startVM,
                    onSignInClicked = {
                        navController.navigate(AuthenticationScreen.SignIn.name)
                    },
                    onAutoSignIn = {
                        navController.navigate(route = "Home") {
                            popUpTo("Authorization") { inclusive = true }
                        }
                    },
                    onSignInManager = {
                        navController.navigate(route = "Dashboard") {
                            popUpTo("Authorization") { inclusive = true }
                        }
                    },
                    onSignUpClicked = {
                        navController.navigate(AuthenticationScreen.SignUp.name)
                    },
                )
            }

            composable(
                route = AuthenticationScreen.SignIn.name,
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val signInVM: SignInViewModel = viewModel(backStackEntry, factory = SignInViewModel.Factory)
                SignInScreen(
                    viewModel = signInVM,
                    onSignInClicked = {
                        navController.navigate(route = "Home") {
                            popUpTo("Authorization") { inclusive = true }
                        }
                    },
                    onSignInManager = {
                        navController.navigate(route = "Dashboard") {
                            popUpTo("Authorization") { inclusive = true }
                        }
                    },
                    onForgotClicked = {
                        navController.navigate(AuthenticationScreen.ForgetPW.name)
                    },
                    onBackClicked = {
                        navController.popBackStack()
                    },
                )
            }

            composable(
                route = AuthenticationScreen.SignUp.name,
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val signUpVM: SignUpViewModel = viewModel(backStackEntry, factory = SignUpViewModel.Factory)
                SignUpScreen(
                    onSignInClicked = {
                        navController.navigate(AuthenticationScreen.SignIn.name)
                    },
                    onBackClicked = {
                        navController.popBackStack()
                    },
                )
            }

            composable(
                route = AuthenticationScreen.ForgetPW.name,
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val forgetPasswordVM: ForgetPasswordViewModel = viewModel(backStackEntry)
                ForgetPasswordScreen(
                    onChangeClicked = {

                    },
                    onCloseClicked = {
                        navController.popBackStack()
                    },
                )
            }
            composable(
                route = AuthenticationScreen.OTP.name,
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) {
            OTPScreen(
                onVerifyClicked = {

                },
                onCloseClicked = {
                    navController.popBackStack()
                }
            )
            }
        }
        navigation(
            startDestination = HomeScreen.HomeSreen.name,
            route = "Home"
        ){
            composable(
                route = HomeScreen.HomeSreen.name,
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewmodel: HomeScreenViewModel = viewModel(backStackEntry, factory = HomeScreenViewModel.Factory)
                HomeScreen(
                    onItemClicked = { item ->
                        navController.navigate("Details/${item.id}")
                    },
                    onSearchClicked = {navController.navigate(HomeScreen.Search.name)},
                    onCartClicked = { navController.navigate("Cart") },
                    onShippingClicked = {navController.navigate("Order")},
                    onNotificationClicked = {navController.navigate("Notification")},
                    onProfileClicked = {navController.navigate("Profile")},
                    viewmodel = viewmodel
                )
            }
            composable(
                route = "Notification",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: NotificationViewModel = viewModel(backStackEntry)
                NotificationScreen(
                    onBackClicked = {navController.popBackStack()},
                    viewModel = viewModel
                )
            }
            composable(
                route = "Profile",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ProfileViewModel = viewModel(backStackEntry, factory = ProfileViewModel.Factory)
                ProfileScreen(
                    onBackClicked = {navController.popBackStack()},
                    onLogoutClicked = {
                        navController.navigate(route = AuthenticationScreen.Start.name) {
                        popUpTo("Profile") { inclusive = true }
                    }},
                    onChangePasswordClicked = { },
                    viewModel = viewModel
                )
            }
            composable(
                route = "Details/{itemId}",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) {backStackEntry ->
                val viewModel: ItemViewModel = viewModel(backStackEntry)
                ItemScreen(
                    onBackClicked = {navController.popBackStack()},
                    onAddClick = {
                        viewModel.addToCart()
                        navController.popBackStack() },
                    onCartClicked = {navController.navigate("Cart")},
                    viewModel = viewModel
                )
            }
            composable(
                route = HomeScreen.Search.name,
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) {backStackEntry ->
                val viewModel: SearchViewModel = viewModel(backStackEntry)
                SearchScreen(
                    onItemClicked = { item ->
                        navController.navigate("Details/${item.id}")
                    },
                    onBackClicked = {navController.popBackStack()},
                    viewModel = viewModel
                )
            }
            composable(
                route = "Cart",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: CartViewModel = viewModel(backStackEntry)
                CartScreen(
                    onBackClicked = {navController.popBackStack()},
                    onAdditionalProductClicked = { item ->
                        navController.navigate("Details/${item}")
                    },
                    onCheckOutClicked = {navController.navigate(("CheckOut"))},
                    viewModel = viewModel
                )
            }
            composable(
                route = "CheckOut",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: CheckOutViewModel = viewModel(backStackEntry)
                CheckOutScreen(
                    onBackClicked = {navController.popBackStack()},
                    onCheckOutClicked = {
                        //order
                        navController.navigate("Order") },
                    onChangeAddressClicked = {
                        navController.navigate("Address")
                        // get address for saveStateHandle
                        navController.previousBackStackEntry?.savedStateHandle?.getLiveData<String>("address")?.observe(
                            backStackEntry
                        ) { address ->
                            viewModel.setAddress(address)
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(
                route = "Address",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ){
                MapScreen(
                    onBackClicked = {navController.popBackStack()},
                    onSelectClicked = { address ->
                        navController.previousBackStackEntry?.savedStateHandle?.set("address", address)
                        navController.popBackStack()
                    },
                )
            }
            composable(
                route = "Order",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ){ backStackEntry ->
                val viewModel: OrderViewModel = viewModel(backStackEntry)
                OrderScreen(
                    onBackClicked = {navController.popBackStack()},
                    onVerifyClicked = {},
                    viewModel = viewModel
                )
            }
            composable(
                route = "Dashboard",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ){ backStackEntry ->
                val viewModel: DashboardViewModel = viewModel(backStackEntry, factory = DashboardViewModel.Factory)
                DashboardScreen(
                    onBackClicked = {
                        navController.navigate(route = AuthenticationScreen.Start.name) {
                        popUpTo("Dashboard") { inclusive = true }
                        }
                    },
                    onItemClicked = { item ->
                        when(item) {
                            "Danh mục" -> navController.navigate("ListCategory")
                            "Sản phẩm" -> navController.navigate("ListProduct")
                            "Người dùng" -> navController.navigate("ListUser")
                            "Shipper" -> navController.navigate("ListShipper")
                            "Thông báo" -> navController.navigate("ListNotification")
                            "Khuyến mãi" -> navController.navigate("ListPromotion")
                            "Đơn hàng" -> navController.navigate("ListOrder")
                        }
                    },
                    onActiveOrderClicked = { order -> },
                    viewModel = viewModel
                )
            }
            composable(
                route = "ListCategory",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ListCategoryViewModel = viewModel(backStackEntry, factory = ListCategoryViewModel.Factory)
                ListCategoryScreen(
                    onBackClick = {navController.popBackStack()},
                    onAddCategoryClick = {
                        navController.navigate("AddCategory")
                    },
                    onEditCategoryClick = { categoryId ->
                        navController.navigate("EditCategory/${categoryId}")
                    },
                    onHomeClick = {
                        navController.navigate("Dashboard") {
                            popUpTo("Dashboard") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(
                route = "AddCategory",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageCategoryViewModel = viewModel(backStackEntry, factory = ManageCategoryViewModel.Factory)
                ManageCategoryScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = true,
                    isEdit = false,
                )
            }
            composable(
                route = "EditCategory/{categoryId}",
                arguments = listOf(navArgument("categoryId") { type = NavType.LongType }),
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageCategoryViewModel = viewModel(backStackEntry, factory = ManageCategoryViewModel.Factory)
                ManageCategoryScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = false,
                    isEdit = true,
                )
            }
            composable(
                route = "ListProduct",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ListProductViewModel = viewModel()
                ListProductScreen(
                    onBackClick = {navController.popBackStack()},
                    onAddProductClick = {
                        navController.navigate("AddProduct")
                    },
                    onEditProductClick = { product ->
                        navController.navigate("EditProduct/${product}")
                    },
                    onProductClick = { product ->
                        navController.navigate("DetailsProduct/${product}")
                    },
                    onHomeClick = {
                        navController.navigate("Dashboard") {
                            popUpTo("Dashboard") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(
                route = "EditProduct/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType }),
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageProductViewModel = viewModel(backStackEntry)
                ManageProductScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = false,
                    isEdit = true,
                )
            }
            composable(
                route = "DetailsProduct/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType }),
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageProductViewModel = viewModel(backStackEntry)
                ManageProductScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = false,
                    isEdit = false,
                )
            }
            composable(
                route = "AddProduct",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageProductViewModel = viewModel()
                ManageProductScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = true,
                    isEdit = false,
                )
            }
            composable(
                route = "ListOrder",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ListOrderViewModel = viewModel()
                ListOrderScreen(
                    onBackClick = {navController.popBackStack()},
                    onEditOrderClick = { orderId ->
                        navController.navigate("EditOrder/${orderId}")
                    },
                    onHomeClick = {
                        navController.navigate("Dashboard") {
                            popUpTo("Dashboard") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(
                route = "EditOrder/{orderId}",
                arguments = listOf(navArgument("orderId") { type = NavType.StringType }),
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) {
                backStackEntry ->
                val viewModel: ManageOrderViewModel = viewModel(backStackEntry)
                ManageOrderScreen(
                    onBackClick = {navController.popBackStack()},
                    viewModel = viewModel,
                )

            }
            composable(
                route = "ListNotification",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ListNotificationViewModel = viewModel()
                ListNotificationScreen(
                    onBackClick = {navController.popBackStack()},
                    onAddNotificationClick = {
                        navController.navigate("AddNotification")
                    },
                    onHomeClick = {
                        navController.navigate("Dashboard") {
                            popUpTo("Dashboard") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(
                route = "AddNotification",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageNotificationViewModel = viewModel()
                ManageNotificationScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                )
            }
            composable(
                route = "ListPromotion",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ListPromotionViewModel = viewModel()
                ListPromotionScreen(
                    onBackClick = {navController.popBackStack()},
                    onAddClick = {
                        navController.navigate("AddPromotion")
                    },
                    onHomeClick = {
                        navController.navigate("Dashboard") {
                            popUpTo("Dashboard") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(
                route = "AddPromotion",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManagePromotionViewModel = viewModel()
                ManagePromotionScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                )
            }
            composable(
                route = "ListShipper",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ListShipperViewModel = viewModel()
                ListShipperScreen(
                    onBackClick = {navController.popBackStack()},
                    onAddShipperClick = {
                        navController.navigate("AddShipper")
                    },
                    onEditShipperClick = { shipperId ->
                        navController.navigate("EditShipper/${shipperId}")
                    },
                    onHomeClick = {
                        navController.navigate("Dashboard") {
                            popUpTo("Dashboard") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(
                route = "AddShipper",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageShipperViewModel = viewModel()
                ManageShipperScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = true,
                    isEdit = false,
                )
            }
            composable(
                route = "EditShipper/{shipperId}",
                arguments = listOf(navArgument("shipperId") { type = NavType.StringType }),
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageShipperViewModel = viewModel(backStackEntry)
                ManageShipperScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = false,
                    isEdit = true,
                )
            }
            composable(
                route = "ListUser",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ListUserViewModel = viewModel()
                ListUserScreen(
                    onBackClick = {navController.popBackStack()},
                    onAddUserClick = {
                        navController.navigate("AddUser")
                    },
                    onEditUserClick = { userId ->
                        navController.navigate("EditUser/${userId}")
                    },
                    onHomeClick = {
                        navController.navigate("Dashboard") {
                            popUpTo("Dashboard") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onUserClick = { userId ->
                        navController.navigate("DetailsUser/${userId}")
                    },
                    viewModel = viewModel
                )
            }
            composable(
                route = "AddUser",
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageUserViewModel = viewModel()
                ManageUserScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = true,
                    isEdit = false,
                )
            }
            composable(
                route = "EditUser/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType }),
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageUserViewModel = viewModel(backStackEntry)
                ManageUserScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = false,
                    isEdit = true,
                )
            }
            composable(
                route = "DetailsUser/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType }),
                enterTransition = defaultTransitions(),
                exitTransition = defaultExitTransitions(),
                popEnterTransition = defaultPopEnterTransitions(),
                popExitTransition = defaultPopExitTransitions()
            ) { backStackEntry ->
                val viewModel: ManageUserViewModel = viewModel(backStackEntry)
                ManageUserScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = false,
                    isEdit = false,
                )
            }
        }
    }
}
fun defaultTransitions(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Up,
        tween(500)
    )
}

fun defaultExitTransitions(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
    fadeOut(
        animationSpec = tween(500),
        targetAlpha = 1f
    )
}

fun defaultPopEnterTransitions(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
    fadeIn(
        animationSpec = tween(500),
        initialAlpha = 1f
    )
}

fun defaultPopExitTransitions(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Down,
        tween(500)
    )
}