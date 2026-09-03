package com.example.ui.screens.orders

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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.OrderEntity
import com.example.data.repository.OrderWithDetails
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerOrdersHistoryScreen(
    viewModel: CustomerOrdersViewModel,
    onBackClick: () -> Unit,
    onOrderClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    val filteredOrders = remember(orders, selectedFilter) {
        when (selectedFilter) {
            "ACTIVE" -> orders.filter { it.orderStatus in listOf("ORDER_PLACED", "SELLER_PROCESSING", "READY_FOR_PICKUP", "OUT_FOR_DELIVERY") }
            "DELIVERED" -> orders.filter { it.orderStatus == "DELIVERED" }
            "CANCELLED" -> orders.filter { it.orderStatus.startsWith("CANCELLED") }
            else -> orders
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Orders", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Violet900)
            )
        },
        containerColor = Slate50,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("ALL" to "All", "ACTIVE" to "Active", "DELIVERED" to "Delivered", "CANCELLED" to "Cancelled").forEach { (key, label) ->
                    val isSelected = selectedFilter == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectFilter(key) },
                        label = { Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Violet700,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            if (filteredOrders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Slate400, modifier = Modifier.size(54.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No orders found", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Slate700)
                        Text("You don't have any orders under this category.", fontSize = 12.sp, color = Slate500)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredOrders, key = { it.id }) { order ->
                        OrderSummaryCard(
                            order = order,
                            onClick = { onOrderClick(order.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }
}

@Composable
private fun OrderSummaryCard(
    order: OrderEntity,
    onClick: () -> Unit
) {
    val dateStr = remember(order.createdAt) {
        SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(order.createdAt))
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("order_card_${order.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                        color = Violet900
                    )
                    Text(text = dateStr, fontSize = 11.sp, color = Slate500)
                }

                OrderStatusBadge(status = order.orderStatus)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Slate100)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Seller: ${order.sellerName}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate800
                    )
                    Text(
                        text = "${order.productCount} items • ${order.paymentMethod}",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                Text(
                    text = "₹${"%.2f".format(order.totalAmount)}",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Slate900
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View Details & Tracking",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Violet700
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Violet700, modifier = Modifier.size(14.dp))
            }
        }
    }
}

// ----------------------------------------------------
// ORDER DETAILS & TRACKING TIMELINE SCREEN
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailTrackingScreen(
    orderId: Long,
    viewModel: CustomerOrdersViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val details by viewModel.selectedOrderDetails.collectAsState()
    val showCancelDialog by viewModel.showCancelDialog.collectAsState()
    val cancelReason by viewModel.cancelReason.collectAsState()
    val isCancelling by viewModel.isCancelling.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(orderId) {
        viewModel.loadOrderDetails(orderId)
    }

    LaunchedEffect(actionMessage) {
        actionMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = details?.order?.orderNumber ?: "Order Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Live Status & Tracking",
                            fontSize = 11.sp,
                            color = Violet100
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Violet900)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Slate50,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (details == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Violet700)
            }
        } else {
            val order = details!!.order
            val items = details!!.items

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Tracking Stepper Timeline Card
                item {
                    TrackingTimelineCard(order = order)
                }

                // Cancellation Notification banner (if cancelled)
                if (order.orderStatus.startsWith("CANCELLED")) {
                    item {
                        CancellationAlertBanner(order = order)
                    }
                }

                // Seller Information Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Storefront, contentDescription = null, tint = Violet700, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Seller Hub Information", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(order.sellerName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate800)
                            Text(order.sellerAddress, fontSize = 12.sp, color = Slate600)
                        }
                    }
                }

                // Items Ordered
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Items Ordered (${items.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            Spacer(modifier = Modifier.height(10.dp))

                            items.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Slate100),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (item.productImage != null) {
                                            AsyncImage(
                                                model = item.productImage,
                                                contentDescription = item.productName,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } else {
                                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Slate400)
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.productName,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = Slate900,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Qty: ${item.quantity} × ₹${item.unitPrice.toInt()}",
                                            fontSize = 11.sp,
                                            color = Slate500
                                        )
                                    }

                                    Text(
                                        text = "₹${item.subtotal.toInt()}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Slate900
                                    )
                                }
                            }
                        }
                    }
                }

                // Delivery Destination & Distance Snapshot
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Violet700, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Delivery Address", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(order.deliveryAddressSnapshot, fontSize = 12.sp, color = Slate700)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Distance: ${order.distanceKm} KM from store", fontSize = 11.sp, color = Violet700, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Price Breakdown
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Payment & Bill Breakdown", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Slate900)
                            Spacer(modifier = Modifier.height(8.dp))

                            PriceRow("Items Subtotal", "₹${"%.2f".format(order.subtotalAmount)}")
                            PriceRow("Delivery Charge (${order.distanceKm} KM)", "₹${"%.2f".format(order.deliveryCharge)}")
                            if (order.codFee > 0) {
                                PriceRow("COD Handling Fee", "₹${"%.2f".format(order.codFee)}")
                            }
                            if (order.cancellationAdjustmentAmount > 0) {
                                PriceRow("Repeated Cancellation Adjustment (+1%)", "₹${"%.2f".format(order.cancellationAdjustmentAmount)}", valueColor = Rose600)
                            }
                            PriceRow("Payment Mode", order.paymentMethod, valueColor = Violet700)
                            PriceRow("Payment Status", order.paymentStatus, valueColor = if (order.paymentStatus == "PAID") Emerald700 else Orange600)

                            Spacer(modifier = Modifier.height(6.dp))
                            Divider(color = Slate200)
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Amount", fontWeight = FontWeight.Black, fontSize = 15.sp, color = Slate900)
                                Text("₹${"%.2f".format(order.totalAmount)}", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Violet900)
                            }
                        }
                    }
                }

                // Customer Cancellation Action (if eligible)
                val isCancellable = order.orderStatus in listOf("ORDER_PLACED", "SELLER_PROCESSING", "READY_FOR_PICKUP")
                if (isCancellable) {
                    item {
                        OutlinedButton(
                            onClick = { viewModel.openCancelDialog() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Rose600),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("cancel_order_button")
                        ) {
                            Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Cancel Order", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(30.dp)) }
            }
        }

        // Customer Cancellation Modal Dialog
        if (showCancelDialog) {
            CustomerCancellationModal(
                selectedReason = cancelReason,
                onReasonSelected = { viewModel.setCancelReason(it) },
                onConfirmCancel = { viewModel.cancelCurrentOrder() },
                onDismiss = { viewModel.closeCancelDialog() },
                isCancelling = isCancelling
            )
        }
    }
}

// ----------------------------------------------------
// TRACKING TIMELINE COMPONENT
// ----------------------------------------------------
@Composable
private fun TrackingTimelineCard(order: OrderEntity) {
    val steps = listOf(
        "ORDER_PLACED" to "Order Placed",
        "SELLER_PROCESSING" to "Seller Processing",
        "READY_FOR_PICKUP" to "Ready for Pickup",
        "DELIVERED" to "Delivered"
    )

    val currentStepIndex = when (order.orderStatus) {
        "ORDER_PLACED" -> 0
        "SELLER_PROCESSING" -> 1
        "READY_FOR_PICKUP" -> 2
        "DELIVERED" -> 3
        else -> if (order.orderStatus.startsWith("CANCELLED")) -1 else 0
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order Progress", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
            Spacer(modifier = Modifier.height(14.dp))

            steps.forEachIndexed { index, (key, label) ->
                val isCompleted = currentStepIndex >= index
                val isCurrent = currentStepIndex == index

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCompleted -> Emerald600
                                        isCurrent -> Violet700
                                        else -> Slate200
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            } else {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.White))
                            }
                        }

                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(28.dp)
                                    .background(if (isCompleted) Emerald500 else Slate200)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = label,
                            fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            color = if (isCurrent || isCompleted) Slate900 else Slate400
                        )
                        Text(
                            text = when (key) {
                                "ORDER_PLACED" -> "Received & Confirmed"
                                "SELLER_PROCESSING" -> "Seller is inspecting & packing"
                                "READY_FOR_PICKUP" -> "Packed & assigned for pickup"
                                "DELIVERED" -> "Handed over to customer"
                                else -> ""
                            },
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderStatusBadge(status: String) {
    val (bg, fg, label) = when (status) {
        "ORDER_PLACED" -> Triple(Violet100, Violet800, "Placed")
        "SELLER_PROCESSING" -> Triple(Amber100, Orange600, "Processing")
        "READY_FOR_PICKUP" -> Triple(Emerald100, Emerald800, "Ready for Pickup")
        "DELIVERED" -> Triple(Emerald100, Emerald900, "Delivered")
        "CANCELLED_BY_SELLER" -> Triple(Rose100, Rose700, "Cancelled by Seller")
        "CANCELLED_BY_CUSTOMER" -> Triple(Rose100, Rose700, "Cancelled by You")
        else -> Triple(Slate100, Slate700, status)
    }

    Surface(shape = RoundedCornerShape(6.dp), color = bg) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = fg,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
private fun CancellationAlertBanner(order: OrderEntity) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Rose100),
        border = androidx.compose.foundation.BorderStroke(1.dp, Rose300),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(Icons.Default.Cancel, contentDescription = null, tint = Rose700, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = if (order.orderStatus == "CANCELLED_BY_SELLER") "Order Cancelled by Seller" else "Order Cancelled by Customer",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Rose900
                )
                if (!order.cancelReason.isNullOrBlank()) {
                    Text("Reason: ${order.cancelReason}", fontSize = 11.sp, color = Rose800)
                }
                if (order.refundStatus == "INITIATED") {
                    Text("Refund Status: ₹${"%.2f".format(order.totalAmount)} Initiated", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald800)
                }
            }
        }
    }
}

// ----------------------------------------------------
// CUSTOMER CANCELLATION MODAL
// ----------------------------------------------------
@Composable
private fun CustomerCancellationModal(
    selectedReason: String,
    onReasonSelected: (String) -> Unit,
    onConfirmCancel: () -> Unit,
    onDismiss: () -> Unit,
    isCancelling: Boolean
) {
    val reasons = listOf(
        "Ordered by mistake",
        "Found a better price elsewhere",
        "Delivery time is too long",
        "Changed my mind",
        "Incorrect delivery address"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Rose600, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirm Cancellation", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Text(
                    text = "Please tell us why you want to cancel this order:",
                    fontSize = 13.sp,
                    color = Slate700
                )
                Spacer(modifier = Modifier.height(10.dp))

                reasons.forEach { r ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onReasonSelected(r) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedReason == r,
                            onClick = { onReasonSelected(r) }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(r, fontSize = 12.sp, color = Slate800)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 1% penalty policy disclosure
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Amber100),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Platform Policy: Cancelling the same product more than 2 times applies a 1% price adjustment on future purchases of that item.",
                        fontSize = 10.sp,
                        color = Orange600,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmCancel,
                enabled = !isCancelling,
                colors = ButtonDefaults.buttonColors(containerColor = Rose600),
                modifier = Modifier.testTag("confirm_cancel_btn")
            ) {
                if (isCancelling) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp))
                } else {
                    Text("Confirm Cancel")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Keep Order")
            }
        }
    )
}

@Composable
private fun PriceRow(label: String, value: String, valueColor: Color = Slate800) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Slate600)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}
