package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import android.content.Intent
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.CategoryBannerEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.DeliveryPartnerEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SubCategoryEntity
import com.example.ui.components.CategoryIconBadge
import com.example.ui.components.SubCategoryVisualBadge
import com.example.ui.components.CATEGORY_PRESET_ICONS
import com.example.ui.components.SUBCATEGORY_PRESET_ICONS
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel,
    onNavigateToHome: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToSellerSignup: () -> Unit = {},
    onNavigateToDeliverySignup: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val adminToast by viewModel.adminToast.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val totalCategories by viewModel.totalCategoriesCount.collectAsState()
    val totalSubCategories by viewModel.totalSubCategoriesCount.collectAsState()
    val totalBanners by viewModel.totalBannersCount.collectAsState()
    val activeBanners by viewModel.activeBannersCount.collectAsState()
    val totalProducts by viewModel.totalProductsCount.collectAsState()
    val activeProducts by viewModel.activeProductsCount.collectAsState()

    val categories by viewModel.allCategories.collectAsState()
    val subCategories by viewModel.allSubCategories.collectAsState()
    val banners by viewModel.allBanners.collectAsState()
    val products by viewModel.allProducts.collectAsState()
    val sellers by viewModel.allSellers.collectAsState()
    val deliveryBoys by viewModel.allDeliveryBoys.collectAsState()
    val pendingSellers by viewModel.pendingSellers.collectAsState()
    val pendingDeliveryBoys by viewModel.pendingDeliveryBoys.collectAsState()
    val pendingApprovals by viewModel.pendingApprovalProducts.collectAsState()
    val commissionRate by viewModel.commissionRate.collectAsState()

    // Dialog state controllers
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<CategoryEntity?>(null) }

    var showAddSubCategoryDialog by remember { mutableStateOf(false) }
    var subCategoryToEdit by remember { mutableStateOf<SubCategoryEntity?>(null) }

    var showAddBannerDialog by remember { mutableStateOf(false) }
    var bannerToEdit by remember { mutableStateOf<CategoryBannerEntity?>(null) }

    var showAddProductDialog by remember { mutableStateOf(false) }

    LaunchedEffect(adminToast) {
        adminToast?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Sampurna Admin Console", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Catalog & Banner Management", fontSize = 11.sp, color = Violet100)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(Icons.Default.Store, contentDescription = "View Store Home", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Violet900)
            )
        },
        floatingActionButton = {
            when (selectedTab) {
                AdminTab.CATEGORIES -> {
                    FloatingActionButton(
                        onClick = { showAddCategoryDialog = true },
                        containerColor = Violet700,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("admin_fab_add_category")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Category")
                    }
                }
                AdminTab.SUB_CATEGORIES -> {
                    FloatingActionButton(
                        onClick = { showAddSubCategoryDialog = true },
                        containerColor = Violet700,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("admin_fab_add_subcategory")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Sub-Category")
                    }
                }
                AdminTab.BANNERS -> {
                    FloatingActionButton(
                        onClick = { showAddBannerDialog = true },
                        containerColor = Orange500,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("admin_fab_add_banner")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Banner")
                    }
                }
                AdminTab.PRODUCTS -> {
                    FloatingActionButton(
                        onClick = { showAddProductDialog = true },
                        containerColor = Violet700,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("admin_fab_add_product")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Product")
                    }
                }
                else -> Unit
            }
        },
        containerColor = Slate50,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Admin Navigation Tabs Bar
            val totalPending = pendingSellers.size + pendingDeliveryBoys.size + pendingApprovals.size
            AdminTabsRow(
                currentTab = selectedTab,
                onTabSelected = { viewModel.selectTab(it) },
                totalPendingCount = totalPending
            )

            // Main Content Body based on tab
            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    AdminTab.DASHBOARD -> {
                        DashboardOverviewTab(
                            totalCategories = totalCategories,
                            totalSubCategories = totalSubCategories,
                            totalBanners = totalBanners,
                            activeBanners = activeBanners,
                            totalProducts = totalProducts,
                            activeProducts = activeProducts,
                            totalDeliveryBoys = deliveryBoys.size,
                            onDutyDeliveryBoys = deliveryBoys.count { it.isOnDuty && it.isActive },
                            pendingSellersCount = pendingSellers.size,
                            pendingDeliveryBoysCount = pendingDeliveryBoys.size,
                            pendingProductsCount = pendingApprovals.size,
                            sellerSignupLink = viewModel.sellerSignupLink,
                            deliveryBoySignupLink = viewModel.deliveryBoySignupLink,
                            onTabNavigate = { viewModel.selectTab(it) },
                            onGoToStore = onNavigateToHome,
                            onNavigateToSellerSignup = onNavigateToSellerSignup,
                            onNavigateToDeliverySignup = onNavigateToDeliverySignup
                        )
                    }
                    AdminTab.CATEGORIES -> {
                        CategoryManagementTab(
                            categories = categories,
                            onAddCategory = { showAddCategoryDialog = true },
                            onEditCategory = { categoryToEdit = it },
                            onDeleteCategory = { viewModel.deleteCategory(it) },
                            onToggleActive = { viewModel.toggleCategoryActive(it) }
                        )
                    }
                    AdminTab.SUB_CATEGORIES -> {
                        SubCategoryManagementTab(
                            subCategories = subCategories,
                            categories = categories,
                            onAddSubCategory = { showAddSubCategoryDialog = true },
                            onEditSubCategory = { subCategoryToEdit = it },
                            onDeleteSubCategory = { viewModel.deleteSubCategory(it) },
                            onToggleActive = { viewModel.toggleSubCategoryActive(it) }
                        )
                    }
                    AdminTab.BANNERS -> {
                        BannerManagementTab(
                            banners = banners,
                            categories = categories,
                            onAddBanner = { showAddBannerDialog = true },
                            onEditBanner = { bannerToEdit = it },
                            onDeleteBanner = { viewModel.deleteBanner(it) },
                            onToggleActive = { viewModel.toggleBannerActive(it) }
                        )
                    }
                    AdminTab.PRODUCTS -> {
                        ProductManagementTab(
                            products = products,
                            categories = categories,
                            onAddProduct = { showAddProductDialog = true },
                            onToggleActive = { viewModel.toggleProductActive(it) },
                            onDeleteProduct = { viewModel.deleteProduct(it) }
                        )
                    }
                    AdminTab.SELLERS -> {
                        SellersManagementTab(
                            sellers = sellers,
                            categories = categories,
                            onAddSeller = { name, bName, mob, mail, addr, info, cats, subs ->
                                viewModel.addSeller(name, bName, mob, mail, addr, info, cats, subs)
                            }
                        )
                    }
                    AdminTab.DELIVERY_BOYS -> {
                        DeliveryBoysManagementTab(
                            deliveryBoys = deliveryBoys,
                            onCreateDeliveryBoy = { name, mobile, email, pass, vType, vNum, lic, hub, emg ->
                                viewModel.createDeliveryBoy(name, mobile, email, pass, vType, vNum, lic, hub, emg)
                            },
                            onUpdateDeliveryBoy = { viewModel.updateDeliveryBoy(it) },
                            onToggleActive = { viewModel.toggleDeliveryBoyActive(it) },
                            onToggleDuty = { viewModel.toggleDeliveryBoyDuty(it) },
                            onDeleteDeliveryBoy = { viewModel.deleteDeliveryBoy(it) }
                        )
                    }
                    AdminTab.APPROVALS -> {
                        AdminApprovalsCenterTab(
                            pendingSellers = pendingSellers,
                            pendingDeliveryBoys = pendingDeliveryBoys,
                            pendingProducts = pendingApprovals,
                            onApproveSeller = { viewModel.approveSeller(it.id) },
                            onRejectSeller = { seller, reason -> viewModel.rejectSeller(seller.id, reason) },
                            onApproveDeliveryBoy = { viewModel.approveDeliveryBoy(it.id) },
                            onRejectDeliveryBoy = { viewModel.rejectDeliveryBoy(it.id) },
                            onApproveProduct = { viewModel.approveProduct(it.id) },
                            onRejectProduct = { prod, reason -> viewModel.rejectProduct(prod.id, reason) }
                        )
                    }
                    AdminTab.COMMISSION -> {
                        CommissionSettingsTab(
                            commissionRate = commissionRate,
                            onUpdateCommission = { viewModel.setCommissionPercent(it) }
                        )
                    }
                    AdminTab.ORDERS -> {
                        val allOrders by viewModel.allOrders.collectAsState()
                        val cancellationAudits by viewModel.allCancellationAudits.collectAsState()
                        AdminOrdersTab(
                            orders = allOrders,
                            cancellationAudits = cancellationAudits,
                            onResetPenalty = { custId, prodId ->
                                viewModel.resetCustomerCancellationPenalty(custId, prodId)
                            }
                        )
                    }
                    AdminTab.SETTINGS -> {
                        AdminSystemSettingsTab(
                            onSaveDelivery = { baseKm, baseFee, extraKm ->
                                viewModel.updateDeliveryConfig(baseKm, baseFee, extraKm)
                            },
                            onSaveCod = { enabled, fee ->
                                viewModel.updateCodConfig(enabled, fee)
                            },
                            onSaveGateway = { gw, key, sec, wh, test ->
                                viewModel.updatePaymentGatewayConfig(gw, key, sec, wh, test)
                            }
                        )
                    }
                }
            }
        }
    }

    // Add / Edit Category Dialog
    if (showAddCategoryDialog) {
        CategoryFormDialog(
            category = null,
            onDismiss = { showAddCategoryDialog = false },
            onSave = { name, icon, desc, order, iconUrl ->
                viewModel.addCategory(name, icon, desc, order, iconUrl)
                showAddCategoryDialog = false
            }
        )
    }

    categoryToEdit?.let { cat ->
        CategoryFormDialog(
            category = cat,
            onDismiss = { categoryToEdit = null },
            onSave = { name, icon, desc, order, iconUrl ->
                viewModel.updateCategory(
                    cat.copy(name = name, iconType = icon, iconUrl = iconUrl, description = desc, displayOrder = order)
                )
                categoryToEdit = null
            }
        )
    }

    // Add / Edit SubCategory Dialog
    if (showAddSubCategoryDialog) {
        SubCategoryFormDialog(
            subCategory = null,
            categories = categories,
            onDismiss = { showAddSubCategoryDialog = false },
            onSave = { catId, name, icon, order, imageUrl ->
                viewModel.addSubCategory(catId, name, icon, order, imageUrl)
                showAddSubCategoryDialog = false
            }
        )
    }

    subCategoryToEdit?.let { sub ->
        SubCategoryFormDialog(
            subCategory = sub,
            categories = categories,
            onDismiss = { subCategoryToEdit = null },
            onSave = { catId, name, icon, order, imageUrl ->
                viewModel.updateSubCategory(
                    sub.copy(categoryId = catId, name = name, iconType = icon, imageUrl = imageUrl, displayOrder = order)
                )
                subCategoryToEdit = null
            }
        )
    }

    // Add / Edit Banner Dialog
    if (showAddBannerDialog) {
        BannerFormDialog(
            banner = null,
            categories = categories,
            onDismiss = { showAddBannerDialog = false },
            onSave = { catId, title, sub, tag, discount, bType, order ->
                viewModel.addBanner(catId, title, sub, tag, discount, bType, order)
                showAddBannerDialog = false
            }
        )
    }

    bannerToEdit?.let { b ->
        BannerFormDialog(
            banner = b,
            categories = categories,
            onDismiss = { bannerToEdit = null },
            onSave = { catId, title, sub, tag, discount, bType, order ->
                viewModel.updateBanner(
                    b.copy(categoryId = catId, title = title, subtitle = sub, tag = tag, discountText = discount, bannerType = bType, displayOrder = order)
                )
                bannerToEdit = null
            }
        )
    }

    // Add Product Dialog
    if (showAddProductDialog) {
        ProductFormDialog(
            categories = categories,
            onDismiss = { showAddProductDialog = false },
            onSave = { catId, subId, name, icon, price, mrp, discount, rating, tag ->
                viewModel.addProduct(catId, subId, name, icon, price, mrp, discount, rating, tag)
                showAddProductDialog = false
            }
        )
    }
}

@Composable
fun AdminTabsRow(
    currentTab: AdminTab,
    onTabSelected: (AdminTab) -> Unit,
    totalPendingCount: Int = 0,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf(
                AdminTab.DASHBOARD to "Dashboard",
                AdminTab.ORDERS to "Orders & Cancellations",
                AdminTab.DELIVERY_BOYS to "Delivery Boys",
                AdminTab.CATEGORIES to "Categories",
                AdminTab.SUB_CATEGORIES to "Sub-Categories",
                AdminTab.BANNERS to "Mega Sale Banners",
                AdminTab.PRODUCTS to "Products",
                AdminTab.SELLERS to "Sellers",
                AdminTab.APPROVALS to if (totalPendingCount > 0) "Approvals ($totalPendingCount)" else "Approvals",
                AdminTab.COMMISSION to "2% Commission",
                AdminTab.SETTINGS to "Delivery & Gateway"
            )

            items(tabs) { (tab, title) ->
                val isSelected = currentTab == tab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Violet700 else Slate100)
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("admin_tab_${tab.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Slate700
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardOverviewTab(
    totalCategories: Int,
    totalSubCategories: Int,
    totalBanners: Int,
    activeBanners: Int,
    totalProducts: Int,
    activeProducts: Int,
    totalDeliveryBoys: Int = 0,
    onDutyDeliveryBoys: Int = 0,
    pendingSellersCount: Int = 0,
    pendingDeliveryBoysCount: Int = 0,
    pendingProductsCount: Int = 0,
    sellerSignupLink: String = "https://sampurna.in/seller/signup",
    deliveryBoySignupLink: String = "https://sampurna.in/delivery/signup",
    onTabNavigate: (AdminTab) -> Unit,
    onGoToStore: () -> Unit,
    onNavigateToSellerSignup: () -> Unit = {},
    onNavigateToDeliverySignup: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Admin Dashboard Overview", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Slate900)
            Text("Real-time dynamic catalog metrics & delivery fleet operations", fontSize = 12.sp, color = Slate500)
        }

        // Generated Onboarding Links Card (Duita Link Generate)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Violet200),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Violet100),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Link, contentDescription = null, tint = Violet700, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Partner Onboarding Links", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
                                Text("Share links for Seller & Delivery Boy sign-up", fontSize = 11.sp, color = Slate500)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Approval Required", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Link 1: Seller Signup
                    OnboardingLinkItem(
                        title = "1. Seller / Merchant Sign Up Link",
                        description = "Merchants register store details. Accounts remain PENDING until you approve.",
                        linkUrl = sellerSignupLink,
                        badgeColor = Violet700,
                        icon = Icons.Default.Store,
                        onOpen = onNavigateToSellerSignup
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Slate100)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Link 2: Delivery Boy Signup
                    OnboardingLinkItem(
                        title = "2. Delivery Boy Sign Up Link",
                        description = "Riders submit driving license & vehicle. Requires your verification.",
                        linkUrl = deliveryBoySignupLink,
                        badgeColor = Color(0xFFD97706),
                        icon = Icons.Default.TwoWheeler,
                        onOpen = onNavigateToDeliverySignup
                    )
                }
            }
        }

        // Pending Approvals Action Banner
        val totalPending = pendingSellersCount + pendingDeliveryBoysCount + pendingProductsCount
        if (totalPending > 0) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth().clickable { onTabNavigate(AdminTab.APPROVALS) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "$totalPending Pending Application(s) Waiting!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF92400E)
                                )
                                Text(
                                    text = "Sellers: $pendingSellersCount | Fleet: $pendingDeliveryBoysCount | Products: $pendingProductsCount",
                                    fontSize = 11.sp,
                                    color = Color(0xFFB45309)
                                )
                            }
                        }
                        Button(
                            onClick = { onTabNavigate(AdminTab.APPROVALS) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Review", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Fleet Quick Metric
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(
                    title = "Delivery Fleet",
                    count = totalDeliveryBoys.toString(),
                    subtitle = "Keonjhar Delivery Boys",
                    icon = Icons.Default.LocalShipping,
                    color = Color(0xFFD97706),
                    modifier = Modifier.weight(1f),
                    onClick = { onTabNavigate(AdminTab.DELIVERY_BOYS) }
                )
                AdminStatCard(
                    title = "Fleet On Duty",
                    count = onDutyDeliveryBoys.toString(),
                    subtitle = "Online Ready to Deliver",
                    icon = Icons.Default.LocalShipping,
                    color = Color(0xFF059669),
                    modifier = Modifier.weight(1f),
                    onClick = { onTabNavigate(AdminTab.DELIVERY_BOYS) }
                )
            }
        }

        // Metrics Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(
                    title = "Total Categories",
                    count = totalCategories.toString(),
                    subtitle = "Dynamic in App",
                    icon = Icons.Default.Category,
                    color = Violet700,
                    modifier = Modifier.weight(1f),
                    onClick = { onTabNavigate(AdminTab.CATEGORIES) }
                )
                AdminStatCard(
                    title = "Sub-Categories",
                    count = totalSubCategories.toString(),
                    subtitle = "Linked to Parents",
                    icon = Icons.Default.Inventory,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.weight(1f),
                    onClick = { onTabNavigate(AdminTab.SUB_CATEGORIES) }
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(
                    title = "Total Banners",
                    count = totalBanners.toString(),
                    subtitle = "Mega Sale & Offers",
                    icon = Icons.Default.ViewCarousel,
                    color = Orange500,
                    modifier = Modifier.weight(1f),
                    onClick = { onTabNavigate(AdminTab.BANNERS) }
                )
                AdminStatCard(
                    title = "Active Banners",
                    count = activeBanners.toString(),
                    subtitle = "Live on Store",
                    icon = Icons.Default.Image,
                    color = Emerald500,
                    modifier = Modifier.weight(1f),
                    onClick = { onTabNavigate(AdminTab.BANNERS) }
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminStatCard(
                    title = "Total Products",
                    count = totalProducts.toString(),
                    subtitle = "Catalog items",
                    icon = Icons.Default.Store,
                    color = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f),
                    onClick = { onTabNavigate(AdminTab.PRODUCTS) }
                )
                AdminStatCard(
                    title = "Active Products",
                    count = activeProducts.toString(),
                    subtitle = "Visible to Users",
                    icon = Icons.Default.Inventory,
                    color = Color(0xFF059669),
                    modifier = Modifier.weight(1f),
                    onClick = { onTabNavigate(AdminTab.PRODUCTS) }
                )
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Violet100.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("💡 Dynamic Synchronization Rule", fontWeight = FontWeight.Bold, color = Violet950, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Every category, subcategory, and Mega Sale banner created or updated in this admin panel immediately syncs with the Customer Home Page via the local Room Database architecture.",
                        fontSize = 12.sp,
                        color = Slate700,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onGoToStore,
                        colors = ButtonDefaults.buttonColors(containerColor = Violet900),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("View Customer Home Page", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    count: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
                Text(text = count, fontSize = 22.sp, fontWeight = FontWeight.Black, color = Slate900)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate800)
            Text(text = subtitle, fontSize = 10.sp, color = Slate500)
        }
    }
}

@Composable
fun CategoryManagementTab(
    categories: List<CategoryEntity>,
    onAddCategory: () -> Unit,
    onEditCategory: (CategoryEntity) -> Unit,
    onDeleteCategory: (CategoryEntity) -> Unit,
    onToggleActive: (CategoryEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Category Management (${categories.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                    Text("Add, edit, disable, or order categories", fontSize = 11.sp, color = Slate500)
                }
                Button(
                    onClick = onAddCategory,
                    colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Category", fontSize = 11.sp)
                }
            }
        }

        items(categories, key = { it.id }) { cat ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        CategoryIconBadge(iconType = cat.iconType, imageUrl = cat.iconUrl, size = 44.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(cat.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            Text("Type: ${cat.iconType} | Order: ${cat.displayOrder}", fontSize = 11.sp, color = Slate500)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = cat.isActive,
                            onCheckedChange = { onToggleActive(cat) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Violet700, checkedTrackColor = Violet100)
                        )
                        IconButton(onClick = { onEditCategory(cat) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Violet700, modifier = Modifier.size(20.dp))
                        }
                        IconButton(onClick = { onDeleteCategory(cat) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Rose500, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubCategoryManagementTab(
    subCategories: List<SubCategoryEntity>,
    categories: List<CategoryEntity>,
    onAddSubCategory: () -> Unit,
    onEditSubCategory: (SubCategoryEntity) -> Unit,
    onDeleteSubCategory: (SubCategoryEntity) -> Unit,
    onToggleActive: (SubCategoryEntity) -> Unit
) {
    val categoryMap = remember(categories) { categories.associateBy { it.id } }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Sub-Category Management (${subCategories.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                    Text("Dynamic subcategories assigned to parents", fontSize = 11.sp, color = Slate500)
                }
                Button(
                    onClick = onAddSubCategory,
                    colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Sub-Cat", fontSize = 11.sp)
                }
            }
        }

        items(subCategories, key = { it.id }) { subCat ->
            val parentName = categoryMap[subCat.categoryId]?.name ?: "Category #${subCat.categoryId}"

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        SubCategoryVisualBadge(iconType = subCat.iconType, imageUrl = subCat.imageUrl, size = 44.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(subCat.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            Text("Parent: $parentName | Order: ${subCat.displayOrder}", fontSize = 11.sp, color = Slate500)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = subCat.isActive,
                            onCheckedChange = { onToggleActive(subCat) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Violet700, checkedTrackColor = Violet100)
                        )
                        IconButton(onClick = { onEditSubCategory(subCat) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Violet700, modifier = Modifier.size(20.dp))
                        }
                        IconButton(onClick = { onDeleteSubCategory(subCat) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Rose500, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BannerManagementTab(
    banners: List<CategoryBannerEntity>,
    categories: List<CategoryEntity>,
    onAddBanner: () -> Unit,
    onEditBanner: (CategoryBannerEntity) -> Unit,
    onDeleteBanner: (CategoryBannerEntity) -> Unit,
    onToggleActive: (CategoryBannerEntity) -> Unit
) {
    val categoryMap = remember(categories) { categories.associateBy { it.id } }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Mega Sale Banners (${banners.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                    Text("Promotional banners linked to categories", fontSize = 11.sp, color = Slate500)
                }
                Button(
                    onClick = onAddBanner,
                    colors = ButtonDefaults.buttonColors(containerColor = Orange500),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Banner", fontSize = 11.sp)
                }
            }
        }

        items(banners, key = { it.id }) { banner ->
            val assignedCat = if (banner.categoryId == null) "All (General Home Slider)" else (categoryMap[banner.categoryId]?.name ?: "Category #${banner.categoryId}")

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(banner.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            Text("Assigned: $assignedCat", fontSize = 11.sp, color = Violet700, fontWeight = FontWeight.SemiBold)
                            Text("Discount: ${banner.discountText} | Tag: ${banner.tag}", fontSize = 10.sp, color = Slate500)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = banner.isActive,
                                onCheckedChange = { onToggleActive(banner) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Orange500, checkedTrackColor = Violet100)
                            )
                            IconButton(onClick = { onEditBanner(banner) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Violet700, modifier = Modifier.size(20.dp))
                            }
                            IconButton(onClick = { onDeleteBanner(banner) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Rose500, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductManagementTab(
    products: List<ProductEntity>,
    categories: List<CategoryEntity>,
    onAddProduct: () -> Unit,
    onToggleActive: (ProductEntity) -> Unit,
    onDeleteProduct: (ProductEntity) -> Unit
) {
    val categoryMap = remember(categories) { categories.associateBy { it.id } }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Products Catalog (${products.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                    Text("Catalog items with live discounts & ratings", fontSize = 11.sp, color = Slate500)
                }
                Button(
                    onClick = onAddProduct,
                    colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Product", fontSize = 11.sp)
                }
            }
        }

        items(products, key = { it.id }) { product ->
            val catName = categoryMap[product.categoryId]?.name ?: "Cat #${product.categoryId}"

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(product.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                        Text("Category: $catName | Tab: ${product.tag}", fontSize = 11.sp, color = Violet700)
                        Text("Price: ₹${product.price.toInt()} | MRP: ₹${product.mrp.toInt()} (-${product.discount}%)", fontSize = 11.sp, color = Slate600)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = product.isActive,
                            onCheckedChange = { onToggleActive(product) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Violet700, checkedTrackColor = Violet100)
                        )
                        IconButton(onClick = { onDeleteProduct(product) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Rose500, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSettingsTab(
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Admin Settings & Platform Identity", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Slate900)

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Sampurna E-Commerce Platform", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                Text("Version 1.0 (Part 1 Foundation)", fontSize = 12.sp, color = Slate500)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Text("Active Admin: admin@sampurna.com", fontSize = 12.sp, color = Slate700)
                Text("Database: SQLite Room Engine (Offline-First, Realtime StateFlows)", fontSize = 12.sp, color = Slate700)
            }
        }

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Rose500),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout of Admin Session")
        }
    }
}

// Dialogs for Admin CRUD

@Composable
fun CategoryFormDialog(
    category: CategoryEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, icon: String, desc: String, order: Int, iconUrl: String?) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var iconType by remember { mutableStateOf(category?.iconType ?: "grocery") }
    var iconUrl by remember { mutableStateOf(category?.iconUrl ?: "") }
    var description by remember { mutableStateOf(category?.description ?: "") }
    var displayOrder by remember { mutableStateOf(category?.displayOrder?.toString() ?: "1") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        if (category == null) "Add New Category" else "Edit Category",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Slate900
                    )
                    Text(
                        "Set name, visual logo, and display order for Home Page",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                // Live Preview Badge
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Violet100.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CategoryIconBadge(
                                iconType = iconType.ifBlank { name },
                                imageUrl = iconUrl.ifBlank { null },
                                size = 56.dp
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("Customer Preview Logo", fontSize = 10.sp, color = Violet700, fontWeight = FontWeight.Bold)
                                Text(
                                    name.ifBlank { "Category Name" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Slate900
                                )
                                Text("Tag: $iconType", fontSize = 11.sp, color = Slate600)
                            }
                        }
                    }
                }

                // Presets Palette
                item {
                    Text("Quick Logo Selection:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        items(CATEGORY_PRESET_ICONS) { preset ->
                            val isSelected = iconType.equals(preset.key, ignoreCase = true)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Violet700 else Slate100,
                                modifier = Modifier.clickable {
                                    iconType = preset.key
                                    if (name.isBlank()) name = preset.title.substringBefore("&").trim()
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(preset.emoji, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        preset.title,
                                        fontSize = 11.sp,
                                        color = if (isSelected) Color.White else Slate800,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Category Name (e.g. Fashion, Grocery, Mobiles)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = iconType,
                        onValueChange = { iconType = it },
                        label = { Text("Icon / Logo Tag (e.g. mobile, grocery, beauty, clothes)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = iconUrl,
                        onValueChange = { iconUrl = it },
                        label = { Text("Logo / Image URL (Optional custom image URL)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = displayOrder,
                        onValueChange = { displayOrder = it },
                        label = { Text("Display Order (Priority on Home Page)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (name.isNotBlank()) {
                                    onSave(
                                        name.trim(),
                                        iconType.trim().ifBlank { name.lowercase().trim() },
                                        description.trim(),
                                        displayOrder.toIntOrNull() ?: 1,
                                        iconUrl.trim().ifBlank { null }
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Violet700)
                        ) {
                            Text("Save Category")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCategoryFormDialog(
    subCategory: SubCategoryEntity?,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (categoryId: Long, name: String, icon: String, order: Int, imageUrl: String?) -> Unit
) {
    var selectedCatId by remember { mutableStateOf(subCategory?.categoryId ?: categories.firstOrNull()?.id ?: 1L) }
    var name by remember { mutableStateOf(subCategory?.name ?: "") }
    var iconType by remember { mutableStateOf(subCategory?.iconType ?: "general") }
    var imageUrl by remember { mutableStateOf(subCategory?.imageUrl ?: "") }
    var displayOrder by remember { mutableStateOf(subCategory?.displayOrder?.toString() ?: "1") }

    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        if (subCategory == null) "Add Sub-Category" else "Edit Sub-Category",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Slate900
                    )
                    Text(
                        "Subcategory with logo shown under parent on Customer Home Page",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                // Live Preview Badge
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Violet100.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SubCategoryVisualBadge(
                                iconType = iconType.ifBlank { name },
                                imageUrl = imageUrl.ifBlank { null },
                                size = 56.dp
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            val parentName = categories.find { it.id == selectedCatId }?.name ?: "Parent Category"
                            Column {
                                Text("Customer Preview Logo", fontSize = 10.sp, color = Violet700, fontWeight = FontWeight.Bold)
                                Text(
                                    name.ifBlank { "SubCategory Name" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Slate900
                                )
                                Text("Parent: $parentName | Tag: $iconType", fontSize = 11.sp, color = Slate600)
                            }
                        }
                    }
                }

                // Parent Category Selector
                item {
                    Text("Select Parent Category:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate700)
                    Spacer(modifier = Modifier.height(2.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        val currentCatName = categories.find { it.id == selectedCatId }?.name ?: "Select Category"
                        OutlinedTextField(
                            value = currentCatName,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.name) },
                                    onClick = {
                                        selectedCatId = cat.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Presets Palette
                item {
                    Text("Quick Logo Selection:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        items(SUBCATEGORY_PRESET_ICONS) { preset ->
                            val isSelected = iconType.equals(preset.key, ignoreCase = true)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Violet700 else Slate100,
                                modifier = Modifier.clickable {
                                    iconType = preset.key
                                    if (name.isBlank()) name = preset.title.substringBefore("&").trim()
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(preset.emoji, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        preset.title,
                                        fontSize = 11.sp,
                                        color = if (isSelected) Color.White else Slate800,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Sub-Category Name (e.g. Shirts, Smart TVs, Dairy)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = iconType,
                        onValueChange = { iconType = it },
                        label = { Text("Icon / Visual Tag (e.g. shirt, umbrella, jeans, dairy, fruit, tv)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("Logo / Image URL (Optional custom image URL)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = displayOrder,
                        onValueChange = { displayOrder = it },
                        label = { Text("Display Order") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (name.isNotBlank()) {
                                    onSave(
                                        selectedCatId,
                                        name.trim(),
                                        iconType.trim().ifBlank { name.lowercase().trim() },
                                        displayOrder.toIntOrNull() ?: 1,
                                        imageUrl.trim().ifBlank { null }
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Violet700)
                        ) {
                            Text("Save Sub-Category")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerFormDialog(
    banner: CategoryBannerEntity?,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (categoryId: Long?, title: String, subtitle: String?, tag: String?, discountText: String?, bannerType: String, order: Int) -> Unit
) {
    var selectedCatId by remember { mutableStateOf<Long?>(banner?.categoryId) }
    var title by remember { mutableStateOf(banner?.title ?: "") }
    var subtitle by remember { mutableStateOf(banner?.subtitle ?: "") }
    var tag by remember { mutableStateOf(banner?.tag ?: "SPECIAL OFFER") }
    var discountText by remember { mutableStateOf(banner?.discountText ?: "UP TO 60% OFF") }
    var bannerType by remember { mutableStateOf(banner?.bannerType ?: "mega_sale") }
    var displayOrder by remember { mutableStateOf(banner?.displayOrder?.toString() ?: "1") }

    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color.White, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(if (banner == null) "Add Mega Sale Banner" else "Edit Banner", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Assign Category (or General Home Slider):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    val assignedName = if (selectedCatId == null) "General Home Slider (All)" else (categories.find { it.id == selectedCatId }?.name ?: "Category #$selectedCatId")
                    OutlinedTextField(
                        value = assignedName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("General Home Slider (All)") },
                            onClick = {
                                selectedCatId = null
                                expanded = false
                            }
                        )
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name) },
                                onClick = {
                                    selectedCatId = cat.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Banner Title (e.g. MEGA SALE)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = discountText, onValueChange = { discountText = it }, label = { Text("Discount Text (e.g. UP TO 70% OFF)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("Subtitle") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(selectedCatId, title, subtitle, tag, discountText, bannerType, displayOrder.toIntOrNull() ?: 1)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500)
                    ) {
                        Text("Save Banner")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormDialog(
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (catId: Long, subId: Long?, name: String, icon: String, price: Double, mrp: Double, discount: Int, rating: Float, tag: String) -> Unit
) {
    var selectedCatId by remember { mutableStateOf(categories.firstOrNull()?.id ?: 1L) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("999") }
    var mrp by remember { mutableStateOf("1999") }
    var discount by remember { mutableStateOf("50") }
    var tag by remember { mutableStateOf("trending") }
    var iconType by remember { mutableStateOf("general") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color.White, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Add New Product", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Product Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price (₹)") }, singleLine = true, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = mrp, onValueChange = { mrp = it }, label = { Text("MRP (₹)") }, singleLine = true, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = tag, onValueChange = { tag = it }, label = { Text("Tag (trending, best_selling, todays_offers)") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onSave(
                                    selectedCatId,
                                    null,
                                    name,
                                    iconType,
                                    price.toDoubleOrNull() ?: 999.0,
                                    mrp.toDoubleOrNull() ?: 1999.0,
                                    discount.toIntOrNull() ?: 50,
                                    4.5f,
                                    tag
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Violet700)
                    ) {
                        Text("Add Product")
                    }
                }
            }
        }
    }
}

@Composable
fun SellersManagementTab(
    sellers: List<com.example.data.local.entity.SellerEntity>,
    categories: List<CategoryEntity>,
    onAddSeller: (
        sellerName: String,
        businessName: String,
        mobile: String,
        email: String,
        businessAddress: String,
        storeInfo: String,
        assignedCategoryIds: List<Long>,
        assignedSubCategoryIds: List<Long>
    ) -> Unit
) {
    var showAddSellerDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Verified Sellers (${sellers.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate900)
                    Text("Category permissions & merchant status", fontSize = 11.sp, color = Slate500)
                }
                Button(
                    onClick = { showAddSellerDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Seller", fontSize = 11.sp)
                }
            }
        }

        items(sellers, key = { it.id }) { seller ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(seller.businessName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            Text("Owner: ${seller.sellerName} • GSTIN: ${seller.gstNumber}", fontSize = 11.sp, color = Slate500)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFECFDF5))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(seller.verificationStatus, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Contact: +91 ${seller.mobile} | ${seller.email}", fontSize = 12.sp, color = Violet700)
                    Text("Address: ${seller.businessAddress}", fontSize = 11.sp, color = Slate600)
                }
            }
        }
    }

    if (showAddSellerDialog) {
        var sName by remember { mutableStateOf("") }
        var bName by remember { mutableStateOf("") }
        var mobile by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var address by remember { mutableStateOf("Keonjhar Main Market, Odisha - 758001") }
        var selectedCats by remember { mutableStateOf(setOf<Long>()) }

        Dialog(onDismissRequest = { showAddSellerDialog = false }) {
            Surface(shape = RoundedCornerShape(20.dp), color = Color.White, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Add New Seller", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = sName, onValueChange = { sName = it }, label = { Text("Owner Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = bName, onValueChange = { bName = it }, label = { Text("Business / Store Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = mobile, onValueChange = { mobile = it }, label = { Text("Mobile (+91)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Assign Permitted Categories:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(categories) { cat ->
                            val isSel = selectedCats.contains(cat.id)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Violet700 else Slate100)
                                    .clickable {
                                        selectedCats = if (isSel) selectedCats - cat.id else selectedCats + cat.id
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(cat.name, color = if (isSel) Color.White else Slate700, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showAddSellerDialog = false }) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = {
                                if (sName.isNotBlank() && bName.isNotBlank()) {
                                    val catList = if (selectedCats.isEmpty()) categories.map { it.id } else selectedCats.toList()
                                    onAddSeller(sName, bName, mobile, email, address, "Authorized Seller", catList, emptyList())
                                    showAddSellerDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Violet700)
                        ) {
                            Text("Create Seller")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminApprovalsCenterTab(
    pendingSellers: List<com.example.data.local.entity.SellerEntity>,
    pendingDeliveryBoys: List<com.example.data.local.entity.DeliveryPartnerEntity>,
    pendingProducts: List<ProductEntity>,
    onApproveSeller: (com.example.data.local.entity.SellerEntity) -> Unit,
    onRejectSeller: (com.example.data.local.entity.SellerEntity, String) -> Unit,
    onApproveDeliveryBoy: (com.example.data.local.entity.DeliveryPartnerEntity) -> Unit,
    onRejectDeliveryBoy: (com.example.data.local.entity.DeliveryPartnerEntity) -> Unit,
    onApproveProduct: (ProductEntity) -> Unit,
    onRejectProduct: (ProductEntity, String) -> Unit
) {
    var selectedApprovalType by remember {
        mutableStateOf(
            if (pendingSellers.isNotEmpty()) "SELLERS"
            else if (pendingDeliveryBoys.isNotEmpty()) "DELIVERY_BOYS"
            else "PRODUCTS"
        )
    }

    var sellerToReject by remember { mutableStateOf<com.example.data.local.entity.SellerEntity?>(null) }
    var sellerRejectionReason by remember { mutableStateOf("Incomplete documentation or unverified business location") }

    var productToReject by remember { mutableStateOf<ProductEntity?>(null) }
    var productRejectionReason by remember { mutableStateOf("Invalid pricing or specifications") }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            val totalAllPending = pendingSellers.size + pendingDeliveryBoys.size + pendingProducts.size
            Text("Admin Approvals Center ($totalAllPending Pending)", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Slate900)
            Text("Review and approve merchant sign-ups, delivery rider applications, and product listings", fontSize = 11.sp, color = Slate500)
        }

        // Sub-filter tabs
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = selectedApprovalType == "SELLERS",
                    onClick = { selectedApprovalType = "SELLERS" },
                    label = { Text("🏪 Sellers (${pendingSellers.size})", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Violet700,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = selectedApprovalType == "DELIVERY_BOYS",
                    onClick = { selectedApprovalType = "DELIVERY_BOYS" },
                    label = { Text("🛵 Delivery Boys (${pendingDeliveryBoys.size})", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD97706),
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = selectedApprovalType == "PRODUCTS",
                    onClick = { selectedApprovalType = "PRODUCTS" },
                    label = { Text("📦 Products (${pendingProducts.size})", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2563EB),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        when (selectedApprovalType) {
            "SELLERS" -> {
                if (pendingSellers.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("No Pending Seller Applications", fontWeight = FontWeight.Bold, color = Slate800)
                                Text("All merchant sign-ups have been reviewed and approved!", fontSize = 12.sp, color = Slate500, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                } else {
                    items(pendingSellers, key = { it.id }) { seller ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(seller.businessName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
                                        Text("Owner: ${seller.sellerName}", fontSize = 12.sp, color = Slate600)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFFEF3C7))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("PENDING APPROVAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = Slate100)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = Violet700, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("+91 ${seller.mobile}", fontSize = 12.sp, color = Slate700, fontWeight = FontWeight.Medium)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Icon(Icons.Default.Email, contentDescription = null, tint = Violet700, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(seller.email, fontSize = 12.sp, color = Slate700)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Slate500, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Address: ${seller.businessAddress}", fontSize = 11.sp, color = Slate600)
                                }

                                if (seller.gstNumber.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Badge, contentDescription = null, tint = Slate500, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("GST/License: ${seller.gstNumber}", fontSize = 11.sp, color = Slate600)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    OutlinedButton(
                                        onClick = { sellerToReject = seller },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Rose500),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Reject", fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { onApproveSeller(seller) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Approve & Activate Store", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            "DELIVERY_BOYS" -> {
                if (pendingDeliveryBoys.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("No Pending Delivery Boy Applications", fontWeight = FontWeight.Bold, color = Slate800)
                                Text("All delivery fleet applications have been processed!", fontSize = 12.sp, color = Slate500, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                } else {
                    items(pendingDeliveryBoys, key = { it.id }) { boy ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(boy.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
                                        Text("Rider Hub: ${boy.assignedHub}", fontSize = 12.sp, color = Color(0xFFD97706), fontWeight = FontWeight.Medium)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFFEF3C7))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("UNVERIFIED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = Slate100)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("+91 ${boy.mobile}", fontSize = 12.sp, color = Slate700, fontWeight = FontWeight.Medium)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(boy.email, fontSize = 12.sp, color = Slate700)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = Slate500, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Vehicle: ${boy.vehicleType} (${boy.vehicleNumber})", fontSize = 11.sp, color = Slate700, fontWeight = FontWeight.SemiBold)
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Badge, contentDescription = null, tint = Slate500, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("DL Number: ${boy.licenseNumber}", fontSize = 11.sp, color = Slate700)
                                }

                                if (!boy.emergencyContact.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Phone, contentDescription = null, tint = Slate400, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Emergency: ${boy.emergencyContact}", fontSize = 11.sp, color = Slate500)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    OutlinedButton(
                                        onClick = { onRejectDeliveryBoy(boy) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Rose500),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Reject", fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { onApproveDeliveryBoy(boy) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Approve & Activate Rider", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            "PRODUCTS" -> {
                if (pendingProducts.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("No Pending Product Submissions", fontWeight = FontWeight.Bold, color = Slate800)
                                Text("All seller product listings have been reviewed and approved!", fontSize = 12.sp, color = Slate500, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                } else {
                    items(pendingProducts, key = { it.id }) { product ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                                        Text("Brand: ${product.brand} | Stock: ${product.stock} units", fontSize = 11.sp, color = Slate500)
                                    }
                                    Text("₹${product.price.toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Violet700)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Specs: ${product.specifications}", fontSize = 11.sp, color = Slate600)
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    OutlinedButton(
                                        onClick = { productToReject = product },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Rose500)
                                    ) {
                                        Text("Reject", fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { onApproveProduct(product) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
                                    ) {
                                        Text("Approve & Go Live", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (sellerToReject != null) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { sellerToReject = null }) {
            Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Reject Merchant Application", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = sellerRejectionReason,
                        onValueChange = { sellerRejectionReason = it },
                        label = { Text("Reason for Rejection") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { sellerToReject = null }) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = {
                                onRejectSeller(sellerToReject!!, sellerRejectionReason)
                                sellerToReject = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Rose500)
                        ) {
                            Text("Confirm Reject")
                        }
                    }
                }
            }
        }
    }

    if (productToReject != null) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { productToReject = null }) {
            Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Reject Product Submission", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = productRejectionReason,
                        onValueChange = { productRejectionReason = it },
                        label = { Text("Rejection Reason for Merchant") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { productToReject = null }) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = {
                                onRejectProduct(productToReject!!, productRejectionReason)
                                productToReject = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Rose500)
                        ) {
                            Text("Confirm Rejection")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingLinkItem(
    title: String,
    description: String,
    linkUrl: String,
    badgeColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onOpen: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var isCopied by remember { mutableStateOf(false) }

    LaunchedEffect(isCopied) {
        if (isCopied) {
            kotlinx.coroutines.delay(2000)
            isCopied = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
        }
        Text(description, fontSize = 11.sp, color = Slate500, modifier = Modifier.padding(start = 26.dp, top = 2.dp))

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Slate50)
                .border(1.dp, Slate200, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = linkUrl,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Violet700,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(linkUrl))
                        isCopied = true
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        if (isCopied) Icons.Default.CheckCircle else Icons.Default.ContentCopy,
                        contentDescription = "Copy Link",
                        tint = if (isCopied) Color(0xFF059669) else Slate600,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Sampurna Partner Registration Link")
                            putExtra(Intent.EXTRA_TEXT, "Join Sampurna Keonjhar partner network: $linkUrl")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Registration Link via"))
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share Link", tint = Slate600, modifier = Modifier.size(16.dp))
                }

                IconButton(
                    onClick = onOpen,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.OpenInNew, contentDescription = "Open Sign Up Form", tint = badgeColor, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun CommissionSettingsTab(
    commissionRate: Double,
    onUpdateCommission: (Double) -> Unit
) {
    var rateInput by remember { mutableStateOf(commissionRate.toString()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Sampurna Platform Commission Configuration", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Slate900)
        Text("Controls the automatic platform fee added to all seller products (Standard = 2.0%).", fontSize = 12.sp, color = Slate500)

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Current Active Commission: $commissionRate%", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Violet700)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = rateInput,
                    onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) rateInput = it },
                    label = { Text("Commission Percentage (%)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = {
                        val rate = rateInput.toDoubleOrNull() ?: 2.0
                        onUpdateCommission(rate)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save & Apply Platform Commission")
                }
            }
        }
    }
}

// ----------------------------------------------------
// ADMIN ORDERS & CANCELLATIONS TAB
// ----------------------------------------------------
@Composable
fun AdminOrdersTab(
    orders: List<com.example.data.local.entity.OrderEntity>,
    cancellationAudits: List<com.example.data.local.entity.CustomerProductCancellationEntity>,
    onResetPenalty: (Long, Long) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredOrders = remember(orders, selectedFilter) {
        when (selectedFilter) {
            "PLACED" -> orders.filter { it.orderStatus == "ORDER_PLACED" }
            "PROCESSING" -> orders.filter { it.orderStatus == "SELLER_PROCESSING" }
            "READY" -> orders.filter { it.orderStatus == "READY_FOR_PICKUP" }
            "DELIVERED" -> orders.filter { it.orderStatus == "DELIVERED" }
            "CANCELLED" -> orders.filter { it.orderStatus.startsWith("CANCELLED") }
            else -> orders
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Global Order Management & Audits", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Slate900)
            Text("Real-time view of orders across Keonjhar sellers with cancellation tracking.", fontSize = 12.sp, color = Slate500)
        }

        // Summary Metric Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminStatCard(title = "Total Orders", value = orders.size.toString(), color = Violet700, modifier = Modifier.weight(1f))
                AdminStatCard(title = "Delivered", value = orders.count { it.orderStatus == "DELIVERED" }.toString(), color = Emerald700, modifier = Modifier.weight(1f))
                AdminStatCard(title = "Cancelled", value = orders.count { it.orderStatus.startsWith("CANCELLED") }.toString(), color = Rose600, modifier = Modifier.weight(1f))
            }
        }

        // Repeated Cancellation Penalty Audit Section
        if (cancellationAudits.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Customer Cancellation Penalty Audits (1% Rule)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Slate900
                        )
                        Text(
                            text = "Customers who cancelled the same item > 2 times have an automatic 1% price adjustment.",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        cancellationAudits.forEach { audit ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Customer #${audit.customerId} • Product #${audit.productId}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate800)
                                    Text("Reason: ${audit.cancellationReason} • Adjustment: ${audit.adjustmentRatePercent}% • Overridden: ${if (audit.isAdminOverridden) "YES" else "NO"}", fontSize = 11.sp, color = if (audit.adjustmentRatePercent > 0) Rose600 else Slate500)
                                }
                                OutlinedButton(
                                    onClick = { onResetPenalty(audit.customerId, audit.productId) },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("Reset Penalty", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Filter chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filters = listOf("ALL" to "All (${orders.size})", "PLACED" to "Placed", "PROCESSING" to "Processing", "READY" to "Ready", "DELIVERED" to "Delivered", "CANCELLED" to "Cancelled")
                items(filters) { (key, label) ->
                    val isSelected = selectedFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Violet700 else Slate200)
                            .clickable { selectedFilter = key }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) Color.White else Slate700)
                    }
                }
            }
        }

        items(filteredOrders, key = { it.id }) { order ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(order.orderNumber, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Violet900)
                            Text("Customer: ${order.customerName} (${order.customerMobile})", fontSize = 11.sp, color = Slate600)
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (order.orderStatus) {
                                "ORDER_PLACED" -> Violet100
                                "SELLER_PROCESSING" -> Amber100
                                "READY_FOR_PICKUP", "DELIVERED" -> Emerald100
                                else -> Rose100
                            }
                        ) {
                            Text(
                                text = order.orderStatus,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (order.orderStatus) {
                                    "ORDER_PLACED" -> Violet800
                                    "SELLER_PROCESSING" -> Orange600
                                    "READY_FOR_PICKUP", "DELIVERED" -> Emerald800
                                    else -> Rose700
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Seller: ${order.sellerName} | Items: ${order.productCount} | Distance: ${order.distanceKm} KM", fontSize = 11.sp, color = Slate500)
                    Text("Total: ₹${"%.2f".format(order.totalAmount)} (${order.paymentMethod} - ${order.paymentStatus})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)

                    if (order.orderStatus.startsWith("CANCELLED")) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Cancellation Reason: ${order.cancelReason ?: "N/A"}", fontSize = 11.sp, color = Rose600, fontWeight = FontWeight.SemiBold)
                        Text("Refund: ${order.refundStatus}", fontSize = 10.sp, color = Slate600)
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// ADMIN SYSTEM SETTINGS TAB (Delivery, COD, Gateway)
// ----------------------------------------------------
@Composable
fun AdminSystemSettingsTab(
    onSaveDelivery: (Double, Double, Double) -> Unit,
    onSaveCod: (Boolean, Double) -> Unit,
    onSaveGateway: (String, String, String, String, Boolean) -> Unit
) {
    var baseKm by remember { mutableStateOf("5.0") }
    var baseFee by remember { mutableStateOf("10.0") }
    var extraPerKm by remember { mutableStateOf("3.0") }

    var codEnabled by remember { mutableStateOf(true) }
    var codFee by remember { mutableStateOf("10.0") }

    var selectedGateway by remember { mutableStateOf("Razorpay") }
    var keyId by remember { mutableStateOf("rzp_live_sampurna_keonjhar") }
    var keySecret by remember { mutableStateOf("sec_98a7sdf897asdf") }
    var webhookSecret by remember { mutableStateOf("whsec_389274892374") }
    var isTestMode by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("System & Operations Settings", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Slate900)
            Text("Configure delivery distance algorithms, COD rules, and payment gateways.", fontSize = 12.sp, color = Slate500)
        }

        // Delivery Charge Algorithm
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Delivery Charge Formula", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Violet900)
                    Text("Default: 0–5 KM = ₹10, Above 5 KM = ₹10 + ₹3/additional KM", fontSize = 11.sp, color = Slate500)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = baseKm,
                            onValueChange = { baseKm = it },
                            label = { Text("Base KM (KM)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = baseFee,
                            onValueChange = { baseFee = it },
                            label = { Text("Base Fee (₹)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = extraPerKm,
                            onValueChange = { extraPerKm = it },
                            label = { Text("Add/KM (₹)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            onSaveDelivery(
                                baseKm.toDoubleOrNull() ?: 5.0,
                                baseFee.toDoubleOrNull() ?: 10.0,
                                extraPerKm.toDoubleOrNull() ?: 3.0
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Delivery Algorithm")
                    }
                }
            }
        }

        // Cash on Delivery (COD) Settings
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Cash on Delivery (COD)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Violet900)
                            Text("Standard: ₹10 handling fee for COD, ₹0 for Online.", fontSize = 11.sp, color = Slate500)
                        }
                        Switch(
                            checked = codEnabled,
                            onCheckedChange = { codEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Violet700)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = codFee,
                        onValueChange = { codFee = it },
                        label = { Text("COD Handling Fee (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            onSaveCod(codEnabled, codFee.toDoubleOrNull() ?: 10.0)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save COD Settings")
                    }
                }
            }
        }

        // Payment Gateway Credentials
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Payment Gateway Configuration", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Violet900)
                    Text("Backend-managed credentials. Secrets are never exposed to client.", fontSize = 11.sp, color = Slate500)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Razorpay", "Cashfree", "Paytm", "PhonePe").forEach { gw ->
                            val isSel = selectedGateway == gw
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Violet700 else Slate100)
                                    .clickable { selectedGateway = gw }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(gw, fontSize = 11.sp, color = if (isSel) Color.White else Slate700, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = keyId,
                        onValueChange = { keyId = it },
                        label = { Text("API Key / Client ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = keySecret,
                        onValueChange = { keySecret = it },
                        label = { Text("API Secret Key (Backend)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = webhookSecret,
                        onValueChange = { webhookSecret = it },
                        label = { Text("Webhook Signing Secret") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sandbox / Test Mode", fontSize = 12.sp, color = Slate700)
                        Switch(
                            checked = isTestMode,
                            onCheckedChange = { isTestMode = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Violet700)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            onSaveGateway(selectedGateway, keyId, keySecret, webhookSecret, isTestMode)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Gateway Configuration")
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = color)
            Text(title, fontSize = 10.sp, color = Slate500)
        }
    }
}


