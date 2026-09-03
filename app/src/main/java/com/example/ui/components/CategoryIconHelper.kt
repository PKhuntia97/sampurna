package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Toys
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.Violet600
import com.example.ui.theme.Violet900

data class PresetIconOption(
    val key: String,
    val title: String,
    val emoji: String,
    val colorHex: Long
)

val CATEGORY_PRESET_ICONS = listOf(
    PresetIconOption("grocery", "Grocery & Kirana", "🛒", 0xFF16A34A),
    PresetIconOption("clothes", "Fashion & Clothes", "👕", 0xFF8B5CF6),
    PresetIconOption("mobile", "Smartphones & Tablets", "📱", 0xFF2563EB),
    PresetIconOption("electronics", "Electronics & IT", "💻", 0xFF7C3AED),
    PresetIconOption("food", "Food & Dining", "🍔", 0xFFEA580C),
    PresetIconOption("medicine", "Pharmacy & Health", "💊", 0xFFDC2626),
    PresetIconOption("home", "Home & Living", "🏠", 0xFF4F46E5),
    PresetIconOption("kitchen", "Kitchen & Dining", "🍳", 0xFF059669),
    PresetIconOption("shoes", "Footwear & Shoes", "👟", 0xFFF59E0B),
    PresetIconOption("beauty", "Beauty & Personal Care", "💄", 0xFFEC4899),
    PresetIconOption("jewelry", "Jewelry & Accessories", "💍", 0xFFD97706),
    PresetIconOption("electrical", "Electrical & Lighting", "⚡", 0xFFF59E0B),
    PresetIconOption("plumber", "Hardware & Plumbing", "🔧", 0xFF0284C7),
    PresetIconOption("sports", "Sports & Gym", "⚽", 0xFF059669),
    PresetIconOption("toys", "Baby & Toys", "🧸", 0xFFD97706),
    PresetIconOption("books", "Books & Stationery", "📚", 0xFF6366F1),
    PresetIconOption("gift", "Gift Cards & Hampers", "🎁", 0xFFE11D48),
    PresetIconOption("more", "More Categories", "📦", 0xFF64748B)
)

val SUBCATEGORY_PRESET_ICONS = listOf(
    PresetIconOption("shirt", "Shirts & Tops", "👔", 0xFF0284C7),
    PresetIconOption("tshirt", "T-Shirts", "👕", 0xFFDC2626),
    PresetIconOption("jeans", "Jeans & Trousers", "👖", 0xFF4338CA),
    PresetIconOption("kurti", "Kurtis & Ethnic", "👘", 0xFFDB2777),
    PresetIconOption("frock", "Dresses & Frocks", "👗", 0xFFE11D48),
    PresetIconOption("saree", "Sarees & Traditional", "🥻", 0xFF9333EA),
    PresetIconOption("innerwear", "Innerwear & Loungewear", "👙", 0xFFF43F5E),
    PresetIconOption("smartphone", "Smartphones", "📱", 0xFF2563EB),
    PresetIconOption("headphone", "Headphones & Audio", "🎧", 0xFF7C3AED),
    PresetIconOption("watch", "Smartwatches", "⌚", 0xFFEA580C),
    PresetIconOption("laptop", "Laptops & PCs", "💻", 0xFF334155),
    PresetIconOption("tv", "Smart TVs", "📺", 0xFF4C1D95),
    PresetIconOption("camera", "Cameras", "📷", 0xFF059669),
    PresetIconOption("game", "Gaming & Consoles", "🎮", 0xFF2563EB),
    PresetIconOption("veg", "Fresh Vegetables", "🥦", 0xFF16A34A),
    PresetIconOption("fruits", "Fresh Fruits", "🍎", 0xFFEA580C),
    PresetIconOption("dairy", "Milk & Dairy", "🥛", 0xFF0284C7),
    PresetIconOption("snacks", "Snacks & Munchies", "🍿", 0xFFF59E0B),
    PresetIconOption("grain", "Atta, Rice & Dal", "🌾", 0xFFD97706),
    PresetIconOption("noodles", "Instant Food", "🍜", 0xFFE11D48),
    PresetIconOption("drink", "Cold Drinks & Juices", "🥤", 0xFF059669),
    PresetIconOption("umbrella", "Rainwear & Umbrella", "☂️", 0xFF2563EB),
    PresetIconOption("shoes", "Sneakers & Casuals", "👟", 0xFFEA580C),
    PresetIconOption("makeup", "Makeup & Cosmetics", "💄", 0xFFEC4899),
    PresetIconOption("perfume", "Deos & Fragrance", "🧴", 0xFF8B5CF6),
    PresetIconOption("bedsheet", "Bedding & Linen", "🛏️", 0xFF7C3AED),
    PresetIconOption("cookware", "Pots & Cookware", "🍳", 0xFF059669),
    PresetIconOption("electrical", "Wires & Switches", "⚡", 0xFFF59E0B),
    PresetIconOption("plumber", "Pipes & Taps", "🔧", 0xFF0284C7)
)

@Composable
fun CategoryIconBadge(
    iconType: String,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    size: Dp = 48.dp,
    isSelected: Boolean = false
) {
    val (backgroundColor, iconVector, emojiFallback) = getCategoryVisuals(iconType)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) Brush.linearGradient(listOf(Violet900, Violet600))
                else Brush.linearGradient(listOf(backgroundColor.copy(alpha = 0.15f), backgroundColor.copy(alpha = 0.05f)))
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank() && (imageUrl.startsWith("http") || imageUrl.startsWith("content://") || imageUrl.startsWith("file://"))) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        } else if (iconVector != null) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = if (isSelected) Color.White else backgroundColor,
                modifier = Modifier.size(size * 0.55f)
            )
        } else {
            Text(
                text = emojiFallback,
                fontSize = (size.value * 0.45f).sp
            )
        }
    }
}

@Composable
fun SubCategoryVisualBadge(
    iconType: String,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    size: Dp = 56.dp
) {
    val (color, emoji, imageVector) = getSubCategoryVisuals(iconType)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(18.dp))
            .background(color.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank() && (imageUrl.startsWith("http") || imageUrl.startsWith("content://") || imageUrl.startsWith("file://"))) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )
        } else if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(size * 0.55f)
            )
        } else {
            Text(
                text = emoji,
                fontSize = (size.value * 0.48f).sp
            )
        }
    }
}

@Composable
fun ProductVisualBadge(
    iconType: String,
    modifier: Modifier = Modifier,
    imageUrl: String? = null
) {
    val (emoji, bgGrad) = getProductVisuals(iconType)
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(bgGrad)),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank() && (imageUrl.startsWith("http") || imageUrl.startsWith("content://") || imageUrl.startsWith("file://"))) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(text = emoji, fontSize = 42.sp)
        }
    }
}

fun getCategoryVisuals(type: String): Triple<Color, ImageVector?, String> {
    val clean = type.lowercase().trim()
    return when {
        clean.contains("gift") -> Triple(Color(0xFFE11D48), Icons.Default.CardGiftcard, "🎁")
        clean.contains("grocery") || clean.contains("kirana") || clean.contains("ration") -> Triple(Color(0xFF16A34A), Icons.Default.ShoppingCart, "🛒")
        clean.contains("mobile") || clean.contains("phone") || clean.contains("tablet") -> Triple(Color(0xFF2563EB), Icons.Default.PhoneAndroid, "📱")
        clean.contains("electronic") || clean.contains("computer") || clean.contains("laptop") -> Triple(Color(0xFF7C3AED), Icons.Default.Computer, "💻")
        clean.contains("electric") || clean.contains("power") || clean.contains("light") -> Triple(Color(0xFFF59E0B), Icons.Default.ElectricBolt, "⚡")
        clean.contains("plumb") || clean.contains("hardware") || clean.contains("tool") -> Triple(Color(0xFF0284C7), Icons.Default.Build, "🔧")
        clean.contains("food") || clean.contains("restaurant") || clean.contains("dine") -> Triple(Color(0xFFEA580C), Icons.Default.Fastfood, "🍔")
        clean.contains("sport") || clean.contains("gym") || clean.contains("fitness") -> Triple(Color(0xFF059669), Icons.Default.SportsSoccer, "⚽")
        clean.contains("medicine") || clean.contains("pharmacy") || clean.contains("health") -> Triple(Color(0xFFDC2626), Icons.Default.LocalPharmacy, "💊")
        clean.contains("toy") || clean.contains("baby") || clean.contains("kid") -> Triple(Color(0xFFD97706), Icons.Default.Toys, "🧸")
        clean.contains("home") || clean.contains("living") || clean.contains("decor") -> Triple(Color(0xFF4F46E5), Icons.Default.Home, "🏠")
        clean.contains("kitchen") || clean.contains("cook") -> Triple(Color(0xFF059669), Icons.Default.Kitchen, "🍳")
        clean.contains("shoe") || clean.contains("footwear") || clean.contains("sandal") -> Triple(Color(0xFFF59E0B), Icons.Default.FitnessCenter, "👟")
        clean.contains("cloth") || clean.contains("fashion") || clean.contains("wear") || clean.contains("apparel") -> Triple(Color(0xFF8B5CF6), Icons.Default.Checkroom, "👕")
        clean.contains("beauty") || clean.contains("makeup") || clean.contains("cosmetic") -> Triple(Color(0xFFEC4899), Icons.Default.Face, "💄")
        clean.contains("jewel") || clean.contains("ornament") -> Triple(Color(0xFFD97706), Icons.Default.AutoAwesome, "💍")
        clean.contains("book") || clean.contains("stationery") || clean.contains("pen") -> Triple(Color(0xFF6366F1), Icons.Default.MenuBook, "📚")
        clean.contains("car") || clean.contains("auto") || clean.contains("bike") -> Triple(Color(0xFF0284C7), Icons.Default.DirectionsCar, "🚗")
        clean.contains("pet") -> Triple(Color(0xFF10B981), Icons.Default.Pets, "🐾")
        clean.contains("game") -> Triple(Color(0xFF2563EB), Icons.Default.SportsEsports, "🎮")
        clean.contains("watch") -> Triple(Color(0xFFEA580C), Icons.Default.Watch, "⌚")
        clean.contains("more") -> Triple(Color(0xFF64748B), Icons.Default.MoreHoriz, "📦")
        else -> Triple(Color(0xFF7C3AED), Icons.Default.ShoppingBag, "🛍️")
    }
}

fun getSubCategoryVisuals(type: String): Triple<Color, String, ImageVector?> {
    val clean = type.lowercase().trim()
    return when {
        clean.contains("umbrella") || clean.contains("rain") -> Triple(Color(0xFF2563EB), "☂️", null)
        clean.contains("formal_shirt") || clean.contains("shirt") -> Triple(Color(0xFF0284C7), "👔", null)
        clean.contains("tshirt") || clean.contains("t-shirt") -> Triple(Color(0xFFDC2626), "👕", null)
        clean.contains("jeans") || clean.contains("pant") || clean.contains("trouser") -> Triple(Color(0xFF4338CA), "👖", null)
        clean.contains("frock") || clean.contains("dress") || clean.contains("gown") -> Triple(Color(0xFFE11D48), "👗", null)
        clean.contains("kurti") || clean.contains("salwar") || clean.contains("suit") -> Triple(Color(0xFFDB2777), "👘", null)
        clean.contains("saree") || clean.contains("lehenga") -> Triple(Color(0xFF9333EA), "🥻", null)
        clean.contains("innerwear") || clean.contains("bra") || clean.contains("panty") || clean.contains("brief") -> Triple(Color(0xFFF43F5E), "👙", null)
        clean.contains("casual") || clean.contains("top") -> Triple(Color(0xFF7C3AED), "👚", null)
        clean.contains("apple") || clean.contains("iphone") -> Triple(Color(0xFF0F172A), "🍎", null)
        clean.contains("smartphone") || clean.contains("mobile") -> Triple(Color(0xFF2563EB), "📱", Icons.Default.PhoneAndroid)
        clean.contains("veg") || clean.contains("sabzi") -> Triple(Color(0xFF16A34A), "🥦", null)
        clean.contains("fruit") -> Triple(Color(0xFFEA580C), "🍎", null)
        clean.contains("dairy") || clean.contains("milk") || clean.contains("paneer") || clean.contains("curd") -> Triple(Color(0xFF0284C7), "🥛", null)
        clean.contains("snack") || clean.contains("biscuit") || clean.contains("namkeen") -> Triple(Color(0xFFF59E0B), "🍿", null)
        clean.contains("drink") || clean.contains("juice") || clean.contains("beverage") -> Triple(Color(0xFF059669), "🥤", null)
        clean.contains("grain") || clean.contains("rice") || clean.contains("atta") || clean.contains("dal") -> Triple(Color(0xFFD97706), "🌾", null)
        clean.contains("noodle") || clean.contains("maggi") || clean.contains("pasta") || clean.contains("instant") -> Triple(Color(0xFFE11D48), "🍜", null)
        clean.contains("oil") || clean.contains("ghee") || clean.contains("masala") || clean.contains("spice") -> Triple(Color(0xFFEA580C), "🌻", null)
        clean.contains("tv") || clean.contains("television") -> Triple(Color(0xFF4C1D95), "📺", Icons.Default.Tv)
        clean.contains("laptop") || clean.contains("desktop") -> Triple(Color(0xFF334155), "💻", Icons.Default.Computer)
        clean.contains("headphone") || clean.contains("earbud") || clean.contains("audio") || clean.contains("speaker") -> Triple(Color(0xFF7C3AED), "🎧", Icons.Default.Headphones)
        clean.contains("watch") -> Triple(Color(0xFFEA580C), "⌚", Icons.Default.Watch)
        clean.contains("camera") -> Triple(Color(0xFF059669), "📷", null)
        clean.contains("game") || clean.contains("playstation") || clean.contains("xbox") -> Triple(Color(0xFF2563EB), "🎮", Icons.Default.SportsEsports)
        clean.contains("shoe") || clean.contains("sneaker") || clean.contains("boot") -> Triple(Color(0xFFEA580C), "👟", null)
        clean.contains("sandal") || clean.contains("slipper") || clean.contains("chappal") -> Triple(Color(0xFFD97706), "🩴", null)
        clean.contains("food") || clean.contains("biryani") || clean.contains("pizza") || clean.contains("burger") -> Triple(Color(0xFFF97316), "🍕", Icons.Default.Fastfood)
        clean.contains("electrical") || clean.contains("switch") || clean.contains("wire") -> Triple(Color(0xFFF59E0B), "⚡", Icons.Default.ElectricBolt)
        clean.contains("plumber") || clean.contains("pipe") || clean.contains("tap") -> Triple(Color(0xFF0284C7), "🔧", Icons.Default.Build)
        clean.contains("bedsheet") || clean.contains("pillow") || clean.contains("curtain") || clean.contains("towel") -> Triple(Color(0xFF7C3AED), "🛏️", null)
        clean.contains("makeup") || clean.contains("lipstick") || clean.contains("cream") -> Triple(Color(0xFFEC4899), "💄", null)
        clean.contains("perfume") || clean.contains("deo") || clean.contains("scent") -> Triple(Color(0xFF8B5CF6), "🧴", null)
        clean.contains("soap") || clean.contains("shampoo") || clean.contains("wash") -> Triple(Color(0xFF06B6D4), "🧼", null)
        clean.contains("book") || clean.contains("notebook") || clean.contains("copy") -> Triple(Color(0xFF6366F1), "📚", Icons.Default.MenuBook)
        clean.contains("pen") || clean.contains("pencil") || clean.contains("geometry") -> Triple(Color(0xFF3B82F6), "✏️", null)
        clean.contains("cookware") || clean.contains("pan") || clean.contains("kadai") || clean.contains("pot") -> Triple(Color(0xFF059669), "🍳", null)
        else -> Triple(Color(0xFF7C3AED), "✨", null)
    }
}

fun getProductVisuals(type: String): Pair<String, List<Color>> {
    val clean = type.lowercase().trim()
    return when {
        clean.contains("mobile") || clean.contains("phone") -> Pair("📱", listOf(Color(0xFFEDE9FE), Color(0xFFDDD6FE)))
        clean.contains("headphone") || clean.contains("earbud") -> Pair("🎧", listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0)))
        clean.contains("watch") -> Pair("⌚", listOf(Color(0xFFFFF7ED), Color(0xFFFFEDD5)))
        clean.contains("mixer") || clean.contains("grinder") -> Pair("🌪️", listOf(Color(0xFFF8FAFC), Color(0xFFF1F5F9)))
        clean.contains("grocery") || clean.contains("pack") -> Pair("🛍️", listOf(Color(0xFFECFDF5), Color(0xFFD1FAE5)))
        clean.contains("oil") || clean.contains("ghee") -> Pair("🌻", listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A)))
        clean.contains("shirt") -> Pair("👔", listOf(Color(0xFFEFF6FF), Color(0xFFDBEAFE)))
        clean.contains("kurti") || clean.contains("saree") -> Pair("👗", listOf(Color(0xFFFDF2F8), Color(0xFFFCE7F3)))
        clean.contains("jeans") -> Pair("👖", listOf(Color(0xFFEEF2FF), Color(0xFFE0E7FF)))
        clean.contains("shoe") || clean.contains("sneaker") -> Pair("👟", listOf(Color(0xFFFFF7ED), Color(0xFFFFEDD5)))
        clean.contains("bedsheet") -> Pair("🛏️", listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF)))
        clean.contains("rice") || clean.contains("atta") -> Pair("🌾", listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A)))
        clean.contains("tv") -> Pair("📺", listOf(Color(0xFFEDE9FE), Color(0xFFDDD6FE)))
        clean.contains("laptop") -> Pair("💻", listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0)))
        else -> Pair("📦", listOf(Color(0xFFF8FAFC), Color(0xFFF1F5F9)))
    }
}
