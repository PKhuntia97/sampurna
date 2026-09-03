package com.example.ui.screens.checkout

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.AddressEntity
import com.example.data.local.entity.OrderEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel,
    onBackClick: () -> Unit,
    onOrderComplete: (Long) -> Unit,
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val savedAddresses by viewModel.savedAddresses.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()
    val checkoutItems by viewModel.checkoutItems.collectAsState()
    val deliveryQuote by viewModel.deliveryQuote.collectAsState()
    val cancellationAnalyses by viewModel.cancellationAnalyses.collectAsState()
    val gatewayConfig by viewModel.gatewayConfig.collectAsState()

    val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsState()
    val selectedUpiApp by viewModel.selectedUpiApp.collectAsState()
    val upiIdInput by viewModel.upiIdInput.collectAsState()

    val cardNumber by viewModel.cardNumber.collectAsState()
    val cardExpiry by viewModel.cardExpiry.collectAsState()
    val cardCvv by viewModel.cardCvv.collectAsState()
    val cardHolderName by viewModel.cardHolderName.collectAsState()

    val isProcessing by viewModel.isProcessing.collectAsState()
    val showOtpDialog by viewModel.showOtpDialog.collectAsState()
    val otpInput by viewModel.otpInput.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = when (currentStep) {
                                is CheckoutStep.Address -> "Select Delivery Address"
                                is CheckoutStep.DeliveryAndReview -> "Delivery & Distance"
                                is CheckoutStep.Payment -> "Payment & Place Order"
                                is CheckoutStep.Success -> "Order Confirmed!"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        if (currentStep !is CheckoutStep.Success) {
                            Text(
                                text = "Step ${when (currentStep) {
                                    is CheckoutStep.Address -> "1 of 3: Address"
                                    is CheckoutStep.DeliveryAndReview -> "2 of 3: Distance & Summary"
                                    is CheckoutStep.Payment -> "3 of 3: Payment"
                                    else -> ""
                                }}",
                                fontSize = 12.sp,
                                color = Violet100
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (currentStep !is CheckoutStep.Success) {
                        IconButton(
                            onClick = {
                                when (currentStep) {
                                    is CheckoutStep.Address -> onBackClick()
                                    is CheckoutStep.DeliveryAndReview -> viewModel.setStep(CheckoutStep.Address)
                                    is CheckoutStep.Payment -> viewModel.setStep(CheckoutStep.DeliveryAndReview)
                                    else -> onBackClick()
                                }
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Violet900)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Slate50,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (val step = currentStep) {
                is CheckoutStep.Address -> {
                    AddressSelectionStep(
                        savedAddresses = savedAddresses,
                        selectedAddress = selectedAddress,
                        onSelectAddress = { viewModel.selectAddress(it) },
                        onProceed = {
                            if (selectedAddress != null) {
                                viewModel.setStep(CheckoutStep.DeliveryAndReview)
                            }
                        }
                    )
                }

                is CheckoutStep.DeliveryAndReview -> {
                    DeliveryAndReviewStep(
                        selectedAddress = selectedAddress,
                        deliveryQuote = deliveryQuote,
                        checkoutItems = checkoutItems,
                        cancellationAnalyses = cancellationAnalyses,
                        subtotal = viewModel.calculateItemsSubtotal(),
                        deliveryCharge = viewModel.calculateDeliveryCharge(),
                        penaltyAdjustment = viewModel.calculateCancellationAdjustment(),
                        grandTotal = viewModel.calculateGrandTotal(),
                        onChangeAddress = { viewModel.setStep(CheckoutStep.Address) },
                        onProceedToPayment = { viewModel.setStep(CheckoutStep.Payment) }
                    )
                }

                is CheckoutStep.Payment -> {
                    PaymentStep(
                        gatewayConfig = gatewayConfig,
                        selectedMethod = selectedPaymentMethod,
                        onSelectMethod = { viewModel.setPaymentMethod(it) },
                        selectedUpiApp = selectedUpiApp,
                        onSelectUpiApp = { viewModel.setUpiApp(it) },
                        upiIdInput = upiIdInput,
                        onUpiIdChange = { viewModel.setUpiId(it) },
                        cardNumber = cardNumber,
                        cardExpiry = cardExpiry,
                        cardCvv = cardCvv,
                        cardHolderName = cardHolderName,
                        onCardDetailsChange = { n, e, c, h -> viewModel.setCardDetails(n, e, c, h) },
                        subtotal = viewModel.calculateItemsSubtotal(),
                        deliveryCharge = viewModel.calculateDeliveryCharge(),
                        codFee = viewModel.calculateCodFee(),
                        penaltyAdjustment = viewModel.calculateCancellationAdjustment(),
                        grandTotal = viewModel.calculateGrandTotal(),
                        isProcessing = isProcessing,
                        onPayNow = { viewModel.processPaymentAndPlaceOrder() }
                    )
                }

                is CheckoutStep.Success -> {
                    OrderSuccessStep(
                        order = step.order,
                        onTrackOrder = { onOrderComplete(step.order.id) },
                        onContinueShopping = onContinueShopping
                    )
                }
            }

            // Bank Card 3D Secure OTP Authentication Simulation Dialog
            if (showOtpDialog) {
                BankOtpVerificationDialog(
                    otpInput = otpInput,
                    onOtpChange = { viewModel.setOtp(it) },
                    onVerify = { viewModel.verifyCardOtpAndPlaceOrder() },
                    onDismiss = { viewModel.dismissOtpDialog() },
                    amount = viewModel.calculateGrandTotal()
                )
            }
        }
    }
}

// ----------------------------------------------------
// STEP 1: ADDRESS SELECTION
// ----------------------------------------------------
@Composable
private fun AddressSelectionStep(
    savedAddresses: List<AddressEntity>,
    selectedAddress: AddressEntity?,
    onSelectAddress: (AddressEntity) -> Unit,
    onProceed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Select a Delivery Destination",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate900
                )
                Text(
                    text = "Distance-based delivery charge will be computed dynamically from the Seller hub.",
                    fontSize = 12.sp,
                    color = Slate600
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            items(savedAddresses, key = { it.id }) { address ->
                val isSelected = selectedAddress?.id == address.id
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Violet50 else Color.White
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Violet700) else androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectAddress(address) }
                        .testTag("address_card_${address.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (isSelected) Violet700 else Slate400,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = address.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Slate900
                                )
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (address.addressType == "HOME") Violet100 else Slate100
                                ) {
                                    Text(
                                        text = address.addressType,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (address.addressType == "HOME") Violet700 else Slate700,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${address.houseFlat}, ${address.streetArea}",
                                fontSize = 13.sp,
                                color = Slate700
                            )
                            if (address.landmark.isNotBlank()) {
                                Text(
                                    text = "Landmark: ${address.landmark}",
                                    fontSize = 12.sp,
                                    color = Slate500
                                )
                            }
                            Text(
                                text = "${address.city}, ${address.state} - ${address.pinCode}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate800
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Phone: ${address.mobile}",
                                fontSize = 12.sp,
                                color = Slate600
                            )
                        }
                    }
                }
            }
        }

        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onProceed,
                enabled = selectedAddress != null,
                colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .height(50.dp)
                    .testTag("proceed_to_delivery_btn")
            ) {
                Text("Deliver to this Address", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

// ----------------------------------------------------
// STEP 2: DELIVERY & DISTANCE CALCULATION & REVIEW
// ----------------------------------------------------
@Composable
private fun DeliveryAndReviewStep(
    selectedAddress: AddressEntity?,
    deliveryQuote: com.example.data.repository.DeliveryFeeQuote?,
    checkoutItems: List<com.example.data.repository.CartItemWithProduct>,
    cancellationAnalyses: List<com.example.data.repository.ProductCancellationAnalysis>,
    subtotal: Double,
    deliveryCharge: Double,
    penaltyAdjustment: Double,
    grandTotal: Double,
    onChangeAddress: () -> Unit,
    onProceedToPayment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Selected Address Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Violet700, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Delivery Address", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
                            }
                            TextButton(onClick = onChangeAddress) {
                                Text("Change", color = Violet700, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        if (selectedAddress != null) {
                            Text(
                                text = "${selectedAddress.name} (${selectedAddress.mobile})",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Slate800
                            )
                            Text(
                                text = "${selectedAddress.houseFlat}, ${selectedAddress.streetArea}, ${selectedAddress.city} - ${selectedAddress.pinCode}",
                                fontSize = 12.sp,
                                color = Slate600
                            )
                        }
                    }
                }
            }

            // Distance & Delivery Charge Breakdown Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Violet50),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Violet200),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = Violet700, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Distance & Delivery Charge", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Violet900)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (deliveryQuote != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Estimated Distance", fontSize = 13.sp, color = Slate700)
                                Text("${deliveryQuote.distanceKm} KM", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Violet900)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Delivery Calculation Rule", fontSize = 12.sp, color = Slate600)
                                Text(
                                    if (deliveryQuote.distanceKm <= deliveryQuote.baseKm) "0–5 KM: Flat ₹${deliveryQuote.baseFee.toInt()}"
                                    else "₹${deliveryQuote.baseFee.toInt()} + ₹${deliveryQuote.perKmExtra.toInt()}/extra KM",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Slate700
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Divider(color = Violet200)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total Delivery Charge", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                                Text("₹${"%.2f".format(deliveryQuote.totalDeliveryCharge)}", fontWeight = FontWeight.Black, fontSize = 15.sp, color = Violet900)
                            }
                        }
                    }
                }
            }

            // Transparent Repeated Cancellation Warning (1% Rule)
            if (penaltyAdjustment > 0) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Amber100),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Amber500),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Orange600, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Repeated Cancellation Adjustment Applied (+1%)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Orange600
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "You have previously cancelled this product more than 2 times. A transparent 1% adjustment (+₹${"%.2f".format(penaltyAdjustment)}) is applied as per platform policy.",
                                    fontSize = 11.sp,
                                    color = Slate800
                                )
                            }
                        }
                    }
                }
            }

            // Items Overview List
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Order Items (${checkoutItems.sumOf { it.cartItem.quantity }})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
                        Spacer(modifier = Modifier.height(10.dp))

                        checkoutItems.forEach { item ->
                            val p = item.product
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = p?.name ?: "Product",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = Slate900,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Qty: ${item.cartItem.quantity} × ₹${(p?.price ?: item.cartItem.unitPrice).toInt()}",
                                        fontSize = 11.sp,
                                        color = Slate500
                                    )
                                }
                                Text(
                                    text = "₹${((p?.price ?: item.cartItem.unitPrice) * item.cartItem.quantity).toInt()}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Slate900
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Continue to Payment
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${"%.2f".format(grandTotal)}",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = Violet900
                    )
                    Text("Total with Delivery", fontSize = 11.sp, color = Slate500)
                }

                Button(
                    onClick = onProceedToPayment,
                    colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("proceed_to_payment_btn")
                ) {
                    Text("Select Payment", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// ----------------------------------------------------
// STEP 3: PAYMENT METHOD SELECTION & ORDER PLACEMENT
// ----------------------------------------------------
@Composable
private fun PaymentStep(
    gatewayConfig: com.example.data.repository.PaymentGatewayConfig?,
    selectedMethod: String,
    onSelectMethod: (String) -> Unit,
    selectedUpiApp: String,
    onSelectUpiApp: (String) -> Unit,
    upiIdInput: String,
    onUpiIdChange: (String) -> Unit,
    cardNumber: String,
    cardExpiry: String,
    cardCvv: String,
    cardHolderName: String,
    onCardDetailsChange: (String, String, String, String) -> Unit,
    subtotal: Double,
    deliveryCharge: Double,
    codFee: Double,
    penaltyAdjustment: Double,
    grandTotal: Double,
    isProcessing: Boolean,
    onPayNow: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Select Payment Method",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate900
                )
                Text(
                    text = "Secured 256-bit encrypted checkout with verified backend simulation",
                    fontSize = 12.sp,
                    color = Slate500
                )
            }

            // Option 1: UPI (PhonePe, Google Pay, Paytm, Other)
            item {
                PaymentOptionCard(
                    title = "UPI (Instant & Zero Fee)",
                    subtitle = "Google Pay, PhonePe, Paytm, or UPI ID",
                    icon = Icons.Default.QrCode,
                    isSelected = selectedMethod == "UPI",
                    onClick = { onSelectMethod("UPI") }
                ) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Text("Choose UPI App:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Google Pay", "PhonePe", "Paytm").forEach { app ->
                                val isAppSelected = selectedUpiApp == app
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isAppSelected) Violet100 else Slate100,
                                    border = if (isAppSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Violet700) else null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onSelectUpiApp(app) }
                                ) {
                                    Text(
                                        text = app,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isAppSelected) Violet900 else Slate700,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = upiIdInput,
                            onValueChange = onUpiIdChange,
                            label = { Text("Or enter Custom UPI ID (e.g. name@okhdfcbank)") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("upi_id_input")
                        )
                    }
                }
            }

            // Option 2: Cards (Debit & Credit Card)
            item {
                PaymentOptionCard(
                    title = "Debit / Credit Cards",
                    subtitle = "Visa, MasterCard, RuPay with 3D Secure OTP",
                    icon = Icons.Default.CreditCard,
                    isSelected = selectedMethod == "DEBIT_CARD" || selectedMethod == "CREDIT_CARD",
                    onClick = { onSelectMethod("DEBIT_CARD") }
                ) {
                    Column(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { if (it.length <= 19) onCardDetailsChange(it, cardExpiry, cardCvv, cardHolderName) },
                            label = { Text("Card Number (16-digits)") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("card_number_input")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = cardExpiry,
                                onValueChange = { if (it.length <= 5) onCardDetailsChange(cardNumber, it, cardCvv, cardHolderName) },
                                label = { Text("MM/YY") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("card_expiry_input")
                            )
                            OutlinedTextField(
                                value = cardCvv,
                                onValueChange = { if (it.length <= 4) onCardDetailsChange(cardNumber, cardExpiry, it, cardHolderName) },
                                label = { Text("CVV") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).testTag("card_cvv_input")
                            )
                        }

                        OutlinedTextField(
                            value = cardHolderName,
                            onValueChange = { onCardDetailsChange(cardNumber, cardExpiry, cardCvv, it) },
                            label = { Text("Cardholder Name") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("card_holder_input")
                        )
                    }
                }
            }

            // Option 3: Cash on Delivery (COD)
            item {
                val isCodEnabled = gatewayConfig?.codEnabled != false
                PaymentOptionCard(
                    title = "Cash on Delivery (COD)",
                    subtitle = if (isCodEnabled) "Pay in cash at your doorstep (+₹10 handling fee)" else "Temporarily unavailable",
                    icon = Icons.Default.AccountBalanceWallet,
                    isSelected = selectedMethod == "COD",
                    enabled = isCodEnabled,
                    onClick = { if (isCodEnabled) onSelectMethod("COD") }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Orange100)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Note: Cash on Delivery adds ₹10 handling fee. Pay via UPI/Card to save ₹10!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Orange600
                        )
                    }
                }
            }

            // Final Bill Summary Review
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Final Payment Summary", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
                        Spacer(modifier = Modifier.height(10.dp))

                        PriceRow("Product Subtotal (with Sampurna price)", "₹${"%.2f".format(subtotal)}")
                        PriceRow("Delivery Fee", "₹${"%.2f".format(deliveryCharge)}")

                        if (selectedMethod == "COD") {
                            PriceRow("COD Handling Fee", "+ ₹${"%.2f".format(codFee)}", valueColor = Orange600)
                        } else {
                            PriceRow("Online Payment COD Discount", "₹0 (Free)", valueColor = Emerald700)
                        }

                        if (penaltyAdjustment > 0) {
                            PriceRow("Frequent Cancellation Adjustment (+1%)", "+ ₹${"%.2f".format(penaltyAdjustment)}", valueColor = Rose600)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Slate200)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Payable", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Slate900)
                            Text("₹${"%.2f".format(grandTotal)}", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Violet900)
                        }
                    }
                }
            }
        }

        // Bottom Pay Button
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onPayNow,
                enabled = !isProcessing,
                colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .height(52.dp)
                    .testTag("pay_and_place_order_btn")
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Verifying Payment...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (selectedMethod == "COD") "Place COD Order (₹${"%.2f".format(grandTotal)})"
                        else "Pay & Place Order (₹${"%.2f".format(grandTotal)})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentOptionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Violet50 else Color.White
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Violet700) else androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Violet100 else Slate100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = if (isSelected) Violet700 else Slate600, modifier = Modifier.size(22.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                    Text(subtitle, fontSize = 11.sp, color = Slate500)
                }

                Icon(
                    if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (isSelected) Violet700 else Slate400,
                    modifier = Modifier.size(20.dp)
                )
            }

            if (isSelected && content != null) {
                content()
            }
        }
    }
}

@Composable
private fun BankOtpVerificationDialog(
    otpInput: String,
    onOtpChange: (String) -> Unit,
    onVerify: () -> Unit,
    onDismiss: () -> Unit,
    amount: Double
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = Violet700, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("3D Secure Bank Verification", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Text(
                    text = "A one-time verification password has been sent to your registered bank mobile for amount ₹${"%.2f".format(amount)}.",
                    fontSize = 12.sp,
                    color = Slate600
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = otpInput,
                    onValueChange = onOtpChange,
                    label = { Text("Enter OTP (Use 123456)") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("bank_otp_input")
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("Demo Simulator: Type 123456 to approve payment", fontSize = 11.sp, color = Emerald700, fontWeight = FontWeight.SemiBold)
            }
        },
        confirmButton = {
            Button(
                onClick = onVerify,
                colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                modifier = Modifier.testTag("verify_otp_btn")
            ) {
                Text("Authorize & Pay")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// ----------------------------------------------------
// STEP 4: ORDER SUCCESS STEP
// ----------------------------------------------------
@Composable
private fun OrderSuccessStep(
    order: OrderEntity,
    onTrackOrder: () -> Unit,
    onContinueShopping: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Emerald100),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald700, modifier = Modifier.size(54.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Order Placed Successfully! 🎉",
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = Slate900,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Order ID: ${order.orderNumber}",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Violet700
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Amount Paid", fontSize = 13.sp, color = Slate600)
                    Text("₹${"%.2f".format(order.totalAmount)}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Payment Method", fontSize = 13.sp, color = Slate600)
                    Text(order.paymentMethod, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Violet700)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Delivery To", fontSize = 13.sp, color = Slate600)
                    Text(order.customerName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onTrackOrder,
            colors = ButtonDefaults.buttonColors(containerColor = Violet700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("track_order_btn")
        ) {
            Text("Track Order Status", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onContinueShopping,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("back_to_shop_btn")
        ) {
            Text("Continue Shopping", fontWeight = FontWeight.SemiBold, color = Violet700)
        }
    }
}

@Composable
private fun PriceRow(label: String, value: String, valueColor: Color = Slate800) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = Slate600)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}
