package com.example.ui.screens.seller

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SellerEntity
import com.example.data.local.entity.SellerProductEntity
import com.example.data.local.entity.SubCategoryEntity
import com.example.ui.theme.SampurnaDarkPurple
import com.example.ui.theme.SampurnaOrange
import com.example.ui.theme.SampurnaPrimaryPurple
import kotlinx.coroutines.launch

@Composable
fun SellerLoginScreen(
    sellerViewModel: SellerViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSignup: () -> Unit = {}
) {
    var email by remember { mutableStateOf("seller1@sampurna.com") }
    var password by remember { mutableStateOf("seller123") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val authState by sellerViewModel.sellerAuthState.collectAsState()

    if (authState is SellerAuthState.Authenticated) {
        onLoginSuccess()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(SampurnaPrimaryPurple),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = "Seller Portal",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sampurna Merchant Hub",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SampurnaDarkPurple
            )
            Text(
                text = "Partner Portal for Verified Sellers & Merchants",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Merchant Login",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (authState is SellerAuthState.Error) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = (authState as SellerAuthState.Error).message,
                                    fontSize = 12.sp,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            sellerViewModel.clearError()
                        },
                        label = { Text("Registered Merchant Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SampurnaPrimaryPurple) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("seller_email_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            sellerViewModel.clearError()
                        },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SampurnaPrimaryPurple) },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("seller_password_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { sellerViewModel.loginSeller(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("seller_login_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                    ) {
                        Text("Access Seller Dashboard", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onNavigateToSignup,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("seller_register_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SampurnaPrimaryPurple)
                    ) {
                        Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("New Merchant? Register Your Store", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Switch Demo Credentials
                    Text(
                        text = "Demo Accounts (Tap to switch):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                email = "seller1@sampurna.com"
                                password = "seller123"
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Keonjhar Tech", fontSize = 11.sp, maxLines = 1)
                        }
                        OutlinedButton(
                            onClick = {
                                email = "seller2@sampurna.com"
                                password = "seller123"
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Odisha Mart", fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardScreen(
    sellerViewModel: SellerViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val currentSeller by sellerViewModel.currentSeller.collectAsState()
    val assignedCategories by sellerViewModel.assignedCategories.collectAsState()
    val sellerProducts by sellerViewModel.sellerProducts.collectAsState()
    val allCatalogProducts by sellerViewModel.availableCatalogProducts.collectAsState()
    val message by sellerViewModel.submissionMessage.collectAsState()

    var selectedDashboardTab by remember { mutableStateOf("OVERVIEW") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (message != null) {
        androidx.compose.runtime.LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message!!)
            sellerViewModel.clearMessage()
        }
    }

    val tabs = listOf(
        "OVERVIEW" to "Overview",
        "ORDERS" to "Orders",
        "MY_PRODUCTS" to "My Listings",
        "ADD_PRODUCT" to "Add Product",
        "SELL_EXISTING" to "Sell Existing",
        "COMMISSION" to "2% Pricing"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = currentSeller?.businessName ?: "Seller Dashboard",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            text = "GSTIN: ${currentSeller?.gstNumber ?: "21AAACR5489Q1Z8"}",
                            fontSize = 11.sp,
                            color = Color(0xFFE2E8F0)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        sellerViewModel.logout()
                        onLogout()
                    }) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SampurnaDarkPurple
                )
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = Color(0xFFF8FAFC)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Navigation Tabs
                ScrollableTabRow(
                    selectedTabIndex = tabs.indexOfFirst { it.first == selectedDashboardTab }.coerceAtLeast(0),
                    containerColor = Color.White,
                    contentColor = SampurnaPrimaryPurple,
                    indicator = { tabPositions ->
                        val index = tabs.indexOfFirst { it.first == selectedDashboardTab }.coerceAtLeast(0)
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                            color = SampurnaPrimaryPurple
                        )
                    },
                    edgePadding = 16.dp
                ) {
                    tabs.forEach { (tabKey, title) ->
                        val isSelected = selectedDashboardTab == tabKey
                        Tab(
                            selected = isSelected,
                            onClick = { selectedDashboardTab = tabKey },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (isSelected) SampurnaPrimaryPurple else Color(0xFF64748B)
                                )
                            }
                        )
                    }
                }

                // Tab Content
                when (selectedDashboardTab) {
                    "OVERVIEW" -> SellerOverviewTab(
                        seller = currentSeller,
                        assignedCategories = assignedCategories,
                        sellerProducts = sellerProducts,
                        onAddProductClick = { selectedDashboardTab = "ADD_PRODUCT" },
                        onViewOrdersClick = { selectedDashboardTab = "ORDERS" }
                    )
                    "ORDERS" -> SellerOrdersManagementTab(
                        sellerViewModel = sellerViewModel
                    )
                    "MY_PRODUCTS" -> SellerMyProductsTab(
                        sellerProducts = sellerProducts,
                        sellerViewModel = sellerViewModel,
                        onAddNew = { selectedDashboardTab = "ADD_PRODUCT" }
                    )
                    "ADD_PRODUCT" -> SellerAddProductTab(
                        sellerViewModel = sellerViewModel,
                        assignedCategories = assignedCategories,
                        onSuccess = { selectedDashboardTab = "MY_PRODUCTS" }
                    )
                    "SELL_EXISTING" -> SellerSellExistingProductTab(
                        sellerViewModel = sellerViewModel,
                        availableCatalogProducts = allCatalogProducts,
                        assignedCategories = assignedCategories,
                        onSuccess = { selectedDashboardTab = "MY_PRODUCTS" }
                    )
                    "COMMISSION" -> SellerCommissionBreakdownTab(
                        sellerViewModel = sellerViewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun SellerOverviewTab(
    seller: SellerEntity?,
    assignedCategories: List<CategoryEntity>,
    sellerProducts: List<ProductEntity>,
    onAddProductClick: () -> Unit,
    onViewOrdersClick: () -> Unit = {}
) {
    val liveCount = sellerProducts.count { it.status.equals("ACTIVE", ignoreCase = true) || it.status.equals("APPROVED", ignoreCase = true) }
    val pendingCount = sellerProducts.count { it.status.equals("PENDING_APPROVAL", ignoreCase = true) }
    val rejectedCount = sellerProducts.count { it.status.equals("REJECTED", ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Merchant Status Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Merchant Status",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFECFDF5))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = seller?.verificationStatus ?: "VERIFIED",
                            color = Color(0xFF059669),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Admin-Assigned Categories:",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    assignedCategories.forEach { cat ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF3E8FF))
                                .border(1.dp, Color(0xFFE9D5FF), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = cat.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SampurnaDarkPurple
                            )
                        }
                    }
                }
            }
        }

        // Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                title = "Live Items",
                value = "$liveCount",
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Pending Review",
                value = "$pendingCount",
                icon = Icons.Default.HourglassEmpty,
                color = SampurnaOrange,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Total Listings",
                value = "${sellerProducts.size}",
                icon = Icons.Default.Inventory,
                color = SampurnaPrimaryPurple,
                modifier = Modifier.weight(1f)
            )
        }

        // Commission Highlight Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDBEAFE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Percent, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Sampurna 2% Fixed Platform Commission",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E40AF)
                    )
                    Text(
                        text = "Customer Final Price = Seller Price + 2%. You receive 100% of your listed seller price on dispatch!",
                        fontSize = 11.sp,
                        color = Color(0xFF3B82F6),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Quick Action Button
        Button(
            onClick = onAddProductClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("List New Product for Review", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SellerMyProductsTab(
    sellerProducts: List<ProductEntity>,
    sellerViewModel: SellerViewModel,
    onAddNew: () -> Unit
) {
    var statusFilter by remember { mutableStateOf("ALL") }

    val filteredList = when (statusFilter) {
        "ALL" -> sellerProducts
        else -> sellerProducts.filter { it.status.equals(statusFilter, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Status Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL" to "All", "ACTIVE" to "Active", "PENDING_APPROVAL" to "Pending", "REJECTED" to "Rejected").forEach { (key, label) ->
                val isSelected = statusFilter == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) SampurnaPrimaryPurple else Color(0xFFF1F5F9))
                        .clickable { statusFilter = key }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.White else Color(0xFF475569),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Inventory, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(54.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("No Products Found", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onAddNew, colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)) {
                    Text("Add First Product")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    SellerProductCard(item = item, sellerViewModel = sellerViewModel)
                }
            }
        }
    }
}

@Composable
private fun SellerProductCard(
    item: ProductEntity,
    sellerViewModel: SellerViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Status Badge
                val statusColor = when (item.status.uppercase()) {
                    "ACTIVE", "APPROVED", "LIVE" -> Color(0xFF059669)
                    "REJECTED" -> Color(0xFFDC2626)
                    else -> SampurnaOrange
                }
                val statusBg = when (item.status.uppercase()) {
                    "ACTIVE", "APPROVED", "LIVE" -> Color(0xFFECFDF5)
                    "REJECTED" -> Color(0xFFFEF2F2)
                    else -> Color(0xFFFFF7ED)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = item.status.replace('_', ' ').uppercase(),
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Stock: ${item.stock} pcs",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Pricing details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Your Seller Price", fontSize = 10.sp, color = Color.Gray)
                    Text("₹${item.sellerPrice.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SampurnaDarkPurple)
                }
                Column {
                    Text("+2% Platform", fontSize = 10.sp, color = Color.Gray)
                    Text("₹${(item.price - item.sellerPrice).toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SampurnaOrange)
                }
                Column {
                    Text("Final Customer Price", fontSize = 10.sp, color = Color.Gray)
                    Text("₹${item.price.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                }
            }

            // Rejection reason display if rejected
            if (item.status.equals("REJECTED", ignoreCase = true) && !item.rejectionReason.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Admin Reason: ${item.rejectionReason}",
                            fontSize = 11.sp,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SellerAddProductTab(
    sellerViewModel: SellerViewModel,
    assignedCategories: List<CategoryEntity>,
    onSuccess: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf<CategoryEntity?>(assignedCategories.firstOrNull()) }
    var subCategories by remember { mutableStateOf<List<SubCategoryEntity>>(emptyList()) }
    var selectedSubCategory by remember { mutableStateOf<SubCategoryEntity?>(null) }

    var title by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var mrpStr by remember { mutableStateOf("") }
    var sellerPriceStr by remember { mutableStateOf("") }
    var stockStr by remember { mutableStateOf("25") }
    var sku by remember { mutableStateOf("SAM-${(1000..9999).random()}") }
    var description by remember { mutableStateOf("") }
    var specs by remember { mutableStateOf("Warranty: 1 Year; Origin: India") }

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var subCategoryDropdownExpanded by remember { mutableStateOf(false) }

    // Load subcategories when category changes
    androidx.compose.runtime.LaunchedEffect(selectedCategory) {
        if (selectedCategory != null) {
            sellerViewModel.fetchSubCategoriesForCategory(selectedCategory!!.id) { subs ->
                subCategories = subs
                selectedSubCategory = subs.firstOrNull()
            }
        }
    }

    // Dynamic 2% customer final price calculation
    val sellerPrice = sellerPriceStr.toDoubleOrNull() ?: 0.0
    val customerFinalPrice = sellerPrice + (sellerPrice * 0.02)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Add New Product Listing",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = SampurnaDarkPurple
        )
        Text(
            text = "Categories are strictly limited to your admin permissions.",
            fontSize = 11.sp,
            color = Color.Gray
        )

        // Category Dropdown (Strictly Assigned Categories)
        ExposedDropdownMenuBox(
            expanded = categoryDropdownExpanded,
            onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedCategory?.name ?: "Select Assigned Category",
                onValueChange = {},
                readOnly = true,
                label = { Text("Category (Admin Assigned)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            ExposedDropdownMenu(
                expanded = categoryDropdownExpanded,
                onDismissRequest = { categoryDropdownExpanded = false }
            ) {
                assignedCategories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat.name) },
                        onClick = {
                            selectedCategory = cat
                            categoryDropdownExpanded = false
                        }
                    )
                }
            }
        }

        // SubCategory Dropdown
        if (subCategories.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = subCategoryDropdownExpanded,
                onExpandedChange = { subCategoryDropdownExpanded = !subCategoryDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedSubCategory?.name ?: "Select Sub-Category",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sub-Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subCategoryDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                ExposedDropdownMenu(
                    expanded = subCategoryDropdownExpanded,
                    onDismissRequest = { subCategoryDropdownExpanded = false }
                ) {
                    subCategories.forEach { sub ->
                        DropdownMenuItem(
                            text = { Text(sub.name) },
                            onClick = {
                                selectedSubCategory = sub
                                subCategoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Title & Brand
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Product Title *") },
            placeholder = { Text("e.g. Realme 12 Pro 5G (Submarine Blue, 256 GB)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Brand *") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        // Pricing Inputs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = mrpStr,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) mrpStr = it },
                label = { Text("MRP (₹) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = sellerPriceStr,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) sellerPriceStr = it },
                label = { Text("Your Seller Price (₹) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            )
        }

        // Live 2% Price Preview Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E8FF)),
            shape = RoundedCornerShape(10.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Your Net Earnings:", fontSize = 12.sp, color = SampurnaDarkPurple)
                    Text("₹${sellerPrice.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SampurnaDarkPurple)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("+ 2% Sampurna Platform Fee:", fontSize = 12.sp, color = SampurnaOrange)
                    Text("₹${(customerFinalPrice - sellerPrice).toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SampurnaOrange)
                }
                HorizontalDivider(color = Color(0xFFE9D5FF), modifier = Modifier.padding(vertical = 6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Customer Final Listed Price:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    Text("₹${customerFinalPrice.toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF059669))
                }
            }
        }

        // Stock & SKU
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = stockStr,
                onValueChange = { if (it.all { c -> c.isDigit() }) stockStr = it },
                label = { Text("Stock Qty *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("SKU *") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            )
        }

        // Specs & Description
        OutlinedTextField(
            value = specs,
            onValueChange = { specs = it },
            label = { Text("Key Specs (e.g. Display: AMOLED; Storage: 256GB)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Product Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Submission Buttons
        Button(
            onClick = {
                val mrp = mrpStr.toDoubleOrNull() ?: 0.0
                val stock = stockStr.toIntOrNull() ?: 10
                if (title.isNotBlank() && brand.isNotBlank() && selectedCategory != null && sellerPrice > 0) {
                    sellerViewModel.submitNewProduct(
                        categoryId = selectedCategory!!.id,
                        subCategoryId = selectedSubCategory?.id,
                        title = title,
                        brand = brand,
                        mrp = mrp,
                        sellerPrice = sellerPrice,
                        stock = stock,
                        sku = sku,
                        specifications = specs,
                        description = description.ifBlank { "High quality $brand $title certified by Sampurna." }
                    ) { success, _ ->
                        if (success) onSuccess()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
        ) {
            Text("Submit for Admin Review", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun SellerSellExistingProductTab(
    sellerViewModel: SellerViewModel,
    availableCatalogProducts: List<ProductEntity>,
    assignedCategories: List<CategoryEntity>,
    onSuccess: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedProductToSell by remember { mutableStateOf<ProductEntity?>(null) }

    val filteredCatalog = availableCatalogProducts.filter { prod ->
        assignedCategories.any { it.id == prod.categoryId } &&
                (searchQuery.isBlank() || prod.name.contains(searchQuery, ignoreCase = true) || prod.brand.contains(searchQuery, ignoreCase = true))
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Sell Existing Catalog Item",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = SampurnaDarkPurple
        )
        Text(
            text = "Choose an item from your assigned categories and provide your seller price & stock.",
            fontSize = 11.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search catalog products...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredCatalog, key = { it.id }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF3E8FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = SampurnaPrimaryPurple)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.brand, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Text(item.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Catalog Price: ₹${item.price.toInt()} (MRP ₹${item.mrp.toInt()})", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { selectedProductToSell = item },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SampurnaOrange),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Sell This", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Sell Existing Dialog
    if (selectedProductToSell != null) {
        var mySellerPrice by remember { mutableStateOf("${selectedProductToSell!!.price.toInt() - 200}") }
        var myStock by remember { mutableStateOf("15") }

        val enteredPrice = mySellerPrice.toDoubleOrNull() ?: 0.0
        val finalCustPrice = enteredPrice + (enteredPrice * 0.02)

        AlertDialog(
            onDismissRequest = { selectedProductToSell = null },
            title = { Text("List Offer for Product", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(selectedProductToSell!!.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = mySellerPrice,
                        onValueChange = { mySellerPrice = it },
                        label = { Text("Your Seller Price (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = myStock,
                        onValueChange = { myStock = it },
                        label = { Text("Your Available Stock") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Customer Price (+2% Platform): ₹${finalCustPrice.toInt()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF059669)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val stock = myStock.toIntOrNull() ?: 10
                        if (enteredPrice > 0) {
                            sellerViewModel.sellExistingCatalogProduct(
                                catalogProduct = selectedProductToSell!!,
                                sellerPrice = enteredPrice,
                                stock = stock
                            ) { success, _ ->
                                if (success) {
                                    selectedProductToSell = null
                                    onSuccess()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                ) {
                    Text("Submit Listing")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedProductToSell = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun SellerCommissionBreakdownTab(
    sellerViewModel: SellerViewModel
) {
    var testPriceStr by remember { mutableStateOf("5000") }
    val testPrice = testPriceStr.toDoubleOrNull() ?: 0.0
    val platformFee = testPrice * 0.02
    val customerPrice = testPrice + platformFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SampurnaPrimaryPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "2% Transparent Commission Model",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SampurnaDarkPurple
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Sampurna operates on a zero-hidden-fee merchant model. You set the exact price you wish to receive. We automatically add 2% at checkout, ensuring complete transparency and maximum merchant margins.",
                    fontSize = 12.sp,
                    color = Color(0xFF475569),
                    lineHeight = 18.sp
                )
            }
        }

        // Interactive Simulator
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Live Commission Simulator",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SampurnaDarkPurple
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = testPriceStr,
                    onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) testPriceStr = it },
                    label = { Text("Enter Sample Seller Price (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Your Payout (100%):", fontSize = 13.sp, color = Color(0xFF334155))
                    Text("₹${testPrice.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SampurnaDarkPurple)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Sampurna 2% Platform Fee:", fontSize = 13.sp, color = SampurnaOrange)
                    Text("₹${platformFee.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SampurnaOrange)
                }
                HorizontalDivider(color = Color(0xFFE9D5FF), modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Final Price on Customer App:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    Text("₹${customerPrice.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF059669))
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            Text(text = title, fontSize = 10.sp, color = Color.Gray, maxLines = 1)
        }
    }
}

// ----------------------------------------------------
// SELLER ORDERS MANAGEMENT TAB
// ----------------------------------------------------
@Composable
fun SellerOrdersManagementTab(
    sellerViewModel: SellerViewModel
) {
    val orders by sellerViewModel.sellerOrders.collectAsState()
    val selectedTab by sellerViewModel.selectedOrderTab.collectAsState()

    var showOutOfStockDialog by remember { mutableStateOf(false) }
    var selectedOrderForCancel by remember { mutableStateOf<com.example.data.local.entity.OrderEntity?>(null) }
    var cancelReasonText by remember { mutableStateOf("Out of stock in Keonjhar hub") }

    val filteredOrders = remember(orders, selectedTab) {
        when (selectedTab) {
            "NEW" -> orders.filter { it.orderStatus == "ORDER_PLACED" }
            "PROCESSING" -> orders.filter { it.orderStatus == "SELLER_PROCESSING" }
            "READY_FOR_PICKUP" -> orders.filter { it.orderStatus == "READY_FOR_PICKUP" }
            "COMPLETED" -> orders.filter { it.orderStatus == "DELIVERED" }
            "CANCELLED" -> orders.filter { it.orderStatus.startsWith("CANCELLED") }
            else -> orders
        }
    }

    val tabs = listOf(
        "ALL" to "All (${orders.size})",
        "NEW" to "New (${orders.count { it.orderStatus == "ORDER_PLACED" }})",
        "PROCESSING" to "Processing (${orders.count { it.orderStatus == "SELLER_PROCESSING" }})",
        "READY_FOR_PICKUP" to "Ready (${orders.count { it.orderStatus == "READY_FOR_PICKUP" }})",
        "COMPLETED" to "Delivered (${orders.count { it.orderStatus == "DELIVERED" }})",
        "CANCELLED" to "Cancelled (${orders.count { it.orderStatus.startsWith("CANCELLED") }})"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Sub-filter tabs
        ScrollableTabRow(
            selectedTabIndex = tabs.indexOfFirst { it.first == selectedTab }.coerceAtLeast(0),
            containerColor = Color.Transparent,
            contentColor = SampurnaPrimaryPurple,
            edgePadding = 0.dp
        ) {
            tabs.forEach { (key, label) ->
                val isSelected = selectedTab == key
                Tab(
                    selected = isSelected,
                    onClick = { sellerViewModel.setOrderTab(key) },
                    text = {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) SampurnaPrimaryPurple else Color(0xFF64748B)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "No orders found in this section",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF475569)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredOrders, key = { it.id }) { order ->
                    SellerOrderCard(
                        order = order,
                        onAccept = { sellerViewModel.acceptAndStartProcessing(order.id) },
                        onMarkReady = { sellerViewModel.markReadyForPickup(order.id) },
                        onMarkDelivered = { sellerViewModel.markDelivered(order.id) },
                        onOutOfStockCancel = {
                            selectedOrderForCancel = order
                            showOutOfStockDialog = true
                        }
                    )
                }
            }
        }
    }

    // Out of Stock Cancellation Dialog
    if (showOutOfStockDialog && selectedOrderForCancel != null) {
        AlertDialog(
            onDismissRequest = { showOutOfStockDialog = false },
            title = {
                Text(
                    text = "Cancel Order (Out of Stock)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Order: ${selectedOrderForCancel?.orderNumber}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Cancelling as 'Out of Stock' will immediately initiate customer refund if prepaid. This will NOT penalize the customer.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = cancelReasonText,
                        onValueChange = { cancelReasonText = it },
                        label = { Text("Reason for Unavailability") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedOrderForCancel?.let {
                            sellerViewModel.cancelOutOfStock(it.id, cancelReasonText)
                        }
                        showOutOfStockDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Confirm Out of Stock")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOutOfStockDialog = false }) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Composable
private fun SellerOrderCard(
    order: com.example.data.local.entity.OrderEntity,
    onAccept: () -> Unit,
    onMarkReady: () -> Unit,
    onMarkDelivered: () -> Unit,
    onOutOfStockCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.orderNumber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SampurnaDarkPurple
                    )
                    Text(
                        text = "Customer: ${order.customerName} (${order.customerMobile})",
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (order.orderStatus) {
                        "ORDER_PLACED" -> Color(0xFFEDE9FE)
                        "SELLER_PROCESSING" -> Color(0xFFFEF3C7)
                        "READY_FOR_PICKUP" -> Color(0xFFD1FAE5)
                        "DELIVERED" -> Color(0xFFD1FAE5)
                        else -> Color(0xFFFEE2E2)
                    }
                ) {
                    Text(
                        text = when (order.orderStatus) {
                            "ORDER_PLACED" -> "NEW ORDER"
                            "SELLER_PROCESSING" -> "PROCESSING"
                            "READY_FOR_PICKUP" -> "READY FOR PICKUP"
                            "DELIVERED" -> "DELIVERED"
                            "CANCELLED_BY_SELLER" -> "CANCELLED (STOCK)"
                            "CANCELLED_BY_CUSTOMER" -> "CANCELLED (CUSTOMER)"
                            else -> order.orderStatus
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (order.orderStatus) {
                            "ORDER_PLACED" -> Color(0xFF6D28D9)
                            "SELLER_PROCESSING" -> Color(0xFFD97706)
                            "READY_FOR_PICKUP", "DELIVERED" -> Color(0xFF059669)
                            else -> Color(0xFFDC2626)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(8.dp))

            // Address & Payment
            Text(
                text = "Delivery Address: ${order.deliveryAddressSnapshot}",
                fontSize = 11.sp,
                color = Color(0xFF64748B),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Payment: ${order.paymentMethod} (${order.paymentStatus})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (order.paymentStatus == "PAID") Color(0xFF059669) else Color(0xFFD97706)
                )

                Text(
                    text = "₹${"%.2f".format(order.totalAmount)}",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color(0xFF0F172A)
                )
            }

            // Action Buttons based on state
            if (order.orderStatus == "ORDER_PLACED") {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onOutOfStockCancel,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(40.dp)
                    ) {
                        Text("Out of Stock", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onAccept,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(40.dp)
                    ) {
                        Text("Accept & Process", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else if (order.orderStatus == "SELLER_PROCESSING") {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onMarkReady,
                    colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                ) {
                    Text("Mark Ready for Pickup", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            } else if (order.orderStatus == "READY_FOR_PICKUP") {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onMarkDelivered,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                ) {
                    Text("Mark Delivered / Dispatched", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

