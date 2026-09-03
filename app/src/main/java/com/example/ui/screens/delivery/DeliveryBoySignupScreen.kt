package com.example.ui.screens.delivery

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SampurnaDatabase
import com.example.data.local.entity.DeliveryPartnerEntity
import com.example.data.repository.SampurnaRepository
import com.example.ui.theme.SampurnaDarkPurple
import com.example.ui.theme.SampurnaPrimaryPurple
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryBoySignupScreen(
    onSignupSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val repository = remember { SampurnaRepository(SampurnaDatabase.getDatabase(context)) }

    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val vehicleTypes = listOf("Motorcycle", "Scooter", "Electric Scooter", "Auto / Van", "Bicycle")
    var selectedVehicleType by remember { mutableStateOf("Motorcycle") }

    var vehicleNumber by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }

    val hubs = listOf(
        "Keonjhar Central Hub (Town & Civil Lines)",
        "Anandapur Sub-Division Hub",
        "Barbil & Joda Mining Belt Hub",
        "Ghatagaon Tarini Temple Hub",
        "Champua Northern Hub"
    )
    var selectedHub by remember { mutableStateOf(hubs[0]) }
    var hubDropdownExpanded by remember { mutableStateOf(false) }

    var emergencyContact by remember { mutableStateOf("") }

    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        if (isSubmittedSuccess) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Application Submitted!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Awaiting Admin Approval",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD97706)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Applicant Summary",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        SummaryItem(label = "Delivery Partner", value = name)
                        SummaryItem(label = "Mobile", value = mobile)
                        SummaryItem(label = "Vehicle", value = "$selectedVehicleType ($vehicleNumber)")
                        SummaryItem(label = "Driving License", value = licenseNumber)
                        SummaryItem(label = "Assigned Hub", value = selectedHub)
                        SummaryItem(label = "Status", value = "PENDING APPROVAL")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Sampurna Admin will verify your driving credentials and activate your rider profile. Once approved, you will be onboarded to the Keonjhar delivery fleet.",
                            fontSize = 12.sp,
                            color = Color(0xFF1E40AF),
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                ) {
                    Text("Return to Store", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "Delivery Boy Sign Up",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = SampurnaDarkPurple
                        )
                        Text(
                            text = "Join Sampurna Fast Delivery Fleet (Keonjhar)",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Approval Notice Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Admin Approval Process: New delivery partners require Admin verification of vehicle & driving license before getting orders.",
                            fontSize = 11.sp,
                            color = Color(0xFF92400E),
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = errorMessage ?: "", fontSize = 12.sp, color = Color(0xFFDC2626))
                        }
                    }
                }

                // Partner Details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Personal Information", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it; errorMessage = null },
                            label = { Text("Full Name *") },
                            placeholder = { Text("e.g. Subrat Kumar Behera") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFD97706)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("delivery_signup_name"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = mobile,
                            onValueChange = { mobile = it; errorMessage = null },
                            label = { Text("Mobile Number (10 Digits) *") },
                            placeholder = { Text("e.g. 9861234567") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFFD97706)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("delivery_signup_mobile"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; errorMessage = null },
                            label = { Text("Email Address *") },
                            placeholder = { Text("e.g. subrat.delivery@gmail.com") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFFD97706)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("delivery_signup_email"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = emergencyContact,
                            onValueChange = { emergencyContact = it },
                            label = { Text("Emergency Contact (Family/Friend)") },
                            placeholder = { Text("e.g. 9437123456") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("delivery_signup_emergency"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Vehicle & License Details", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Vehicle Type", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            vehicleTypes.take(3).forEach { vType ->
                                FilterChip(
                                    selected = selectedVehicleType == vType,
                                    onClick = { selectedVehicleType = vType },
                                    label = { Text(vType, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFD97706),
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            vehicleTypes.drop(3).forEach { vType ->
                                FilterChip(
                                    selected = selectedVehicleType == vType,
                                    onClick = { selectedVehicleType = vType },
                                    label = { Text(vType, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFD97706),
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = vehicleNumber,
                            onValueChange = { vehicleNumber = it; errorMessage = null },
                            label = { Text("Vehicle Registration Number *") },
                            placeholder = { Text("e.g. OD-09-AK-5421") },
                            leadingIcon = { Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = Color(0xFFD97706)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("delivery_signup_vehicle_number"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = licenseNumber,
                            onValueChange = { licenseNumber = it; errorMessage = null },
                            label = { Text("Driving License (DL) Number *") },
                            placeholder = { Text("e.g. OD09 20220014820") },
                            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = Color(0xFFD97706)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("delivery_signup_license"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Preferred Keonjhar Hub", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                        Spacer(modifier = Modifier.height(6.dp))

                        ExposedDropdownMenuBox(
                            expanded = hubDropdownExpanded,
                            onExpandedChange = { hubDropdownExpanded = !hubDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedHub,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Assigned Hub") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = hubDropdownExpanded) },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFD97706)) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = hubDropdownExpanded,
                                onDismissRequest = { hubDropdownExpanded = false }
                            ) {
                                hubs.forEach { hub ->
                                    DropdownMenuItem(
                                        text = { Text(hub, fontSize = 12.sp) },
                                        onClick = {
                                            selectedHub = hub
                                            hubDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Security Password", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it; errorMessage = null },
                            label = { Text("Password *") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFD97706)) },
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                                }
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("delivery_signup_password"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it; errorMessage = null },
                            label = { Text("Confirm Password *") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFD97706)) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("delivery_signup_confirm_password"),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (name.isBlank() || mobile.isBlank() || email.isBlank() || vehicleNumber.isBlank() || licenseNumber.isBlank() || password.isBlank()) {
                            errorMessage = "Please fill all required fields marked with *"
                            return@Button
                        }
                        if (mobile.trim().length < 10) {
                            errorMessage = "Please enter a valid 10-digit mobile number"
                            return@Button
                        }
                        if (!email.contains("@")) {
                            errorMessage = "Please enter a valid email address"
                            return@Button
                        }
                        if (password != confirmPassword) {
                            errorMessage = "Passwords do not match"
                            return@Button
                        }

                        isSubmitting = true
                        errorMessage = null

                        coroutineScope.launch {
                            try {
                                val newBoy = DeliveryPartnerEntity(
                                    name = name.trim(),
                                    mobile = mobile.trim(),
                                    email = email.trim(),
                                    passwordHash = password.trim(),
                                    vehicleType = selectedVehicleType,
                                    vehicleNumber = vehicleNumber.trim().uppercase(),
                                    licenseNumber = licenseNumber.trim().uppercase(),
                                    assignedHub = selectedHub,
                                    emergencyContact = emergencyContact.trim(),
                                    isActive = false, // Pending Admin Approval!
                                    isOnDuty = false,
                                    rating = 5.0f,
                                    totalDeliveries = 0
                                )
                                val id = repository.createDeliveryBoy(newBoy)
                                isSubmitting = false
                                if (id > 0) {
                                    isSubmittedSuccess = true
                                } else {
                                    errorMessage = "Failed to register. Please try again."
                                }
                            } catch (e: Exception) {
                                isSubmitting = false
                                errorMessage = e.localizedMessage ?: "Error submitting application."
                            }
                        }
                    },
                    enabled = !isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("delivery_submit_registration_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submitting Application...")
                    } else {
                        Text("Submit Partner Registration", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF64748B))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
    }
}
