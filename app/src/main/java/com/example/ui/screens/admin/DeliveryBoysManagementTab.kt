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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.data.local.entity.DeliveryPartnerEntity
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Orange500
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Violet100
import com.example.ui.theme.Violet700
import com.example.ui.theme.Violet900

enum class DeliveryBoyFilter {
    ALL,
    ON_DUTY,
    OFF_DUTY,
    ACTIVE,
    SUSPENDED
}

@Composable
fun DeliveryBoysManagementTab(
    deliveryBoys: List<DeliveryPartnerEntity>,
    onCreateDeliveryBoy: (
        name: String,
        mobile: String,
        email: String,
        password: String,
        vehicleType: String,
        vehicleNumber: String,
        licenseNumber: String,
        assignedHub: String,
        emergencyContact: String?
    ) -> Unit,
    onUpdateDeliveryBoy: (DeliveryPartnerEntity) -> Unit,
    onToggleActive: (DeliveryPartnerEntity) -> Unit,
    onToggleDuty: (DeliveryPartnerEntity) -> Unit,
    onDeleteDeliveryBoy: (DeliveryPartnerEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(DeliveryBoyFilter.ALL) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var boyToEdit by remember { mutableStateOf<DeliveryPartnerEntity?>(null) }
    var boyToDelete by remember { mutableStateOf<DeliveryPartnerEntity?>(null) }

    val onDutyCount = deliveryBoys.count { it.isOnDuty && it.isActive }
    val activeCount = deliveryBoys.count { it.isActive }
    val totalCompletedDeliveries = deliveryBoys.sumOf { it.totalDeliveries }

    val filteredBoys = deliveryBoys.filter { boy ->
        val matchesSearch = searchQuery.isBlank() ||
                boy.name.contains(searchQuery, ignoreCase = true) ||
                boy.mobile.contains(searchQuery) ||
                boy.vehicleNumber.contains(searchQuery, ignoreCase = true) ||
                boy.assignedHub.contains(searchQuery, ignoreCase = true) ||
                boy.licenseNumber.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            DeliveryBoyFilter.ALL -> true
            DeliveryBoyFilter.ON_DUTY -> boy.isOnDuty && boy.isActive
            DeliveryBoyFilter.OFF_DUTY -> !boy.isOnDuty
            DeliveryBoyFilter.ACTIVE -> boy.isActive
            DeliveryBoyFilter.SUSPENDED -> !boy.isActive
        }

        matchesSearch && matchesFilter
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Fleet Header & Quick Stats
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9D5FF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.LocalShipping,
                                    contentDescription = null,
                                    tint = Violet700,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Delivery Fleet Management",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Slate900
                                )
                            }
                            Text(
                                "Create delivery boy accounts, assign hubs & monitor duty",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }

                        Button(
                            onClick = { showCreateDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("admin_add_delivery_boy_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Delivery Boy", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Metrics Strip
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FleetMiniStat(
                            title = "Total Fleet",
                            value = "${deliveryBoys.size}",
                            color = Violet700,
                            modifier = Modifier.weight(1f)
                        )
                        FleetMiniStat(
                            title = "On Duty Now",
                            value = "$onDutyCount",
                            color = Color(0xFF059669),
                            modifier = Modifier.weight(1f)
                        )
                        FleetMiniStat(
                            title = "Active / Valid",
                            value = "$activeCount",
                            color = Color(0xFF2563EB),
                            modifier = Modifier.weight(1f)
                        )
                        FleetMiniStat(
                            title = "Deliveries Done",
                            value = "$totalCompletedDeliveries",
                            color = Orange500,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Search Bar & Filter Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by name, mobile, vehicle or hub...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Slate400, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Slate400, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_delivery_boy_search")
                )

                // Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    val filters = listOf(
                        DeliveryBoyFilter.ALL to "All (${deliveryBoys.size})",
                        DeliveryBoyFilter.ON_DUTY to "🟢 On Duty ($onDutyCount)",
                        DeliveryBoyFilter.OFF_DUTY to "⚪ Off Duty (${deliveryBoys.size - onDutyCount})",
                        DeliveryBoyFilter.ACTIVE to "Approved ($activeCount)",
                        DeliveryBoyFilter.SUSPENDED to "Suspended (${deliveryBoys.size - activeCount})"
                    )

                    items(filters) { (filter, label) ->
                        val isSelected = selectedFilter == filter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) Violet700 else Slate100)
                                .clickable { selectedFilter = filter }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Slate700
                            )
                        }
                    }
                }
            }
        }

        // Delivery Boys List
        if (filteredBoys.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "No delivery partners found",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Slate700
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Click '+ Add Delivery Boy' to create an account for your Keonjhar fleet.",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                    }
                }
            }
        } else {
            items(filteredBoys, key = { it.id }) { boy ->
                DeliveryBoyCard(
                    boy = boy,
                    onEdit = { boyToEdit = boy },
                    onToggleActive = { onToggleActive(boy) },
                    onToggleDuty = { onToggleDuty(boy) },
                    onDelete = { boyToDelete = boy }
                )
            }
        }
    }

    // Create New Delivery Boy Dialog
    if (showCreateDialog) {
        DeliveryBoyFormDialog(
            deliveryBoy = null,
            onDismiss = { showCreateDialog = false },
            onSave = { name, mobile, email, password, vType, vNum, lic, hub, emg ->
                onCreateDeliveryBoy(name, mobile, email, password, vType, vNum, lic, hub, emg)
                showCreateDialog = false
            }
        )
    }

    // Edit Delivery Boy Dialog
    boyToEdit?.let { boy ->
        DeliveryBoyFormDialog(
            deliveryBoy = boy,
            onDismiss = { boyToEdit = null },
            onSave = { name, mobile, email, password, vType, vNum, lic, hub, emg ->
                onUpdateDeliveryBoy(
                    boy.copy(
                        name = name,
                        mobile = mobile,
                        email = email,
                        passwordHash = if (password.isNotBlank()) password else boy.passwordHash,
                        vehicleType = vType,
                        vehicleNumber = vNum,
                        licenseNumber = lic,
                        assignedHub = hub,
                        emergencyContact = emg
                    )
                )
                boyToEdit = null
            }
        )
    }

    // Delete Confirmation Dialog
    boyToDelete?.let { boy ->
        AlertDialog(
            onDismissRequest = { boyToDelete = null },
            title = { Text("Delete Delivery Boy Account?", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Text(
                    "Are you sure you want to delete ${boy.name} (${boy.mobile})? All past delivery history linked to this partner will be unlinked.",
                    fontSize = 13.sp,
                    color = Slate700
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteDeliveryBoy(boy)
                        boyToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Delete Account")
                }
            },
            dismissButton = {
                TextButton(onClick = { boyToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun FleetMiniStat(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, fontSize = 9.sp, color = Slate500, maxLines = 1)
        }
    }
}

@Composable
fun DeliveryBoyCard(
    boy: DeliveryPartnerEntity,
    onEdit: () -> Unit,
    onToggleActive: () -> Unit,
    onToggleDuty: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (boy.isActive) Slate200 else Color(0xFFFECACA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Avatar, Name, Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (boy.isActive) Violet100 else Slate200),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = if (boy.isActive) Violet700 else Slate500,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            boy.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (boy.isActive) Slate900 else Slate500
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Hub: ${boy.assignedHub}",
                                fontSize = 11.sp,
                                color = Violet700,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Status Chips
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    // Active / Suspended
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (boy.isActive) Color(0xFFECFDF5) else Color(0xFFFEF2F2))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            if (boy.isActive) "APPROVED" else "SUSPENDED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (boy.isActive) Color(0xFF059669) else Color(0xFFDC2626)
                        )
                    }

                    // Online Duty
                    if (boy.isActive) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (boy.isOnDuty) Color(0xFFEFF6FF) else Slate100)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                if (boy.isOnDuty) "🟢 ON DUTY" else "⚪ OFF DUTY",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (boy.isOnDuty) Color(0xFF1D4ED8) else Slate600
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Slate100, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Details Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1.2f)) {
                    // Contact
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = Slate400, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+91 ${boy.mobile}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    }
                    if (boy.email.isNotBlank()) {
                        Text(boy.email, fontSize = 11.sp, color = Slate500, maxLines = 1)
                    }
                    if (!boy.emergencyContact.isNullOrBlank()) {
                        Text("Emergency: +91 ${boy.emergencyContact}", fontSize = 10.sp, color = Slate500)
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "${boy.vehicleType}: ${if (boy.vehicleNumber.isNotBlank()) boy.vehicleNumber else "No Reg Number"}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Slate800
                    )
                    if (boy.licenseNumber.isNotBlank()) {
                        Text("DL: ${boy.licenseNumber}", fontSize = 10.sp, color = Slate500)
                    }
                    // Deliveries & Rating
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Orange500, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("${boy.rating}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("• ${boy.totalDeliveries} orders", fontSize = 11.sp, color = Slate600)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Quick Duty Toggle Switch
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = boy.isOnDuty,
                        onCheckedChange = { onToggleDuty() },
                        enabled = boy.isActive,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF059669)
                        ),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (boy.isOnDuty) "On Duty" else "Off Duty",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (boy.isOnDuty) Color(0xFF059669) else Slate500
                    )
                }

                // Edit, Suspend/Activate, Delete buttons
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Activate / Suspend Toggle Button
                    OutlinedButton(
                        onClick = onToggleActive,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (boy.isActive) Color(0xFFDC2626) else Color(0xFF059669)
                        )
                    ) {
                        Text(
                            if (boy.isActive) "Suspend" else "Activate",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Edit
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Violet700, modifier = Modifier.size(16.dp))
                    }

                    // Delete
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryBoyFormDialog(
    deliveryBoy: DeliveryPartnerEntity?,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        mobile: String,
        email: String,
        password: String,
        vehicleType: String,
        vehicleNumber: String,
        licenseNumber: String,
        assignedHub: String,
        emergencyContact: String?
    ) -> Unit
) {
    var name by remember { mutableStateOf(deliveryBoy?.name ?: "") }
    var mobile by remember { mutableStateOf(deliveryBoy?.mobile ?: "") }
    var email by remember { mutableStateOf(deliveryBoy?.email ?: "") }
    var password by remember { mutableStateOf(deliveryBoy?.passwordHash ?: "delivery123") }
    var vehicleType by remember { mutableStateOf(deliveryBoy?.vehicleType ?: "Bike") }
    var vehicleNumber by remember { mutableStateOf(deliveryBoy?.vehicleNumber ?: "") }
    var licenseNumber by remember { mutableStateOf(deliveryBoy?.licenseNumber ?: "") }
    var assignedHub by remember { mutableStateOf(deliveryBoy?.assignedHub ?: "Keonjhar Central Hub") }
    var emergencyContact by remember { mutableStateOf(deliveryBoy?.emergencyContact ?: "") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val vehicleTypes = listOf("Bike", "Scooter", "Electric Bike", "Van / Auto", "Bicycle")
    val popularHubs = listOf(
        "Keonjhar Central Hub",
        "Anandapur Hub",
        "Barbil & Joda Hub",
        "Ghatagaon Tarini Hub",
        "Champua Hub",
        "Ghasipura Hub"
    )

    var vehicleDropdownExpanded by remember { mutableStateOf(false) }
    var hubDropdownExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (deliveryBoy == null) "Create Delivery Boy Account" else "Edit Delivery Boy Account",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Slate900
                )
                Text(
                    text = "Fill in partner details to enable Keonjhar order delivery assignment",
                    fontSize = 11.sp,
                    color = Slate500
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (errorMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF2F2))
                            .padding(8.dp)
                    ) {
                        Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Full Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; errorMessage = null },
                    label = { Text("Full Name *") },
                    placeholder = { Text("e.g. Rakesh Kumar Nayak") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mobile & Email
                OutlinedTextField(
                    value = mobile,
                    onValueChange = {
                        if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                            mobile = it
                            errorMessage = null
                        }
                    },
                    label = { Text("Mobile Number (+91) *") },
                    placeholder = { Text("10-digit mobile") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    placeholder = { Text("e.g. rakesh@sampurna.in") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Login Password / PIN *") },
                    placeholder = { Text("Password for delivery app login") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Slate100, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))

                // Vehicle Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = vehicleDropdownExpanded,
                    onExpandedChange = { vehicleDropdownExpanded = !vehicleDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = vehicleType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Vehicle Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = vehicleDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = vehicleDropdownExpanded,
                        onDismissRequest = { vehicleDropdownExpanded = false }
                    ) {
                        vehicleTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    vehicleType = type
                                    vehicleDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Vehicle Registration Number
                OutlinedTextField(
                    value = vehicleNumber,
                    onValueChange = { vehicleNumber = it.uppercase() },
                    label = { Text("Vehicle Registration No.") },
                    placeholder = { Text("e.g. OD-09-AF-2481") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Driving License Number
                OutlinedTextField(
                    value = licenseNumber,
                    onValueChange = { licenseNumber = it.uppercase() },
                    label = { Text("Driving License (DL) No.") },
                    placeholder = { Text("e.g. OD0920210045231") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Assigned Hub Dropdown
                ExposedDropdownMenuBox(
                    expanded = hubDropdownExpanded,
                    onExpandedChange = { hubDropdownExpanded = !hubDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = assignedHub,
                        onValueChange = { assignedHub = it },
                        label = { Text("Assigned Delivery Hub") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = hubDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = hubDropdownExpanded,
                        onDismissRequest = { hubDropdownExpanded = false }
                    ) {
                        popularHubs.forEach { hub ->
                            DropdownMenuItem(
                                text = { Text(hub) },
                                onClick = {
                                    assignedHub = hub
                                    hubDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Emergency Contact
                OutlinedTextField(
                    value = emergencyContact,
                    onValueChange = {
                        if (it.length <= 10 && it.all { char -> char.isDigit() }) emergencyContact = it
                    },
                    label = { Text("Emergency Contact Number") },
                    placeholder = { Text("Family / Guardian mobile") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Slate600)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                errorMessage = "Please enter the delivery partner's name."
                                return@Button
                            }
                            if (mobile.length < 10) {
                                errorMessage = "Please enter a valid 10-digit mobile number."
                                return@Button
                            }
                            onSave(
                                name,
                                mobile,
                                if (email.isBlank()) "${name.lowercase().replace(" ", "")}.delivery@sampurna.in" else email,
                                password,
                                vehicleType,
                                vehicleNumber,
                                licenseNumber,
                                assignedHub,
                                emergencyContact.ifBlank { null }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (deliveryBoy == null) "Create Account" else "Save Changes")
                    }
                }
            }
        }
    }
}
