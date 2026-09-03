package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.data.local.entity.ProductEntity
import com.example.ui.components.NavigationTab
import com.example.ui.components.SampurnaBottomBar
import com.example.ui.screens.admin.AdminAuthState
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminLoginScreen
import com.example.ui.screens.admin.AdminViewModel
import com.example.ui.screens.auth.CustomerAuthState
import com.example.ui.screens.auth.CustomerAuthViewModel
import com.example.ui.screens.auth.CustomerLoginScreen
import com.example.ui.screens.auth.CustomerSignupScreen
import com.example.ui.screens.categories.CategoriesExplorerScreen
import com.example.ui.screens.customer.AddressManagementScreen
import com.example.ui.screens.customer.CustomerAccountScreen
import com.example.ui.screens.customer.CustomerAccountViewModel
import com.example.ui.screens.customer.MyOffersScreen
import com.example.ui.screens.customer.NotificationsScreen
import com.example.ui.screens.customer.ProductDetailScreen
import com.example.ui.screens.customer.RecentlyViewedScreen
import com.example.ui.screens.customer.WishlistScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.home.HomeViewModel
import com.example.ui.screens.cart.CartScreen
import com.example.ui.screens.cart.CartViewModel
import com.example.ui.screens.checkout.CheckoutScreen
import com.example.ui.screens.checkout.CheckoutViewModel
import com.example.ui.screens.orders.CustomerOrdersHistoryScreen
import com.example.ui.screens.orders.CustomerOrdersViewModel
import com.example.ui.screens.orders.OrderDetailTrackingScreen
import com.example.ui.screens.secondary.PlayScreen
import com.example.ui.screens.seller.SellerAuthState
import com.example.ui.screens.seller.SellerDashboardScreen
import com.example.ui.screens.seller.SellerLoginScreen
import com.example.ui.screens.seller.SellerSignupScreen
import com.example.ui.screens.seller.SellerViewModel
import com.example.ui.screens.delivery.DeliveryBoySignupScreen
import com.example.ui.screens.splash.SampurnaSplashScreen
import com.example.ui.theme.MyApplicationTheme

enum class AppScreen {
    SPLASH,
    STORE_MAIN,
    PRODUCT_DETAIL,
    CHECKOUT,
    CUSTOMER_ORDERS,
    ORDER_DETAIL_TRACKING,
    CUSTOMER_LOGIN,
    CUSTOMER_SIGNUP,
    CUSTOMER_ACCOUNT,
    ADDRESS_MANAGEMENT,
    WISHLIST,
    RECENTLY_VIEWED,
    MY_OFFERS,
    NOTIFICATIONS,
    SELLER_LOGIN,
    SELLER_SIGNUP,
    SELLER_DASHBOARD,
    DELIVERY_BOY_SIGNUP,
    ADMIN_LOGIN,
    ADMIN_DASHBOARD,
    CATEGORIES_EXPLORER
}

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val adminViewModel: AdminViewModel by viewModels()
    private val customerAuthViewModel: CustomerAuthViewModel by viewModels()
    private val customerAccountViewModel: CustomerAccountViewModel by viewModels()
    private val sellerViewModel: SellerViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val checkoutViewModel: CheckoutViewModel by viewModels()
    private val customerOrdersViewModel: CustomerOrdersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SampurnaMainApp(
                    homeViewModel = homeViewModel,
                    adminViewModel = adminViewModel,
                    authViewModel = customerAuthViewModel,
                    accountViewModel = customerAccountViewModel,
                    sellerViewModel = sellerViewModel,
                    cartViewModel = cartViewModel,
                    checkoutViewModel = checkoutViewModel,
                    customerOrdersViewModel = customerOrdersViewModel
                )
            }
        }
    }
}

@Composable
fun SampurnaMainApp(
    homeViewModel: HomeViewModel,
    adminViewModel: AdminViewModel,
    authViewModel: CustomerAuthViewModel,
    accountViewModel: CustomerAccountViewModel,
    sellerViewModel: SellerViewModel,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel,
    customerOrdersViewModel: CustomerOrdersViewModel
) {
    var currentAppScreen by remember { mutableStateOf(AppScreen.SPLASH) }
    var currentBottomTab by remember { mutableStateOf(NavigationTab.HOME) }
    var selectedProductForDetail by remember { mutableStateOf<ProductEntity?>(null) }
    var selectedOrderIdForTracking by remember { mutableStateOf<Long?>(null) }

    val cartItemCount by cartViewModel.totalItemCount.collectAsState()
    val adminAuthState by adminViewModel.authState.collectAsState()
    val customerAuthState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val sellerAuthState by sellerViewModel.sellerAuthState.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.id?.let { accountViewModel.setCurrentUserId(it) }
    }

    AnimatedContent(
        targetState = currentAppScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "main_screen_transition"
    ) { screen ->
        when (screen) {
            AppScreen.SPLASH -> {
                SampurnaSplashScreen(
                    onSplashFinished = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    }
                )
            }

            AppScreen.STORE_MAIN -> {
                Scaffold(
                    bottomBar = {
                        SampurnaBottomBar(
                            currentTab = currentBottomTab,
                            cartItemCount = cartItemCount,
                            onTabSelected = { tab ->
                                currentBottomTab = tab
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentBottomTab) {
                            NavigationTab.HOME -> {
                                HomeScreen(
                                    viewModel = homeViewModel,
                                    onNavigateToAdminLogin = {
                                        if (adminAuthState is AdminAuthState.Authenticated) {
                                            currentAppScreen = AppScreen.ADMIN_DASHBOARD
                                        } else {
                                            currentAppScreen = AppScreen.ADMIN_LOGIN
                                        }
                                    },
                                    onNavigateToCategories = {
                                        currentBottomTab = NavigationTab.CATEGORIES
                                    },
                                    onNavigateToProductDetail = { product ->
                                        selectedProductForDetail = product
                                        currentAppScreen = AppScreen.PRODUCT_DETAIL
                                    },
                                    onNavigateToAccount = {
                                        if (customerAuthState is CustomerAuthState.Authenticated) {
                                            currentAppScreen = AppScreen.CUSTOMER_ACCOUNT
                                        } else {
                                            currentAppScreen = AppScreen.CUSTOMER_LOGIN
                                        }
                                    },
                                    onNavigateToNotifications = {
                                        currentAppScreen = AppScreen.NOTIFICATIONS
                                    },
                                    onNavigateToSellerPortal = {
                                        if (sellerAuthState is SellerAuthState.Authenticated) {
                                            currentAppScreen = AppScreen.SELLER_DASHBOARD
                                        } else {
                                            currentAppScreen = AppScreen.SELLER_LOGIN
                                        }
                                    },
                                    onNavigateToWishlist = {
                                        currentAppScreen = AppScreen.WISHLIST
                                    },
                                    onNavigateToOffers = {
                                        currentAppScreen = AppScreen.MY_OFFERS
                                    }
                                )
                            }
                            NavigationTab.PLAY -> {
                                PlayScreen(
                                    onExploreShop = { currentBottomTab = NavigationTab.HOME }
                                )
                            }
                            NavigationTab.CATEGORIES -> {
                                CategoriesExplorerScreen(
                                    viewModel = homeViewModel,
                                    onCategorySelected = { cat ->
                                        homeViewModel.selectCategory(cat)
                                        currentBottomTab = NavigationTab.HOME
                                    },
                                    onNavigateBack = { currentBottomTab = NavigationTab.HOME }
                                )
                            }
                            NavigationTab.ACCOUNT -> {
                                if (customerAuthState is CustomerAuthState.Authenticated) {
                                    CustomerAccountScreen(
                                        accountViewModel = accountViewModel,
                                        onNavigateToAddresses = {
                                            currentAppScreen = AppScreen.ADDRESS_MANAGEMENT
                                        },
                                        onNavigateToWishlist = {
                                            currentAppScreen = AppScreen.WISHLIST
                                        },
                                        onNavigateToRecentlyViewed = {
                                            currentAppScreen = AppScreen.RECENTLY_VIEWED
                                        },
                                        onNavigateToOffers = {
                                            currentAppScreen = AppScreen.MY_OFFERS
                                        },
                                        onNavigateToNotifications = {
                                            currentAppScreen = AppScreen.NOTIFICATIONS
                                        },
                                        onNavigateToOrders = {
                                            currentAppScreen = AppScreen.CUSTOMER_ORDERS
                                        },
                                        onNavigateToSellerPortal = {
                                            if (sellerAuthState is SellerAuthState.Authenticated) {
                                                currentAppScreen = AppScreen.SELLER_DASHBOARD
                                            } else {
                                                currentAppScreen = AppScreen.SELLER_LOGIN
                                            }
                                        },
                                        onNavigateToAdminPortal = {
                                            if (adminAuthState is AdminAuthState.Authenticated) {
                                                currentAppScreen = AppScreen.ADMIN_DASHBOARD
                                            } else {
                                                currentAppScreen = AppScreen.ADMIN_LOGIN
                                            }
                                        },
                                        onLogout = {
                                            authViewModel.logout()
                                        }
                                    )
                                } else {
                                    CustomerLoginScreen(
                                        viewModel = authViewModel,
                                        onLoginSuccess = {
                                            // Authenticated, will re-render CustomerAccountScreen
                                        },
                                        onAdminLoginSuccess = {
                                            adminViewModel.login("pranayakhuntia85@gmail.com", "Pranaya@1997")
                                            currentAppScreen = AppScreen.ADMIN_DASHBOARD
                                        },
                                        onNavigateToSignup = {
                                            currentAppScreen = AppScreen.CUSTOMER_SIGNUP
                                        },
                                        onNavigateBack = {
                                            currentBottomTab = NavigationTab.HOME
                                        }
                                    )
                                }
                            }
                            NavigationTab.CART -> {
                                CartScreen(
                                    viewModel = cartViewModel,
                                    onContinueShopping = { currentBottomTab = NavigationTab.HOME },
                                    onProceedToCheckout = { currentAppScreen = AppScreen.CHECKOUT },
                                    onProductClick = { prodId ->
                                        // Product detail if needed
                                    }
                                )
                            }
                        }
                    }
                }
            }

            AppScreen.CHECKOUT -> {
                CheckoutScreen(
                    viewModel = checkoutViewModel,
                    onBackClick = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    },
                    onOrderComplete = { orderId ->
                        selectedOrderIdForTracking = orderId
                        currentAppScreen = AppScreen.ORDER_DETAIL_TRACKING
                    },
                    onContinueShopping = {
                        currentAppScreen = AppScreen.STORE_MAIN
                        currentBottomTab = NavigationTab.HOME
                    }
                )
            }

            AppScreen.CUSTOMER_ORDERS -> {
                CustomerOrdersHistoryScreen(
                    viewModel = customerOrdersViewModel,
                    onBackClick = {
                        currentAppScreen = AppScreen.STORE_MAIN
                        currentBottomTab = NavigationTab.ACCOUNT
                    },
                    onOrderClick = { orderId ->
                        selectedOrderIdForTracking = orderId
                        currentAppScreen = AppScreen.ORDER_DETAIL_TRACKING
                    }
                )
            }

            AppScreen.ORDER_DETAIL_TRACKING -> {
                selectedOrderIdForTracking?.let { orderId ->
                    OrderDetailTrackingScreen(
                        orderId = orderId,
                        viewModel = customerOrdersViewModel,
                        onBackClick = {
                            currentAppScreen = AppScreen.CUSTOMER_ORDERS
                        }
                    )
                } ?: run {
                    currentAppScreen = AppScreen.CUSTOMER_ORDERS
                }
            }

            AppScreen.PRODUCT_DETAIL -> {
                selectedProductForDetail?.let { product ->
                    ProductDetailScreen(
                        product = product,
                        accountViewModel = accountViewModel,
                        onAddToCart = { prod ->
                            homeViewModel.addToCart(prod)
                        },
                        onNavigateBack = {
                            currentAppScreen = AppScreen.STORE_MAIN
                        }
                    )
                } ?: run {
                    currentAppScreen = AppScreen.STORE_MAIN
                }
            }

            AppScreen.CUSTOMER_LOGIN -> {
                CustomerLoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        currentAppScreen = AppScreen.STORE_MAIN
                        currentBottomTab = NavigationTab.ACCOUNT
                    },
                    onAdminLoginSuccess = {
                        adminViewModel.login("pranayakhuntia85@gmail.com", "Pranaya@1997")
                        currentAppScreen = AppScreen.ADMIN_DASHBOARD
                    },
                    onNavigateToSignup = {
                        currentAppScreen = AppScreen.CUSTOMER_SIGNUP
                    },
                    onNavigateBack = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    }
                )
            }

            AppScreen.CUSTOMER_SIGNUP -> {
                CustomerSignupScreen(
                    viewModel = authViewModel,
                    onSignupSuccess = {
                        currentAppScreen = AppScreen.CUSTOMER_ACCOUNT
                    },
                    onNavigateToLogin = {
                        currentAppScreen = AppScreen.CUSTOMER_LOGIN
                    },
                    onNavigateBack = {
                        currentAppScreen = AppScreen.CUSTOMER_LOGIN
                    }
                )
            }

            AppScreen.CUSTOMER_ACCOUNT -> {
                CustomerAccountScreen(
                    accountViewModel = accountViewModel,
                    onNavigateToAddresses = {
                        currentAppScreen = AppScreen.ADDRESS_MANAGEMENT
                    },
                    onNavigateToWishlist = {
                        currentAppScreen = AppScreen.WISHLIST
                    },
                    onNavigateToRecentlyViewed = {
                        currentAppScreen = AppScreen.RECENTLY_VIEWED
                    },
                    onNavigateToOffers = {
                        currentAppScreen = AppScreen.MY_OFFERS
                    },
                    onNavigateToNotifications = {
                        currentAppScreen = AppScreen.NOTIFICATIONS
                    },
                    onNavigateToOrders = {
                        currentAppScreen = AppScreen.CUSTOMER_ORDERS
                    },
                    onNavigateToSellerPortal = {
                        if (sellerAuthState is SellerAuthState.Authenticated) {
                            currentAppScreen = AppScreen.SELLER_DASHBOARD
                        } else {
                            currentAppScreen = AppScreen.SELLER_LOGIN
                        }
                    },
                    onNavigateToAdminPortal = {
                        if (adminAuthState is AdminAuthState.Authenticated) {
                            currentAppScreen = AppScreen.ADMIN_DASHBOARD
                        } else {
                            currentAppScreen = AppScreen.ADMIN_LOGIN
                        }
                    },
                    onLogout = {
                        authViewModel.logout()
                        currentAppScreen = AppScreen.CUSTOMER_LOGIN
                    }
                )
            }

            AppScreen.ADDRESS_MANAGEMENT -> {
                AddressManagementScreen(
                    accountViewModel = accountViewModel,
                    onNavigateBack = {
                        currentAppScreen = AppScreen.CUSTOMER_ACCOUNT
                    }
                )
            }

            AppScreen.WISHLIST -> {
                WishlistScreen(
                    accountViewModel = accountViewModel,
                    onProductSelected = { prod ->
                        selectedProductForDetail = prod
                        currentAppScreen = AppScreen.PRODUCT_DETAIL
                    },
                    onAddToCart = { prod ->
                        homeViewModel.addToCart(prod)
                    },
                    onNavigateBack = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    }
                )
            }

            AppScreen.RECENTLY_VIEWED -> {
                RecentlyViewedScreen(
                    accountViewModel = accountViewModel,
                    onProductSelected = { prod ->
                        selectedProductForDetail = prod
                        currentAppScreen = AppScreen.PRODUCT_DETAIL
                    },
                    onAddToCart = { prod ->
                        homeViewModel.addToCart(prod)
                    },
                    onNavigateBack = {
                        currentAppScreen = AppScreen.CUSTOMER_ACCOUNT
                    }
                )
            }

            AppScreen.MY_OFFERS -> {
                MyOffersScreen(
                    accountViewModel = accountViewModel,
                    onNavigateBack = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    }
                )
            }

            AppScreen.NOTIFICATIONS -> {
                NotificationsScreen(
                    accountViewModel = accountViewModel,
                    onNavigateBack = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    }
                )
            }

            AppScreen.SELLER_LOGIN -> {
                SellerLoginScreen(
                    sellerViewModel = sellerViewModel,
                    onLoginSuccess = {
                        currentAppScreen = AppScreen.SELLER_DASHBOARD
                    },
                    onNavigateBack = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    },
                    onNavigateToSignup = {
                        currentAppScreen = AppScreen.SELLER_SIGNUP
                    }
                )
            }

            AppScreen.SELLER_SIGNUP -> {
                SellerSignupScreen(
                    sellerViewModel = sellerViewModel,
                    onSignupSuccess = {
                        currentAppScreen = AppScreen.SELLER_LOGIN
                    },
                    onNavigateToLogin = {
                        currentAppScreen = AppScreen.SELLER_LOGIN
                    },
                    onNavigateBack = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    }
                )
            }

            AppScreen.SELLER_DASHBOARD -> {
                SellerDashboardScreen(
                    sellerViewModel = sellerViewModel,
                    onNavigateBack = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    },
                    onLogout = {
                        sellerViewModel.logout()
                        currentAppScreen = AppScreen.STORE_MAIN
                    }
                )
            }

            AppScreen.DELIVERY_BOY_SIGNUP -> {
                DeliveryBoySignupScreen(
                    onSignupSuccess = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    },
                    onNavigateBack = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    }
                )
            }

            AppScreen.ADMIN_LOGIN -> {
                AdminLoginScreen(
                    viewModel = adminViewModel,
                    onLoginSuccess = {
                        currentAppScreen = AppScreen.ADMIN_DASHBOARD
                    },
                    onNavigateBack = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    }
                )
            }

            AppScreen.ADMIN_DASHBOARD -> {
                AdminDashboardScreen(
                    viewModel = adminViewModel,
                    onNavigateToHome = {
                        currentAppScreen = AppScreen.STORE_MAIN
                        currentBottomTab = NavigationTab.HOME
                    },
                    onLogout = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    },
                    onNavigateToSellerSignup = {
                        currentAppScreen = AppScreen.SELLER_SIGNUP
                    },
                    onNavigateToDeliverySignup = {
                        currentAppScreen = AppScreen.DELIVERY_BOY_SIGNUP
                    }
                )
            }

            AppScreen.CATEGORIES_EXPLORER -> {
                CategoriesExplorerScreen(
                    viewModel = homeViewModel,
                    onCategorySelected = { cat ->
                        homeViewModel.selectCategory(cat)
                        currentAppScreen = AppScreen.STORE_MAIN
                        currentBottomTab = NavigationTab.HOME
                    },
                    onNavigateBack = {
                        currentAppScreen = AppScreen.STORE_MAIN
                    }
                )
            }
        }
    }
}

