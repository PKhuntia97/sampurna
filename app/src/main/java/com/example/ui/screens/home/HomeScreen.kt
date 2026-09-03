package com.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.ProductEntity
import com.example.ui.components.AdvantageSection
import com.example.ui.components.CategoryGrid
import com.example.ui.components.ImageSearchDialog
import com.example.ui.components.LocationPickerSheet
import com.example.ui.components.MegaSaleBannerSection
import com.example.ui.components.ProductSection
import com.example.ui.components.SampurnaHeader
import com.example.ui.components.SampurnaLogoBadge
import com.example.ui.components.SearchBarSection
import com.example.ui.components.SubCategoryRow
import com.example.ui.components.VoiceSearchDialog
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.SubCategoryEntity
import com.example.ui.components.CategoryIconBadge
import com.example.ui.components.SubCategoryVisualBadge
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToAdminLogin: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToProductDetail: (ProductEntity) -> Unit = {},
    onNavigateToAccount: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToSellerPortal: () -> Unit = {},
    onNavigateToWishlist: () -> Unit = {},
    onNavigateToOffers: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val categories by viewModel.activeCategories.collectAsState()
    val allActiveSubCategories by viewModel.allActiveSubCategories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val subCategories by viewModel.subCategories.collectAsState()
    val selectedSubCategory by viewModel.selectedSubCategory.collectAsState()
    val generalBanners by viewModel.generalBanners.collectAsState()
    val categoryBanner by viewModel.categoryBanner.collectAsState()
    val products by viewModel.products.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val activeTab by viewModel.activeProductTab.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val cartCount by viewModel.cartItemCount.collectAsState()
    val snackBarMsg by viewModel.snackBarMessage.collectAsState()

    var showVoiceDialog by remember { mutableStateOf(false) }
    var showImageDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showAccountMenuDialog by remember { mutableStateOf(false) }

    LaunchedEffect(snackBarMsg) {
        snackBarMsg?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackBarMessage()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color.White
            ) {
                // Drawer Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Violet900)
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SampurnaLogoBadge()
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Sampurna",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Everything, For Everyone",
                                color = Violet100,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                NavigationDrawerItem(
                    label = { Text("Browse All Categories", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavigateToCategories()
                    },
                    icon = { Icon(Icons.Default.Category, contentDescription = null, tint = Violet700) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("My Account / Profile", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavigateToAccount()
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null, tint = Violet700) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("My Wishlist", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavigateToWishlist()
                    },
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Violet700) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Offers & Coupons", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavigateToOffers()
                    },
                    icon = { Icon(Icons.Default.Category, contentDescription = null, tint = Orange500) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Notifications", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        onNavigateToNotifications()
                    },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = null, tint = Violet700) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                NavigationDrawerItem(
                    label = { Text("Help & 24/7 Support", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = { coroutineScope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.HelpOutline, contentDescription = null, tint = Slate500) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("About Sampurna", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = { coroutineScope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Info, contentDescription = null, tint = Slate500) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Slate50,
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                // 1. TOP HEADER (Hamburger, Logo, Name, Location, Notification, Account)
                item {
                    SampurnaHeader(
                        currentLocation = currentLocation,
                        notificationCount = 3,
                        onMenuClick = { coroutineScope.launch { drawerState.open() } },
                        onLocationClick = { showLocationDialog = true },
                        onNotificationClick = { onNavigateToNotifications() },
                        onAccountClick = { onNavigateToAccount() }
                    )
                }

                // 2. SEARCH SECTION (Overlaps header, Voice & Image Match search)
                item {
                    SearchBarSection(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        onFilterClick = { /* Opens filter / sort */ },
                        onVoiceSearchClick = { showVoiceDialog = true },
                        onImageSearchClick = { showImageDialog = true },
                        modifier = Modifier.padding(top = 0.dp) // naturally sits below header
                    )
                }

                item { Spacer(modifier = Modifier.height(14.dp)) }

                // 3. MAIN CATEGORIES SECTION (Dynamic responsive horizontal grid)
                item {
                    CategoryGrid(
                        categories = categories,
                        selectedCategoryId = selectedCategory?.id,
                        onCategorySelected = { cat -> viewModel.selectCategory(cat) },
                        onMoreCategoriesClick = onNavigateToCategories
                    )
                }

                item { Spacer(modifier = Modifier.height(6.dp)) }

                // 4. CATEGORY-SPECIFIC MEGA SALE / ADVANTAGE BANNER (or General Slider)
                item {
                    MegaSaleBannerSection(
                        generalBanners = generalBanners,
                        categoryBanner = categoryBanner,
                        isCategorySelected = selectedCategory != null,
                        onShopNowClick = {
                            // Focus on deals or relevant category items
                            viewModel.setProductTab("todays_offers")
                        }
                    )
                }

                // 5. SELECTED CATEGORY SUB-CATEGORIES ROW (Appears below Category Mega Sale image)
                item {
                    selectedCategory?.let { currentCat ->
                        SubCategoryRow(
                            categoryName = currentCat.name,
                            subCategories = subCategories,
                            selectedSubCategoryId = selectedSubCategory?.id,
                            onSubCategoryClick = { subCat -> viewModel.selectSubCategory(subCat) },
                            onViewAllClick = onNavigateToCategories
                        )
                    }
                }

                // 6. ADVANTAGE SECTION (Fast Delivery, Secure Payment, Best Offers, 24/7 Support)
                item {
                    AdvantageSection()
                }

                // 6.1 DYNAMIC ALL CATEGORIES & SUB-CATEGORIES SECTION (Logo-based live directory)
                if (categories.isNotEmpty() && selectedCategory == null) {
                    item {
                        AllCategoriesDirectorySection(
                            categories = categories,
                            subCategories = allActiveSubCategories,
                            onCategoryClick = { cat -> viewModel.selectCategory(cat) },
                            onSubCategoryClick = { cat, subCat ->
                                viewModel.selectCategoryAndSubCategory(cat, subCat)
                            },
                            onExploreAll = onNavigateToCategories
                        )
                    }
                }

                // 7. BEST DEALS FOR YOU / PRODUCT PREVIEW SECTION
                item {
                    ProductSection(
                        products = products,
                        selectedTab = activeTab,
                        onTabSelected = { tabId -> viewModel.setProductTab(tabId) },
                        onAddToCart = { product -> viewModel.addToCart(product) },
                        onProductClick = { product -> onNavigateToProductDetail(product) },
                        onViewAllClick = { viewModel.setSearchQuery("") }
                    )
                }

                // Extra padding at bottom so last item is clearly visible above bottom navigation bar
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }

    // Voice Search Dialog
    if (showVoiceDialog) {
        VoiceSearchDialog(
            onDismiss = { showVoiceDialog = false },
            onVoiceResult = { query -> viewModel.setSearchQuery(query) }
        )
    }

    // Image Match Search Dialog
    if (showImageDialog) {
        ImageSearchDialog(
            onDismiss = { showImageDialog = false },
            onImageSelected = { categoryGuess ->
                val matched = categories.find { it.name.contains(categoryGuess, ignoreCase = true) }
                if (matched != null) {
                    viewModel.selectCategory(matched)
                } else {
                    viewModel.setSearchQuery(categoryGuess)
                }
            }
        )
    }

    // Location Picker Dialog
    if (showLocationDialog) {
        LocationPickerSheet(
            currentLocation = currentLocation,
            onLocationSelected = { loc -> viewModel.setLocation(loc) },
            onDismiss = { showLocationDialog = false }
        )
    }

    // Account / Admin Shortcut Modal
    if (showAccountMenuDialog) {
        AccountShortcutModal(
            onDismiss = { showAccountMenuDialog = false },
            onOpenAdmin = {
                showAccountMenuDialog = false
                onNavigateToAdminLogin()
            }
        )
    }
}

@Composable
fun NotificationsModal(onDismiss: () -> Unit) {
    val notices = listOf(
        "🎉 Mega Sale is Live! Up to 60% off on top electronics & fashion.",
        "⚡ Flash Deal: Extra 20% discount on first grocery order.",
        "📦 Your previous order was delivered successfully to Keonjhar."
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Notifications (3)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                notices.forEach { note ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Violet100.copy(alpha = 0.5f))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(note, fontSize = 12.sp, color = Slate700, lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AccountShortcutModal(
    onDismiss: () -> Unit,
    onOpenAdmin: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("My Sampurna Account", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Slate100)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Violet900),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("PK", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Pranaya Khuntia", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                        Text("Customer Account (Keonjhar, Odisha)", fontSize = 11.sp, color = Slate500)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Admin Login Access Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Orange500)
                        .clickable { onOpenAdmin() }
                        .padding(12.dp)
                        .testTag("open_admin_portal_button"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open Admin Management Portal", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun ProductQuickViewDialog(
    product: ProductEntity,
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Product Details", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "₹${com.example.ui.components.formatIndianCurrency(product.price)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Violet900
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Violet700)
                        .clickable { onAddToCart() }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add to Cart - Instant Order", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun AllCategoriesDirectorySection(
    categories: List<CategoryEntity>,
    subCategories: List<SubCategoryEntity>,
    onCategoryClick: (CategoryEntity) -> Unit,
    onSubCategoryClick: (CategoryEntity, SubCategoryEntity) -> Unit,
    onExploreAll: () -> Unit
) {
    val subCategoriesByCat = remember(subCategories) {
        subCategories.groupBy { it.categoryId }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "✨ Explore Categories & Subcategories",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "Live categories curated by Admin with instant visual logos",
                    fontSize = 11.sp,
                    color = Slate500
                )
            }
            Text(
                text = "View All",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Violet700,
                modifier = Modifier
                    .clickable { onExploreAll() }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        categories.forEach { category ->
            val subs = subCategoriesByCat[category.id] ?: emptyList()

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Category Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategoryClick(category) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CategoryIconBadge(
                                iconType = category.iconType,
                                imageUrl = category.iconUrl,
                                size = 38.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = category.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Text(
                                    text = if (subs.isNotEmpty()) "${subs.size} Sub-Categories available" else "Explore category products",
                                    fontSize = 10.sp,
                                    color = Slate500
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Violet100.copy(alpha = 0.6f),
                            modifier = Modifier.clickable { onCategoryClick(category) }
                        ) {
                            Text(
                                text = "Shop >",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Violet700,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    if (subs.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(subs, key = { it.id }) { sub ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Slate50,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                                    modifier = Modifier
                                        .clickable { onSubCategoryClick(category, sub) }
                                        .testTag("home_dir_subcat_${sub.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SubCategoryVisualBadge(
                                            iconType = sub.iconType,
                                            imageUrl = sub.imageUrl,
                                            size = 28.dp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = sub.name,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Slate800
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
}
