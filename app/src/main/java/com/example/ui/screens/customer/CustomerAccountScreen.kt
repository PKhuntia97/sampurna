package com.example.ui.screens.customer

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.R
import com.example.ui.theme.SampurnaDarkPurple
import com.example.ui.theme.SampurnaOrange
import com.example.ui.theme.SampurnaPrimaryPurple

@Composable
fun CustomerAccountScreen(
    accountViewModel: CustomerAccountViewModel,
    onNavigateToAddresses: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToRecentlyViewed: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToOrders: () -> Unit = {},
    onNavigateToSellerPortal: () -> Unit,
    onNavigateToAdminPortal: () -> Unit,
    onLogout: () -> Unit
) {
    val user by accountViewModel.customerProfile.collectAsState()
    val wishlistItems by accountViewModel.wishlistProducts.collectAsState()
    val addresses by accountViewModel.savedAddresses.collectAsState()
    val offers by accountViewModel.activeOffers.collectAsState()
    val unreadNotifications by accountViewModel.unreadNotificationCount.collectAsState()
    val notificationPrefs by accountViewModel.notificationPreferences.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showChangeMobileDialog by remember { mutableStateOf(false) }
    var showNotificationPrefsDialog by remember { mutableStateOf(false) }
    var showAvatarSelectDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(SampurnaDarkPurple, SampurnaPrimaryPurple)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar with edit button
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(3.dp, SampurnaOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Avatar",
                                tint = SampurnaPrimaryPurple,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(SampurnaOrange)
                                .clickable { showAvatarSelectDialog = true }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Change Photo",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Header Info
                    Text(
                        text = user?.name ?: "My Account",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!user?.mobile.isNullOrBlank()) {
                            Text(
                                text = "+91 ${user?.mobile}",
                                fontSize = 13.sp,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                        if (!user?.email.isNullOrBlank()) {
                            if (!user?.mobile.isNullOrBlank()) {
                                Text(
                                    text = " • ",
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 13.sp
                                )
                            }
                            Text(
                                text = user?.email ?: "",
                                fontSize = 13.sp,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }
                }
            }

            // Quick Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickStatCard(
                    title = "Wishlist",
                    value = "${wishlistItems.size}",
                    icon = Icons.Default.Favorite,
                    color = Color(0xFFEC4899),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToWishlist
                )
                QuickStatCard(
                    title = "Addresses",
                    value = "${addresses.size}",
                    icon = Icons.Default.LocationOn,
                    color = SampurnaPrimaryPurple,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToAddresses
                )
                QuickStatCard(
                    title = "Offers",
                    value = "${offers.size}",
                    icon = Icons.Default.LocalOffer,
                    color = SampurnaOrange,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToOffers
                )
            }

            // Section 1: Orders & Shopping
            AccountSectionCard(title = "Shopping & Activity") {
                AccountMenuItem(
                    icon = Icons.Default.ShoppingBag,
                    title = "My Orders",
                    subtitle = "Track, return, or view past purchases",
                    badge = "2 Active",
                    onClick = onNavigateToOrders
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                AccountMenuItem(
                    icon = Icons.Default.Favorite,
                    title = "My Wishlist",
                    subtitle = "Saved items and price drop alerts",
                    badge = "${wishlistItems.size} items",
                    onClick = onNavigateToWishlist
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                AccountMenuItem(
                    icon = Icons.Default.History,
                    title = "Recently Viewed Products",
                    subtitle = "Quickly resume your browsing",
                    onClick = onNavigateToRecentlyViewed
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                AccountMenuItem(
                    icon = Icons.Default.LocalOffer,
                    title = "My Offers & Coupons",
                    subtitle = "Exclusive discounts, promo codes & vouchers",
                    badge = "${offers.size} available",
                    onClick = onNavigateToOffers
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Section 2: Account Settings & Addresses
            AccountSectionCard(title = "Account Details & Location") {
                AccountMenuItem(
                    icon = Icons.Default.Edit,
                    title = "Edit Profile",
                    subtitle = "Update your name and primary email",
                    onClick = { showEditProfileDialog = true }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                AccountMenuItem(
                    icon = Icons.Default.LocationOn,
                    title = "Saved Delivery Addresses",
                    subtitle = "Manage Home, Work, and GPS locations",
                    badge = "${addresses.size} saved",
                    onClick = onNavigateToAddresses
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                AccountMenuItem(
                    icon = Icons.Default.Phone,
                    title = "Change Mobile Number",
                    subtitle = "Update registered 10-digit mobile",
                    onClick = { showChangeMobileDialog = true }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                AccountMenuItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications & Alerts",
                    subtitle = "Order updates, offers, and account alerts",
                    badge = if (unreadNotifications > 0) "$unreadNotifications New" else null,
                    onClick = onNavigateToNotifications
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                AccountMenuItem(
                    icon = Icons.Default.Tune,
                    title = "Notification Preferences",
                    subtitle = "App, Email, WhatsApp, and SMS settings",
                    onClick = { showNotificationPrefsDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Section 3: Help & Information
            AccountSectionCard(title = "Help & Information") {
                AccountMenuItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    title = "24x7 Customer Support",
                    subtitle = "Toll Free: 1800-SAMPURNA (Keonjhar, Odisha)",
                    onClick = { /* Customer Help */ }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
                AccountMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Terms & Privacy Policies",
                    subtitle = "Sampurna terms of service & buyer protections",
                    onClick = { /* Terms */ }
                )
            }

            // Admin Control Dashboard (Shown only if logged in user is Admin Pranaya Khuntia)
            if (user?.email?.contains("pranaya", ignoreCase = true) == true || user?.mobile == "6370805780") {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onNavigateToAdminPortal() },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Control",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Admin Management & Controls",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "All controls: Categories, Banners, Products, Sellers, Delivery, Orders",
                                fontSize = 11.sp,
                                color = Color(0xFFB45309)
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Open",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Official Brand Stamp
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_sampurna_logo),
                            contentDescription = "Sampurna Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ସମ୍ପୂର୍ଣ୍ଣ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = SampurnaDarkPurple
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SAMPURNA",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = SampurnaDarkPurple
                            )
                        }
                        Text(
                            text = "Complete Solution • v1.0.0",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp)
                    .testTag("account_logout_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFEE2E2),
                    contentColor = Color(0xFFDC2626)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Logout from Sampurna",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var editName by remember { mutableStateOf(user?.name ?: "") }
        var editEmail by remember { mutableStateOf(user?.email ?: "") }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Profile Details", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email ID") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        accountViewModel.updateProfileInfo(editName, editEmail)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Change Mobile Dialog
    if (showChangeMobileDialog) {
        var newMobileInput by remember { mutableStateOf("") }
        var mobileError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showChangeMobileDialog = false },
            title = { Text("Change Registered Mobile", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Current Mobile: +91 ${user?.mobile ?: ""}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (mobileError != null) {
                        Text(
                            text = mobileError!!,
                            color = Color(0xFFDC2626),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                    OutlinedTextField(
                        value = newMobileInput,
                        onValueChange = {
                            if (it.length <= 10 && it.all { c -> c.isDigit() }) {
                                newMobileInput = it
                            }
                        },
                        label = { Text("New 10-Digit Mobile") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newMobileInput.length == 10) {
                            accountViewModel.changeMobileNumber(newMobileInput) { success, msg ->
                                if (success) {
                                    showChangeMobileDialog = false
                                } else {
                                    mobileError = msg
                                }
                            }
                        } else {
                            mobileError = "Enter a valid 10-digit mobile number"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                ) {
                    Text("Update Mobile")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangeMobileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Notification Preferences Dialog
    if (showNotificationPrefsDialog) {
        var appNotif by remember { mutableStateOf(notificationPrefs?.appEnabled ?: true) }
        var emailNotif by remember { mutableStateOf(notificationPrefs?.emailEnabled ?: true) }
        var whatsappNotif by remember { mutableStateOf(notificationPrefs?.whatsappEnabled ?: true) }
        var smsNotif by remember { mutableStateOf(notificationPrefs?.smsEnabled ?: false) }

        AlertDialog(
            onDismissRequest = { showNotificationPrefsDialog = false },
            title = { Text("Notification Preferences", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Choose how you would like to receive updates:", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(14.dp))

                    NotificationToggleRow(
                        title = "App Push Notifications",
                        subtitle = "Instant order alerts & flash deals",
                        checked = appNotif,
                        onCheckedChange = { appNotif = it }
                    )
                    NotificationToggleRow(
                        title = "Email Notifications",
                        subtitle = "Invoices, coupons & account security",
                        checked = emailNotif,
                        onCheckedChange = { emailNotif = it }
                    )
                    NotificationToggleRow(
                        title = "WhatsApp Notifications",
                        subtitle = "Live tracking links & delivery ETA",
                        checked = whatsappNotif,
                        onCheckedChange = { whatsappNotif = it }
                    )
                    NotificationToggleRow(
                        title = "SMS Updates",
                        subtitle = "Critical delivery SMS (Currently disabled)",
                        checked = smsNotif,
                        onCheckedChange = { smsNotif = it }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        accountViewModel.saveNotificationPreferences(appNotif, emailNotif, whatsappNotif, smsNotif)
                        showNotificationPrefsDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotificationPrefsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Avatar Selection Dialog
    if (showAvatarSelectDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarSelectDialog = false },
            title = { Text("Profile Photo", fontWeight = FontWeight.Bold) },
            text = {
                Text("Your profile photo is synced with your verified Sampurna customer account.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        accountViewModel.updateProfilePhoto("avatar_default")
                        showAvatarSelectDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                ) {
                    Text("Set Default Avatar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAvatarSelectDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun QuickStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag("account_stat_$title"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun AccountSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun AccountMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = SampurnaPrimaryPurple,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
        }
        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF7ED))
                    .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SampurnaOrange
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Open",
            tint = Color(0xFFCBD5E1),
            modifier = Modifier.size(12.dp)
        )
    }
}

@Composable
private fun NotificationToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, fontSize = 11.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = SampurnaPrimaryPurple)
        )
    }
}
