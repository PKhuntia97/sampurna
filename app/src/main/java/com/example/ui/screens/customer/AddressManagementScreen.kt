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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.AddressEntity
import com.example.ui.theme.SampurnaDarkPurple
import com.example.ui.theme.SampurnaOrange
import com.example.ui.theme.SampurnaPrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressManagementScreen(
    accountViewModel: CustomerAccountViewModel,
    onNavigateBack: () -> Unit
) {
    val addresses by accountViewModel.savedAddresses.collectAsState()
    val locationState by accountViewModel.locationFetchState.collectAsState()

    var showAddAddressSheet by remember { mutableStateOf(false) }
    var addressToEdit by remember { mutableStateOf<AddressEntity?>(null) }
    var addressToDelete by remember { mutableStateOf<AddressEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Delivery Addresses",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SampurnaDarkPurple
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addressToEdit = null
                    showAddAddressSheet = true
                },
                containerColor = SampurnaOrange,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("add_address_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Address")
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = Color(0xFFF8FAFC)
        ) {
            if (addresses.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "No Address",
                        tint = Color.LightGray,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Saved Addresses",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Add your home, office, or live location for lightning-fast checkout.",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            addressToEdit = null
                            showAddAddressSheet = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add New Address")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        // GPS Quick-Detect Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    accountViewModel.fetchCurrentLocation { house, street, city, dist, state, pin, lat, lng ->
                                        accountViewModel.addAddress(
                                            name = "Pranaya Khuntia (Current Location)",
                                            mobile = "9876543210",
                                            houseFlat = house,
                                            streetArea = street,
                                            landmark = "Detected via GPS",
                                            city = city,
                                            district = dist,
                                            state = state,
                                            pinCode = pin,
                                            addressType = "HOME",
                                            setAsDefault = true,
                                            latitude = lat,
                                            longitude = lng
                                        )
                                    }
                                }
                                .testTag("gps_current_location_card"),
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
                                    Icon(
                                        imageVector = Icons.Default.MyLocation,
                                        contentDescription = "Current Location",
                                        tint = Color(0xFF2563EB),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Use Current Location",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E40AF)
                                    )
                                    Text(
                                        text = locationState ?: "Tap to auto-detect GPS coordinates & reverse geocode address",
                                        fontSize = 11.sp,
                                        color = Color(0xFF3B82F6)
                                    )
                                }
                            }
                        }
                    }

                    items(addresses, key = { it.id }) { address ->
                        AddressItemCard(
                            address = address,
                            onSetDefault = { accountViewModel.setDefaultAddress(address.id) },
                            onEdit = {
                                addressToEdit = address
                                showAddAddressSheet = true
                            },
                            onDelete = { addressToDelete = address }
                        )
                    }
                }
            }
        }
    }

    // Add / Edit Address Sheet
    if (showAddAddressSheet) {
        AddressFormSheet(
            existingAddress = addressToEdit,
            onDismiss = { showAddAddressSheet = false },
            onUseGpsLocation = { onDetected ->
                accountViewModel.fetchCurrentLocation { house, street, city, dist, state, pin, lat, lng ->
                    onDetected(house, street, city, dist, state, pin, lat, lng)
                }
            },
            onSave = { name, mobile, house, street, landmark, city, dist, state, pin, type, isDefault, lat, lng ->
                if (addressToEdit != null) {
                    accountViewModel.updateAddress(
                        addressToEdit!!.copy(
                            name = name,
                            mobile = mobile,
                            houseFlat = house,
                            streetArea = street,
                            landmark = landmark,
                            city = city,
                            district = dist,
                            state = state,
                            pinCode = pin,
                            addressType = type,
                            isDefault = isDefault,
                            latitude = lat,
                            longitude = lng
                        )
                    )
                } else {
                    accountViewModel.addAddress(
                        name = name,
                        mobile = mobile,
                        houseFlat = house,
                        streetArea = street,
                        landmark = landmark,
                        city = city,
                        district = dist,
                        state = state,
                        pinCode = pin,
                        addressType = type,
                        setAsDefault = isDefault,
                        latitude = lat,
                        longitude = lng
                    )
                }
                showAddAddressSheet = false
            }
        )
    }

    // Delete confirmation dialog
    if (addressToDelete != null) {
        AlertDialog(
            onDismissRequest = { addressToDelete = null },
            title = { Text("Delete Address?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to remove this address from your saved list?") },
            confirmButton = {
                Button(
                    onClick = {
                        accountViewModel.deleteAddress(addressToDelete!!.id)
                        addressToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { addressToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun AddressItemCard(
    address: AddressEntity,
    onSetDefault: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("address_card_${address.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp),
        border = if (address.isDefault) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SampurnaPrimaryPurple)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Type Icon & Label
                val (icon, typeColor) = when (address.addressType) {
                    "WORK" -> Icons.Default.Work to Color(0xFF2563EB)
                    "OTHER" -> Icons.Default.Apartment to Color(0xFFD97706)
                    else -> Icons.Default.Home to Color(0xFF10B981)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(typeColor.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = icon, contentDescription = null, tint = typeColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = address.addressType,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = typeColor
                        )
                    }
                }

                if (address.isDefault) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SampurnaPrimaryPurple.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "DEFAULT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SampurnaPrimaryPurple
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = address.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = "+91 ${address.mobile}",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${address.houseFlat}, ${address.streetArea}${if (address.landmark.isNotBlank()) ", Near ${address.landmark}" else ""}",
                fontSize = 13.sp,
                color = Color(0xFF334155),
                lineHeight = 18.sp
            )
            Text(
                text = "${address.city}, ${address.district}, ${address.state} - ${address.pinCode}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF475569)
            )

            if (!address.isDefault) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onSetDefault,
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Set as Default Address", fontSize = 12.sp, color = SampurnaPrimaryPurple)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddressFormSheet(
    existingAddress: AddressEntity?,
    onDismiss: () -> Unit,
    onUseGpsLocation: (onDetected: (house: String, street: String, city: String, dist: String, state: String, pin: String, lat: Double, lng: Double) -> Unit) -> Unit,
    onSave: (
        name: String,
        mobile: String,
        house: String,
        street: String,
        landmark: String,
        city: String,
        dist: String,
        state: String,
        pin: String,
        type: String,
        isDefault: Boolean,
        lat: Double?,
        lng: Double?
    ) -> Unit
) {
    var name by remember { mutableStateOf(existingAddress?.name ?: "Pranaya Khuntia") }
    var mobile by remember { mutableStateOf(existingAddress?.mobile ?: "9876543210") }
    var house by remember { mutableStateOf(existingAddress?.houseFlat ?: "") }
    var street by remember { mutableStateOf(existingAddress?.streetArea ?: "") }
    var landmark by remember { mutableStateOf(existingAddress?.landmark ?: "") }
    var city by remember { mutableStateOf(existingAddress?.city ?: "Keonjhar") }
    var district by remember { mutableStateOf(existingAddress?.district ?: "Kendujhar") }
    var state by remember { mutableStateOf(existingAddress?.state ?: "Odisha") }
    var pin by remember { mutableStateOf(existingAddress?.pinCode ?: "758001") }
    var addressType by remember { mutableStateOf(existingAddress?.addressType ?: "HOME") }
    var isDefault by remember { mutableStateOf(existingAddress?.isDefault ?: false) }
    var latitude by remember { mutableStateOf(existingAddress?.latitude) }
    var longitude by remember { mutableStateOf(existingAddress?.longitude) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = if (existingAddress != null) "Edit Address" else "Add New Delivery Address",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SampurnaDarkPurple
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Use GPS Auto-fill button
            OutlinedButton(
                onClick = {
                    onUseGpsLocation { gHouse, gStreet, gCity, gDist, gState, gPin, gLat, gLng ->
                        house = gHouse
                        street = gStreet
                        city = gCity
                        district = gDist
                        state = gState
                        pin = gPin
                        latitude = gLat
                        longitude = gLng
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.MyLocation, contentDescription = null, tint = SampurnaPrimaryPurple)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Auto-Fill using Current GPS Location", color = SampurnaPrimaryPurple)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Contact Info
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Receiver Full Name *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = mobile,
                onValueChange = { if (it.length <= 10) mobile = it },
                label = { Text("10-Digit Mobile Number *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Location Info
            OutlinedTextField(
                value = pin,
                onValueChange = { if (it.length <= 6) pin = it },
                label = { Text("PIN Code *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = house,
                onValueChange = { house = it },
                label = { Text("Flat / House No. / Building Name *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = street,
                onValueChange = { street = it },
                label = { Text("Street / Road / Area *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = landmark,
                onValueChange = { landmark = it },
                label = { Text("Landmark (Optional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("State") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Address Type Selection
            Text(text = "Address Type", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("HOME", "WORK", "OTHER").forEach { type ->
                    val isSelected = addressType == type
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) SampurnaPrimaryPurple else Color(0xFFF1F5F9))
                            .clickable { addressType = type }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = type,
                            color = if (isSelected) Color.White else Color(0xFF475569),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isDefault,
                    onCheckedChange = { isDefault = it },
                    colors = CheckboxDefaults.colors(checkedColor = SampurnaPrimaryPurple)
                )
                Text(text = "Make this my default delivery address", fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && mobile.isNotBlank() && house.isNotBlank() && street.isNotBlank() && pin.isNotBlank()) {
                        onSave(name, mobile, house, street, landmark, city, district, state, pin, addressType, isDefault, latitude, longitude)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SampurnaPrimaryPurple)
            ) {
                Text("Save Address", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
