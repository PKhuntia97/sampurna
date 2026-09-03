package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ProductEntity
import com.example.ui.theme.*

data class ProductTab(val id: String, val title: String)

val ProductTabsList = listOf(
    ProductTab("trending", "Trending"),
    ProductTab("best_selling", "Best Selling"),
    ProductTab("todays_offers", "Today's Offers"),
    ProductTab("new_arrivals", "New Arrivals"),
    ProductTab("recommended", "Recommended")
)

@Composable
fun ProductSection(
    products: List<ProductEntity>,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    onAddToCart: (ProductEntity) -> Unit,
    onProductClick: (ProductEntity) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 16.dp)
    ) {
        // Section Header: "Best Deals For You" + "View All"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Best Deals For You",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = Slate900
            )

            Row(
                modifier = Modifier
                    .clickable { onViewAllClick() }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View All",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Violet700
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View all deals",
                    tint = Violet700,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Product Filter Tabs
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ProductTabsList) { tab ->
                val isSelected = selectedTab == tab.id
                val tabBg by animateColorAsState(
                    targetValue = if (isSelected) Violet700 else Color.White,
                    animationSpec = spring(),
                    label = "tab_bg"
                )
                val tabTextColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else Slate700,
                    animationSpec = spring(),
                    label = "tab_text"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(tabBg)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Violet700 else Slate200,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onTabSelected(tab.id) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                        .testTag("tab_${tab.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.title,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = tabTextColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Product Cards Grid
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No products found for this filter.",
                    color = Slate500,
                    fontSize = 13.sp
                )
            }
        } else {
            // Render 2-column list of products
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val chunkedProducts = products.chunked(2)
                chunkedProducts.forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { product ->
                            Box(modifier = Modifier.weight(1f)) {
                                ProductCard(
                                    product = product,
                                    onAddToCart = { onAddToCart(product) },
                                    onClick = { onProductClick(product) }
                                )
                            }
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductEntity,
    onAddToCart: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isWishlisted by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(18.dp), spotColor = Slate200)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag("product_card_${product.id}"),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Image Box + Badges (Discount + Wishlist)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Slate50)
            ) {
                // Product Graphic
                ProductVisualBadge(
                    iconType = product.iconType,
                    modifier = Modifier.fillMaxWidth()
                )

                // Discount Badge (Top Left)
                if (product.discount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Violet700)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-${product.discount}%",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Wishlist Heart Button (Top Right)
                IconButton(
                    onClick = { isWishlisted = !isWishlisted },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) Rose500 else Slate400,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Title
            Text(
                text = product.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate900,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp,
                modifier = Modifier.height(30.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Rating Row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF16A34A))
                        .padding(horizontal = 4.dp, vertical = 1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${product.rating}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(9.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${product.ratingCount})",
                    fontSize = 9.sp,
                    color = Slate400,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Price & MRP Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "₹${formatIndianCurrency(product.price)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Violet900
                    )
                    if (product.mrp > product.price) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "₹${formatIndianCurrency(product.mrp)}",
                            fontSize = 10.sp,
                            color = Slate400,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Add to Cart Button
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .testTag("add_to_cart_${product.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Violet700,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add to Cart",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun formatIndianCurrency(amount: Double): String {
    val longVal = amount.toLong()
    return String.format("%,d", longVal)
}
