package com.example.ui.screens.cart

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.repository.CartItemWithProduct
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onContinueShopping: () -> Unit,
    onProceedToCheckout: () -> Unit,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val couponInput by viewModel.couponInput.collectAsState()
    val couponError by viewModel.couponError.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()
    val availableOffers by viewModel.availableOffers.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

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
                            text = "My Shopping Cart",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${cartItems.sumOf { it.cartItem.quantity }} items",
                            fontSize = 12.sp,
                            color = Violet100
                        )
                    }
                },
                actions = {
                    if (cartItems.isNotEmpty()) {
                        TextButton(onClick = { viewModel.clearCart() }) {
                            Text("Clear All", color = Orange400, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Violet900)
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                CartBottomStickyBar(
                    finalTotal = viewModel.calculateFinalTotal(),
                    itemCount = cartItems.sumOf { it.cartItem.quantity },
                    onCheckout = onProceedToCheckout
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Slate50,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            EmptyCartView(
                onContinueShopping = onContinueShopping,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // Free & Transparent Delivery Banner
                item {
                    DeliveryAssuranceBanner()
                }

                // Cart Items List
                items(cartItems, key = { it.cartItem.id }) { itemWithProduct ->
                    CartProductCard(
                        itemWithProduct = itemWithProduct,
                        onQuantityChange = { newQty -> viewModel.updateQuantity(itemWithProduct.cartItem.id, newQty) },
                        onRemove = { viewModel.removeItem(itemWithProduct.cartItem.id) },
                        onSaveToWishlist = {
                            itemWithProduct.product?.let { p ->
                                viewModel.saveToWishlist(itemWithProduct.cartItem.id, p.id)
                            }
                        },
                        onProductClick = {
                            itemWithProduct.product?.let { p -> onProductClick(p.id) }
                        }
                    )
                }

                // Coupon Application Card
                item {
                    CouponSectionCard(
                        appliedCoupon = appliedCoupon,
                        couponInput = couponInput,
                        couponError = couponError,
                        availableOffers = availableOffers,
                        onCouponInputChange = { viewModel.setCouponInput(it) },
                        onApply = { viewModel.applyCoupon(couponInput) },
                        onRemoveCoupon = { viewModel.removeCoupon() },
                        onSelectOffer = { offer -> viewModel.applyCoupon(offer.code) }
                    )
                }

                // Price Breakdown Card
                item {
                    PriceBreakdownCard(
                        mrpTotal = viewModel.calculateMrpTotal(),
                        subtotal = viewModel.calculateSubtotal(),
                        couponDiscount = viewModel.calculateDiscount(),
                        finalTotal = viewModel.calculateFinalTotal()
                    )
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun EmptyCartView(
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Violet100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = Violet700,
                modifier = Modifier.size(54.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Your Cart is Empty",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Slate900
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Explore verified local sellers in Keonjhar with genuine warranty and 2% transparent pricing.",
            fontSize = 13.sp,
            color = Slate600,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onContinueShopping,
            colors = ButtonDefaults.buttonColors(containerColor = Violet700),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("continue_shopping_button")
        ) {
            Text("Start Shopping", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Composable
private fun DeliveryAssuranceBanner() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Emerald100),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Shield, contentDescription = null, tint = Emerald700, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "100% Genuine Local Seller Guarantee & Distance-Based Delivery",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Emerald900
            )
        }
    }
}

@Composable
private fun CartProductCard(
    itemWithProduct: CartItemWithProduct,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    onSaveToWishlist: () -> Unit,
    onProductClick: () -> Unit
) {
    val p = itemWithProduct.product
    val seller = itemWithProduct.seller
    val qty = itemWithProduct.cartItem.quantity
    val unitPrice = p?.price ?: itemWithProduct.cartItem.unitPrice
    val unitMrp = p?.mrp ?: itemWithProduct.cartItem.unitMrp
    val lineSubtotal = unitPrice * qty

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().testTag("cart_item_${itemWithProduct.cartItem.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Image
                Box(
                    modifier = Modifier
                        .size(85.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate100)
                        .clickable { onProductClick() },
                    contentAlignment = Alignment.Center
                ) {
                    if (p?.imageUrl != null) {
                        AsyncImage(
                            model = p.imageUrl,
                            contentDescription = p.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Slate400, modifier = Modifier.size(32.dp))
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = p?.brand ?: "Genuine Brand",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Violet700
                    )
                    Text(
                        text = p?.name ?: "Sampurna Product",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate900,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { onProductClick() }
                    )

                    // Seller info badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Storefront, contentDescription = null, tint = Slate500, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = seller?.businessName ?: "Verified Seller",
                            fontSize = 11.sp,
                            color = Slate600
                        )
                    }

                    // Price Line
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "₹${unitPrice.toInt()}",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Slate900
                        )
                        if (unitMrp > unitPrice) {
                            Text(
                                text = "₹${unitMrp.toInt()}",
                                fontSize = 12.sp,
                                color = Slate400,
                                textDecoration = TextDecoration.LineThrough
                            )
                            val discountPct = ((unitMrp - unitPrice) / unitMrp * 100).toInt()
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Orange100
                            ) {
                                Text(
                                    text = "$discountPct% OFF",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Orange600,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Slate100)
            Spacer(modifier = Modifier.height(10.dp))

            // Action row: Stepper and Wishlist / Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity Stepper
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Slate100)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = { onQuantityChange(qty - 1) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            if (qty == 1) Icons.Default.DeleteOutline else Icons.Default.Remove,
                            contentDescription = "Decrease",
                            tint = if (qty == 1) Rose600 else Slate700,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "$qty",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Slate900,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    IconButton(
                        onClick = { onQuantityChange(qty + 1) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = Slate700, modifier = Modifier.size(16.dp))
                    }
                }

                // Item Subtotal
                Text(
                    text = "Subtotal: ₹${lineSubtotal.toInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Violet900
                )

                // Save to wishlist & delete
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onSaveToWishlist,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Wishlist", tint = Slate500, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Remove", tint = Rose600, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CouponSectionCard(
    appliedCoupon: com.example.data.local.entity.OfferEntity?,
    couponInput: String,
    couponError: String?,
    availableOffers: List<com.example.data.local.entity.OfferEntity>,
    onCouponInputChange: (String) -> Unit,
    onApply: () -> Unit,
    onRemoveCoupon: () -> Unit,
    onSelectOffer: (com.example.data.local.entity.OfferEntity) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalOffer, contentDescription = null, tint = Orange500, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Apply Coupon / Offer Code", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (appliedCoupon != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Emerald100)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald700, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(appliedCoupon.code, fontWeight = FontWeight.Black, fontSize = 14.sp, color = Emerald900)
                            Text(appliedCoupon.title, fontSize = 11.sp, color = Emerald800)
                        }
                    }
                    IconButton(onClick = onRemoveCoupon, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Emerald900)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = couponInput,
                        onValueChange = onCouponInputChange,
                        placeholder = { Text("e.g. SAMPURNA50", fontSize = 13.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Violet700,
                            unfocusedBorderColor = Slate300
                        ),
                        modifier = Modifier.weight(1f).testTag("coupon_input_field")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onApply,
                        colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(52.dp).testTag("apply_coupon_button")
                    ) {
                        Text("Apply", fontWeight = FontWeight.Bold)
                    }
                }

                if (couponError != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(couponError, color = Rose600, fontSize = 12.sp)
                }

                // Available quick coupons chips
                if (availableOffers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Available Coupons:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate500)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableOffers.take(2).forEach { offer ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Violet100,
                                modifier = Modifier.clickable { onSelectOffer(offer) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(offer.code, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Violet900)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("(${offer.discountPercent}% OFF)", fontSize = 10.sp, color = Violet700)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceBreakdownCard(
    mrpTotal: Double,
    subtotal: Double,
    couponDiscount: Double,
    finalTotal: Double
) {
    val mrpDiscount = Math.max(0.0, mrpTotal - subtotal)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Price Details", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Slate900)
            Spacer(modifier = Modifier.height(12.dp))

            PriceRow("Total MRP", "₹${"%.2f".format(mrpTotal)}")
            if (mrpDiscount > 0) {
                PriceRow("Product Discount", "- ₹${"%.2f".format(mrpDiscount)}", valueColor = Emerald700)
            }
            PriceRow("Item Total (with 2% Sampurna price)", "₹${"%.2f".format(subtotal)}")

            if (couponDiscount > 0) {
                PriceRow("Coupon Savings", "- ₹${"%.2f".format(couponDiscount)}", valueColor = Emerald700)
            }

            PriceRow(
                label = "Delivery Charges",
                value = "Calculated at checkout",
                valueColor = Violet700
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Slate200)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Payable", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Slate900)
                    Text("Inclusive of all taxes", fontSize = 11.sp, color = Slate500)
                }
                Text(
                    text = "₹${"%.2f".format(finalTotal)}",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = Violet900
                )
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, value: String, valueColor: Color = Slate800) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = Slate600)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}

@Composable
private fun CartBottomStickyBar(
    finalTotal: Double,
    itemCount: Int,
    onCheckout: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 12.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "₹${"%.2f".format(finalTotal)}",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = Violet900
                )
                Text(
                    text = "For $itemCount items",
                    fontSize = 12.sp,
                    color = Slate500
                )
            }

            Button(
                onClick = onCheckout,
                colors = ButtonDefaults.buttonColors(containerColor = Violet700),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("proceed_to_checkout_button")
            ) {
                Text("Proceed to Checkout", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}
