package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.CategoryBannerEntity
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun MegaSaleBannerSection(
    generalBanners: List<CategoryBannerEntity>,
    categoryBanner: CategoryBannerEntity?,
    isCategorySelected: Boolean,
    onShopNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        AnimatedContent(
            targetState = isCategorySelected to categoryBanner,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "banner_transition"
        ) { (isSelected, catBanner) ->
            if (isSelected && catBanner != null) {
                // Category Specific Banner (Dynamically matched to clicked category)
                SingleBannerCard(
                    banner = catBanner,
                    onShopNowClick = onShopNowClick
                )
            } else {
                // General Banner Auto-Slider (When no category is selected)
                GeneralBannerSlider(
                    banners = if (generalBanners.isNotEmpty()) generalBanners else listOf(
                        CategoryBannerEntity(
                            title = "MEGA SALE",
                            subtitle = "Smart Gadgets, Fashion & Daily Essentials",
                            discountText = "UP TO 60% OFF",
                            tag = "SPECIAL OFFER",
                            bannerType = "mega_sale"
                        )
                    ),
                    onShopNowClick = onShopNowClick
                )
            }
        }
    }
}

@Composable
fun GeneralBannerSlider(
    banners: List<CategoryBannerEntity>,
    onShopNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { banners.size })

    // Auto-slide every 3.5 seconds
    LaunchedEffect(banners.size) {
        if (banners.size > 1) {
            while (true) {
                delay(3500)
                val nextPage = (pagerState.currentPage + 1) % banners.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val banner = banners[page]
            SingleBannerCard(
                banner = banner,
                onShopNowClick = onShopNowClick
            )
        }

        // Pagination Dots
        if (banners.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(banners.size) { index ->
                    val isCurrent = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (isCurrent) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCurrent) Violet700 else Violet400.copy(alpha = 0.4f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun SingleBannerCard(
    banner: CategoryBannerEntity,
    onShopNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientColors = getBannerGradient(banner.bannerType)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(172.dp) // Increased height for rich presentation and clear visibility
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(24.dp), spotColor = Violet900.copy(alpha = 0.35f))
            .clip(RoundedCornerShape(24.dp))
            .clickable { onShopNowClick() }
            .testTag("banner_card_${banner.id}"),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(gradientColors))
        ) {
            // Optional Background Image (if configured in banner)
            if (!banner.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = banner.imageUrl,
                    contentDescription = banner.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Dark Gradient Scrim to ensure crisp readable text
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.85f),
                                    Color.Black.copy(alpha = 0.65f),
                                    Color.Black.copy(alpha = 0.35f)
                                )
                            )
                        )
                )
            } else {
                // Subtle decorative background circles for visual depth
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.05f))
                )
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 20.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.08f))
                )
            }

            // Main Content Layout
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 18.dp, end = 14.dp, top = 14.dp, bottom = 14.dp)
            ) {
                // Left Content: Tag, Title, Subtitle, Discount, Shop Now Button
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.68f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Row: Tag Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GoldYellow.copy(alpha = 0.25f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = banner.tag ?: "SPECIAL OFFER",
                            color = GoldYellow,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.8.sp
                        )
                    }

                    // Middle Section: Title + Subtitle + Discount Tag
                    Column(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(
                            text = banner.title,
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 22.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            letterSpacing = (-0.3).sp
                        )

                        if (!banner.subtitle.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = banner.subtitle,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = banner.discountText ?: "UP TO 60% OFF",
                            color = GoldYellow,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    // Bottom: Shop Now Button Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Orange500)
                            .clickable { onShopNowClick() }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SHOP NOW",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Shop Now",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Right Graphics / Illustration & Limited Time Offer Badge
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd)
                ) {
                    // Limited Time Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Orange600.copy(alpha = 0.9f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "⚡ LIMITED TIME",
                            color = Color.White,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 3D Emojis / Visual Showcase matched to Category
                    val visualEmoji = getBannerEmoji(banner.bannerType)
                    Box(
                        modifier = Modifier
                            .padding(bottom = 6.dp, end = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = visualEmoji,
                            fontSize = 42.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}

private fun getBannerGradient(type: String): List<Color> {
    val key = type.lowercase()
    return when {
        key.contains("fashion") || key.contains("clothes") || key.contains("apparel") ->
            listOf(Color(0xFF831843), Color(0xFFBE185D), Color(0xFFF43F5E))

        key.contains("mobile") || key.contains("phone") ->
            listOf(Color(0xFF1E1B4B), Color(0xFF3730A3), Color(0xFF4F46E5))

        key.contains("grocery") || key.contains("veg") || key.contains("fresh") ->
            listOf(Color(0xFF064E3B), Color(0xFF059669), Color(0xFF10B981))

        key.contains("electronics") || key.contains("laptop") || key.contains("tv") ->
            listOf(Color(0xFF312E81), Color(0xFF5B21B6), Color(0xFF7C3AED))

        key.contains("electrical") ->
            listOf(Color(0xFF78350F), Color(0xFFB45309), Color(0xFFD97706))

        key.contains("plumber") ->
            listOf(Color(0xFF0C4A6E), Color(0xFF0284C7), Color(0xFF38BDF8))

        key.contains("food") || key.contains("restaurant") ->
            listOf(Color(0xFF7F1D1D), Color(0xFFB91C1C), Color(0xFFEF4444))

        key.contains("shoes") || key.contains("footwear") ->
            listOf(Color(0xFF1C1917), Color(0xFF44403C), Color(0xFF78716C))

        key.contains("beauty") || key.contains("cosmetic") ->
            listOf(Color(0xFF701A75), Color(0xFFA21CAF), Color(0xFFE879F9))

        key.contains("sport") || key.contains("fitness") ->
            listOf(Color(0xFF14532D), Color(0xFF15803D), Color(0xFF22C55E))

        key.contains("gift") || key.contains("toy") || key.contains("fest") ->
            listOf(Color(0xFF7C2D12), Color(0xFFC2410C), Color(0xFFEA580C))

        key.contains("medicine") || key.contains("health") ->
            listOf(Color(0xFF134E4A), Color(0xFF0F766E), Color(0xFF14B8A6))

        key.contains("home") || key.contains("decor") ->
            listOf(Color(0xFF431407), Color(0xFF9A3412), Color(0xFFF97316))

        else -> listOf(Violet950, Violet800, Color(0xFF6D28D9))
    }
}

private fun getBannerEmoji(type: String): String {
    val key = type.lowercase()
    return when {
        key.contains("fashion") || key.contains("clothes") -> "👗 🛍️"
        key.contains("mobile") || key.contains("phone") -> "📱 ⚡"
        key.contains("grocery") || key.contains("veg") -> "🥦 🛒"
        key.contains("electronics") || key.contains("laptop") -> "🎧 💻"
        key.contains("electrical") -> "⚡ 💡"
        key.contains("plumber") -> "🔧 🚰"
        key.contains("food") -> "🍕 🍛"
        key.contains("shoes") || key.contains("footwear") -> "👟 👞"
        key.contains("beauty") -> "💄 ✨"
        key.contains("sport") -> "⚽ 🏋️"
        key.contains("gift") || key.contains("toy") -> "🎁 🧸"
        key.contains("medicine") || key.contains("health") -> "💊 🩺"
        key.contains("home") || key.contains("decor") -> "🛋️ 🏡"
        key.contains("fest") -> "🎁 🎆"
        else -> "📱 🎧 👟"
    }
}

