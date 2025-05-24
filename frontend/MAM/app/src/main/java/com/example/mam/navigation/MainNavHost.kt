package com.example.mam.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "Authentication",
) {
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000),
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f) },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                StartScreen(
                    onSignInClicked = {
                        navController.navigate(AuthenticationScreen.SignIn.name)
                    },
                    onSignUpClicked = {
                        navController.navigate(AuthenticationScreen.SignUp.name)
                    },
                    onTermsClicked = {
                        navController.navigate(AuthenticationScreen.Terms.name)
                    }
                )
            }

            composable(
                route = AuthenticationScreen.SignIn.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val signInVM: SignInViewModel = viewModel(backStackEntry, factory = SignInViewModel.Factory)
                SignInScreen(
                    onSignInClicked = {
                        if (signInVM.signInState.value.username == "1" || signInVM.signInState.value.password == "1") {
                            navController.navigate(route = "Home") {
                                popUpTo("Authorization") { inclusive = true }
                            }
                        }
                        else if (signInVM.signInState.value.username == "2" || signInVM.signInState.value.password == "2") {
                            navController.navigate(route = "Dashboard") {
                                popUpTo("Authorization") { inclusive = true }
                            }
                        }else {
                            coroutineScope.launch {
                                when (signInVM.SignIn()) {
                                    0 -> signInVM.notifySignInFalse()
                                    1 -> navController.navigate("Home") {
                                        popUpTo("Authorization") { inclusive = true }
                                    }
                                }
                            }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val signUpVM: SignUpViewModel = viewModel(backStackEntry)
                SignUpScreen(
                    onSignUpClicked = {
                        coroutineScope.launch {
                            when (signUpVM.SignUp()) {
                                0 -> signUpVM.notifySignUpFalse()
                                1 -> navController.navigate(AuthenticationScreen.SignIn.name)
                            }
                        }
                    },
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewmodel: HomeScreenViewModel = viewModel(backStackEntry)
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewModel: NotificationViewModel = viewModel(backStackEntry)
                NotificationScreen(
                    onBackClicked = {navController.popBackStack()},
                    viewModel = viewModel
                )
            }
            composable(
                route = "Profile",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewModel: ProfileViewModel = viewModel(backStackEntry)
                ProfileScreen(
                    onBackClicked = {navController.popBackStack()},
                    onChangePasswordClicked = { },
                    viewModel = viewModel
                )
            }
            composable(
                route = "Details/{itemId}",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                            viewModel.address = address
                        }
                    },
                    viewModel = viewModel
                )
            }
            composable(
                route = "Address",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )

                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ){ backStackEntry ->
                val viewModel: DashboardViewModel = viewModel(backStackEntry)
                DashboardScreen(
                    onBackClicked = {
                        navController.navigate(route = "Authorization") {
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewModel: ListCategoryViewModel = viewModel(backStackEntry)
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewModel: ManageCategoryViewModel = viewModel()
                ManageCategoryScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = true,
                    isEdit = false,
                )
            }
            composable(
                route = "EditCategory/{categoryId}",
                arguments = listOf(navArgument("categoryId") { type = NavType.StringType }),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewModel: ManageCategoryViewModel = viewModel(backStackEntry)
                ManageCategoryScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                    isAdd = false,
                    isEdit = true,
                )
            }
            composable(
                route = "ListProduct",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewModel: ManageNotificationViewModel = viewModel()
                ManageNotificationScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                )
            }
            composable(
                route = "ListPromotion",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
            ) { backStackEntry ->
                val viewModel: ManagePromotionViewModel = viewModel()
                ManagePromotionScreen(
                    viewModel = viewModel,
                    onBackClick = {navController.popBackStack()},
                )
            }
            composable(
                route = "ListShipper",
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        tween(1000)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(1000),
                        targetAlpha = 1f)
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(1000),
                        initialAlpha = 1f)
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        tween(1000)
                    )
                }
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