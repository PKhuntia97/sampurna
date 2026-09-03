package com.example.data.local

import com.example.data.local.entity.AddressEntity
import com.example.data.local.entity.AdminUserEntity
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.CategoryBannerEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.CustomerProductCancellationEntity
import com.example.data.local.entity.DeliveryPartnerEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.NotificationPreferenceEntity
import com.example.data.local.entity.OfferEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ProductVariantEntity
import com.example.data.local.entity.RecentlyViewedEntity
import com.example.data.local.entity.SellerCategoryAssignmentEntity
import com.example.data.local.entity.SellerEntity
import com.example.data.local.entity.SubCategoryEntity
import com.example.data.local.entity.SystemSettingEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.WishlistEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseInitializer {

    suspend fun seedDatabase(database: SampurnaDatabase) = withContext(Dispatchers.IO) {
        // Only seed if empty
        if (database.adminDao().getAdminCount() > 0) return@withContext

        // 1. Seed System Settings (Sampurna 2% Commission, Delivery Fee, COD, Gateway, Cancellation rules)
        val initialSettings = listOf(
            SystemSettingEntity(key = "sampurna_commission_percent", value = "2.0"),
            SystemSettingEntity(key = "delivery_base_km", value = "5.0"),
            SystemSettingEntity(key = "delivery_base_fee", value = "10.0"),
            SystemSettingEntity(key = "delivery_per_km_extra", value = "3.0"),
            SystemSettingEntity(key = "cod_enabled", value = "true"),
            SystemSettingEntity(key = "cod_fee", value = "10.0"),
            SystemSettingEntity(key = "payment_gateway_provider", value = "Razorpay"),
            SystemSettingEntity(key = "payment_gateway_key_id", value = "rzp_live_sampurna_78x9"),
            SystemSettingEntity(key = "payment_gateway_secret", value = "sk_live_hidden_secure_9281"),
            SystemSettingEntity(key = "payment_gateway_merchant_id", value = "MERCHANT_SAMPURNA_ODISHA"),
            SystemSettingEntity(key = "payment_gateway_webhook", value = "https://api.sampurna.store/webhook/v1/payment"),
            SystemSettingEntity(key = "payment_gateway_mode", value = "TEST"),
            SystemSettingEntity(key = "repeated_cancel_threshold", value = "2"),
            SystemSettingEntity(key = "repeated_cancel_adjustment_percent", value = "1.0")
        )
        initialSettings.forEach { database.systemSettingDao().setSetting(it) }

        // 2. Seed Admin (Pranaya Khuntia)
        database.adminDao().insertAdmin(
            AdminUserEntity(
                email = "pranayakhuntia85@gmail.com",
                passwordHash = "Pranaya@1997",
                name = "Pranaya Khuntia (Admin)",
                role = "super_admin"
            )
        )

        // 3. Seed Default Categories
        val defaultCategories = listOf(
            CategoryEntity(name = "Gift Cards", iconType = "gift", displayOrder = 1),
            CategoryEntity(name = "Grocery Store", iconType = "grocery", displayOrder = 2),
            CategoryEntity(name = "Mobile", iconType = "mobile", displayOrder = 3),
            CategoryEntity(name = "Electronics", iconType = "electronics", displayOrder = 4),
            CategoryEntity(name = "Electrical Service", iconType = "electrical", displayOrder = 5),
            CategoryEntity(name = "Plumber Service", iconType = "plumber", displayOrder = 6),
            CategoryEntity(name = "Food Delivery", iconType = "food", displayOrder = 7),
            CategoryEntity(name = "Sports", iconType = "sports", displayOrder = 8),
            CategoryEntity(name = "Medicine", iconType = "medicine", displayOrder = 9),
            CategoryEntity(name = "Toys & Baby", iconType = "toys", displayOrder = 10),
            CategoryEntity(name = "Home & Living", iconType = "home", displayOrder = 11),
            CategoryEntity(name = "Shoes", iconType = "shoes", displayOrder = 12),
            CategoryEntity(name = "Clothes", iconType = "clothes", displayOrder = 13),
            CategoryEntity(name = "More Categories", iconType = "more", displayOrder = 14)
        )
        val categoryIds = database.categoryDao().insertCategories(defaultCategories)

        val categoryMap = defaultCategories.mapIndexed { index, cat ->
            cat.name to categoryIds[index]
        }.toMap()

        // 4. Seed Sub-Categories
        val subCategories = mutableListOf<SubCategoryEntity>()

        // Clothes subcategories
        categoryMap["Clothes"]?.let { clothesId ->
            val clothesSubs = listOf(
                "Monsoon" to "umbrella",
                "Shirts" to "shirt",
                "Jeans" to "jeans",
                "Frocks" to "frock",
                "Kurti" to "kurti",
                "Bra & Panty" to "innerwear",
                "Full Shirt" to "formal_shirt",
                "T-Shirts" to "tshirt",
                "Dresses" to "dress",
                "Casual Wear" to "casual"
            )
            clothesSubs.forEachIndexed { idx, (name, icon) ->
                subCategories.add(
                    SubCategoryEntity(
                        categoryId = clothesId,
                        name = name,
                        iconType = icon,
                        displayOrder = idx + 1
                    )
                )
            }
        }

        // Mobile subcategories
        categoryMap["Mobile"]?.let { mobileId ->
            val mobileSubs = listOf(
                "iPhone" to "apple",
                "vivo" to "smartphone",
                "OPPO" to "smartphone",
                "POCO" to "smartphone",
                "Samsung" to "smartphone",
                "Motorola" to "smartphone",
                "realme" to "smartphone",
                "Nothing" to "smartphone"
            )
            mobileSubs.forEachIndexed { idx, (name, icon) ->
                subCategories.add(
                    SubCategoryEntity(
                        categoryId = mobileId,
                        name = name,
                        iconType = icon,
                        displayOrder = idx + 1
                    )
                )
            }
        }

        // Grocery subcategories
        categoryMap["Grocery Store"]?.let { groceryId ->
            val grocerySubs = listOf(
                "Fresh Veggies" to "veg",
                "Fruits" to "fruits",
                "Dairy & Milk" to "dairy",
                "Snacks & Munchies" to "snacks",
                "Beverages" to "drink",
                "Rice & Atta" to "grain",
                "Instant Foods" to "noodles"
            )
            grocerySubs.forEachIndexed { idx, (name, icon) ->
                subCategories.add(
                    SubCategoryEntity(
                        categoryId = groceryId,
                        name = name,
                        iconType = icon,
                        displayOrder = idx + 1
                    )
                )
            }
        }

        // Electronics subcategories
        categoryMap["Electronics"]?.let { elecId ->
            val elecSubs = listOf(
                "Smart TVs" to "tv",
                "Laptops" to "laptop",
                "Audio & Headphones" to "headphone",
                "Smart Watches" to "watch",
                "Cameras" to "camera",
                "Gaming Consoles" to "game"
            )
            elecSubs.forEachIndexed { idx, (name, icon) ->
                subCategories.add(
                    SubCategoryEntity(
                        categoryId = elecId,
                        name = name,
                        iconType = icon,
                        displayOrder = idx + 1
                    )
                )
            }
        }

        // Electrical & Plumber Services
        categoryMap["Electrical Service"]?.let { elecServId ->
            listOf("Fan Repair", "House Wiring", "AC Service", "Inverter Setup").forEachIndexed { idx, name ->
                subCategories.add(SubCategoryEntity(categoryId = elecServId, name = name, iconType = "electrical", displayOrder = idx + 1))
            }
        }
        categoryMap["Plumber Service"]?.let { plumbId ->
            listOf("Pipe Repair", "Tap Fitting", "Tank Cleaning", "Bath Fitting").forEachIndexed { idx, name ->
                subCategories.add(SubCategoryEntity(categoryId = plumbId, name = name, iconType = "plumber", displayOrder = idx + 1))
            }
        }

        // Food Delivery & Shoes
        categoryMap["Food Delivery"]?.let { foodId ->
            listOf("Biryani", "Pizza", "Burgers", "Bakery & Cakes", "Rolls").forEachIndexed { idx, name ->
                subCategories.add(SubCategoryEntity(categoryId = foodId, name = name, iconType = "food", displayOrder = idx + 1))
            }
        }
        categoryMap["Shoes"]?.let { shoesId ->
            listOf("Sneakers", "Formal Shoes", "Sports Shoes", "Sandals & Floaters", "Ethnic Wear").forEachIndexed { idx, name ->
                subCategories.add(SubCategoryEntity(categoryId = shoesId, name = name, iconType = "shoes", displayOrder = idx + 1))
            }
        }

        val subCategoryIds = database.subCategoryDao().insertSubCategories(subCategories)
        val subCategoryMap = subCategories.mapIndexed { index, subCat ->
            subCat.name to subCategoryIds[index]
        }.toMap()

        // 5. Seed Banners
        val banners = mutableListOf<CategoryBannerEntity>()
        banners.add(
            CategoryBannerEntity(
                categoryId = null,
                title = "MEGA SALE",
                subtitle = "On Smart Gadgets, Lifestyle & Daily Needs",
                tag = "SPECIAL OFFER",
                discountText = "UP TO 60% OFF",
                bannerType = "mega_sale",
                displayOrder = 1
            )
        )
        banners.add(
            CategoryBannerEntity(
                categoryId = null,
                title = "FESTIVAL CARNIVAL",
                subtitle = "Top Brands at Unbeatable Wholesale Rates",
                tag = "LIMITED TIME",
                discountText = "FLAT 50% OFF",
                bannerType = "fest",
                displayOrder = 2
            )
        )
        banners.add(
            CategoryBannerEntity(
                categoryId = null,
                title = "SUPER SAVER WEEK",
                subtitle = "Instant Doorstep Delivery & 100% Assurance",
                tag = "EXTRA REWARDS",
                discountText = "FREE DELIVERY",
                bannerType = "special",
                displayOrder = 3
            )
        )

        categoryMap["Clothes"]?.let { clothesId ->
            banners.add(
                CategoryBannerEntity(
                    categoryId = clothesId,
                    title = "CLOTHES MEGA SALE",
                    subtitle = "Trendy Monsoon, Ethnic & Casual Collections",
                    tag = "FASHION FEST",
                    discountText = "UP TO 70% OFF",
                    bannerType = "fashion_mega_sale",
                    displayOrder = 1
                )
            )
        }

        categoryMap["Mobile"]?.let { mobileId ->
            banners.add(
                CategoryBannerEntity(
                    categoryId = mobileId,
                    title = "MOBILE BONANZA",
                    subtitle = "5G Flagships, Budget Killers & Smart Accessories",
                    tag = "TECH CARNIVAL",
                    discountText = "EXTRA ₹3,000 OFF",
                    bannerType = "mobile_mega_sale",
                    displayOrder = 1
                )
            )
        }

        categoryMap["Grocery"]?.let { groceryId ->
            banners.add(
                CategoryBannerEntity(
                    categoryId = groceryId,
                    title = "GROCERY SUPER SAVER",
                    subtitle = "Fresh Vegetables, Fruits, Dairy & Daily Staples",
                    tag = "DAILY SAVINGS",
                    discountText = "UP TO 50% OFF",
                    bannerType = "grocery_mega_sale",
                    displayOrder = 1
                )
            )
        }

        categoryMap["Electronics"]?.let { elecId ->
            banners.add(
                CategoryBannerEntity(
                    categoryId = elecId,
                    title = "ELECTRONICS DHAMAKA",
                    subtitle = "Smart TVs, Laptops, Audio Gear & Smartwatches",
                    tag = "MEGA TECH SALE",
                    discountText = "UP TO 65% OFF",
                    bannerType = "electronics_mega_sale",
                    displayOrder = 1
                )
            )
        }

        categoryMap["Electrical Service"]?.let { elecServId ->
            banners.add(
                CategoryBannerEntity(
                    categoryId = elecServId,
                    title = "ELECTRICAL CARE & REPAIR",
                    subtitle = "Certified Electricians, Safe Wiring & Appliance Service",
                    tag = "DOORSTEP SERVICE",
                    discountText = "STARTS @ ₹199",
                    bannerType = "electrical_service",
                    displayOrder = 1
                )
            )
        }

        categoryMap["Plumber Service"]?.let { plumbId ->
            banners.add(
                CategoryBannerEntity(
                    categoryId = plumbId,
                    title = "PLUMBING EXPERTS AT DOORSTEP",
                    subtitle = "Leak Repair, Tap Fitting & Tank Cleaning Solutions",
                    tag = "VERIFIED PROS",
                    discountText = "INSTANT VISIT",
                    bannerType = "plumber_service",
                    displayOrder = 1
                )
            )
        }

        categoryMap["Food Delivery"]?.let { foodId ->
            banners.add(
                CategoryBannerEntity(
                    categoryId = foodId,
                    title = "FOOD DELIVERY BONANZA",
                    subtitle = "Hot Biryani, Tasty Pizzas, Rolls & Local Sweets",
                    tag = "FOOD FEST",
                    discountText = "FLAT ₹100 OFF",
                    bannerType = "food_delivery",
                    displayOrder = 1
                )
            )
        }

        categoryMap["Shoes"]?.let { shoesId ->
            banners.add(
                CategoryBannerEntity(
                    categoryId = shoesId,
                    title = "FOOTWEAR CARNIVAL",
                    subtitle = "Sneakers, Formal Shoes, Sports & Daily Wear",
                    tag = "STEP IN STYLE",
                    discountText = "UP TO 55% OFF",
                    bannerType = "shoes_mega_sale",
                    displayOrder = 1
                )
            )
        }

        database.bannerDao().insertBanners(banners)

        // 6. Seed Sellers & Category Assignments
        val seller1Id = database.sellerDao().insertSeller(
            SellerEntity(
                sellerName = "Bikash Mohapatra",
                businessName = "Keonjhar Digital & Mobile Hub",
                mobile = "9437012345",
                email = "keonjhardigital@sampurna.com",
                businessAddress = "Main Market Road, Near Town Hall, Keonjhar, Odisha 758001",
                storeInfo = "Authorized regional distributor for genuine Smartphones, Gadgets & Audio Gear.",
                status = "ACTIVE"
            )
        )

        val seller2Id = database.sellerDao().insertSeller(
            SellerEntity(
                sellerName = "Sunita Behera",
                businessName = "Odisha Handloom & Fashion House",
                mobile = "9861054321",
                email = "odishafashion@sampurna.com",
                businessAddress = "Station Road, Barbil, Keonjhar, Odisha 758035",
                storeInfo = "Authentic Sambalpuri Sarees, Premium Kurtis, and Modern Men & Women Casuals.",
                status = "ACTIVE"
            )
        )

        val seller3Id = database.sellerDao().insertSeller(
            SellerEntity(
                sellerName = "Ramesh Sahu",
                businessName = "Sampurna Fresh Daily Mart",
                mobile = "9439088776",
                email = "freshdaily@sampurna.com",
                businessAddress = "Old Bus Stand, Keonjhargarh, Odisha 758002",
                storeInfo = "Direct farm-fresh vegetables, organic staples, and daily packaged essentials.",
                status = "ACTIVE"
            )
        )

        // Assign Categories to Sellers
        categoryMap["Mobile"]?.let { mobileId ->
            database.sellerDao().insertAssignment(
                SellerCategoryAssignmentEntity(sellerId = seller1Id, categoryId = mobileId)
            )
        }
        categoryMap["Electronics"]?.let { elecId ->
            database.sellerDao().insertAssignment(
                SellerCategoryAssignmentEntity(sellerId = seller1Id, categoryId = elecId)
            )
        }
        categoryMap["Clothes"]?.let { clothesId ->
            database.sellerDao().insertAssignment(
                SellerCategoryAssignmentEntity(sellerId = seller2Id, categoryId = clothesId)
            )
        }
        categoryMap["Shoes"]?.let { shoesId ->
            database.sellerDao().insertAssignment(
                SellerCategoryAssignmentEntity(sellerId = seller2Id, categoryId = shoesId)
            )
        }
        categoryMap["Grocery Store"]?.let { groceryId ->
            database.sellerDao().insertAssignment(
                SellerCategoryAssignmentEntity(sellerId = seller3Id, categoryId = groceryId)
            )
        }

        // Helper function for 2% pricing
        // Customer Price = Seller Price + 2% (rounded to nearest integer)
        fun calculateCustomerPrice(sellerPrice: Double): Double {
            return Math.round(sellerPrice * 1.02 * 100.0) / 100.0
        }

        // 7. Seed Products with 2% Formula & Brand/Seller Association
        val products = mutableListOf<ProductEntity>()

        categoryMap["Mobile"]?.let { mobileId ->
            val redmiSellerPrice = 13725.0
            val redmiCustPrice = calculateCustomerPrice(redmiSellerPrice) // ~13999.5 -> 13999.0
            products.add(
                ProductEntity(
                    categoryId = mobileId,
                    subCategoryId = subCategoryMap["POCO"],
                    sellerId = seller1Id,
                    brand = "Xiaomi",
                    name = "Redmi Note 13 5G (Prism Gold)",
                    description = "Super clear 108MP 3X in-sensor zoom camera, ultra-slim 120Hz AMOLED display, MediaTek Dimensity 6080 5G processor with 33W fast charging.",
                    iconType = "mobile",
                    mrp = 18999.0,
                    sellerPrice = 13724.51,
                    price = 13999.0,
                    discount = 26,
                    stock = 45,
                    sku = "REDMI-N13-GLD",
                    warranty = "1 Year Manufacturer Warranty for Phone and 6 Months for Accessories",
                    specifications = "Display: 6.67 inch FHD+ AMOLED 120Hz; Processor: Dimensity 6080; RAM: 8GB; Storage: 256GB; Battery: 5000mAh",
                    rating = 4.3f,
                    ratingCount = 128,
                    tag = "trending",
                    status = "APPROVED"
                )
            )

            products.add(
                ProductEntity(
                    categoryId = mobileId,
                    subCategoryId = subCategoryMap["Samsung"],
                    sellerId = seller1Id,
                    brand = "Samsung",
                    name = "Samsung S24 Ultra AI Edition",
                    description = "Unleash revolutionary Galaxy AI with Circle to Search, Live Call Translation, Note Assist, 200MP camera, and titanium durability.",
                    iconType = "mobile",
                    mrp = 129999.0,
                    sellerPrice = 102940.20,
                    price = 104999.0,
                    discount = 19,
                    stock = 15,
                    sku = "SAM-S24U-TIT",
                    warranty = "1 Year Brand Warranty",
                    specifications = "Display: 6.8 inch Dynamic AMOLED 2X; Processor: Snapdragon 8 Gen 3; RAM: 12GB; Storage: 512GB; Camera: 200MP Quad",
                    rating = 4.8f,
                    ratingCount = 310,
                    tag = "todays_offers",
                    status = "APPROVED"
                )
            )

            products.add(
                ProductEntity(
                    categoryId = mobileId,
                    subCategoryId = subCategoryMap["iPhone"],
                    sellerId = seller1Id,
                    brand = "Apple",
                    name = "iPhone 15 Pro Max Titanium",
                    description = "Forged in titanium and featuring the groundbreaking A17 Pro chip, customizable Action button, and the most powerful iPhone camera system.",
                    iconType = "mobile",
                    mrp = 134900.0,
                    sellerPrice = 117549.02,
                    price = 119900.0,
                    discount = 11,
                    stock = 20,
                    sku = "APL-IP15PM-NT",
                    warranty = "1 Year Apple Care India Warranty",
                    specifications = "Display: 6.7 inch Super Retina XDR OLED; Chip: A17 Pro 3nm; Storage: 256GB; Ceramic Shield Front",
                    rating = 4.9f,
                    ratingCount = 420,
                    tag = "recommended",
                    status = "APPROVED"
                )
            )

            products.add(
                ProductEntity(
                    categoryId = mobileId,
                    subCategoryId = subCategoryMap["POCO"],
                    sellerId = seller1Id,
                    brand = "POCO",
                    name = "POCO X6 Pro 5G (Racing Yellow)",
                    description = "Flagship Dimensity 8300-Ultra processor with WildBoost 2.0 gaming optimization, 64MP OIS triple camera and 67W Turbo Charge.",
                    iconType = "mobile",
                    mrp = 26999.0,
                    sellerPrice = 22548.04,
                    price = 22999.0,
                    discount = 15,
                    stock = 30,
                    sku = "POCO-X6P-YLW",
                    warranty = "1 Year Brand Warranty",
                    specifications = "Display: 1.5K 120Hz Flow AMOLED; RAM: 12GB LPDDR5X; ROM: 512GB UFS 4.0",
                    rating = 4.4f,
                    ratingCount = 89,
                    tag = "new_arrivals",
                    status = "APPROVED"
                )
            )
        }

        categoryMap["Electronics"]?.let { elecId ->
            products.add(
                ProductEntity(
                    categoryId = elecId,
                    subCategoryId = subCategoryMap["Audio & Headphones"],
                    sellerId = seller1Id,
                    brand = "boAt",
                    name = "boAt Rockerz 450 Wireless Headphone",
                    description = "40mm dynamic drivers for HD immersive sound, up to 15 hours non-stop playback, plush ear cushions and easy integrated controls.",
                    iconType = "headphone",
                    mrp = 1999.0,
                    sellerPrice = 1371.57,
                    price = 1399.0,
                    discount = 30,
                    stock = 60,
                    sku = "BOAT-R450-BLK",
                    warranty = "1 Year Brand Replacement Warranty",
                    specifications = "Driver Size: 40mm; Bluetooth: v5.0; Playback: 15 Hours; Weight: 168g",
                    rating = 4.4f,
                    ratingCount = 96,
                    tag = "trending",
                    status = "APPROVED"
                )
            )

            products.add(
                ProductEntity(
                    categoryId = elecId,
                    subCategoryId = subCategoryMap["Smart Watches"],
                    sellerId = seller1Id,
                    brand = "Noise",
                    name = "Noise ColorFit Pulse 3 Smartwatch",
                    description = "1.96-inch TFT massive display, Bluetooth calling with TruSync technology, 100+ sports modes and comprehensive 24/7 health tracking.",
                    iconType = "watch",
                    mrp = 4999.0,
                    sellerPrice = 1469.61,
                    price = 1499.0,
                    discount = 70,
                    stock = 80,
                    sku = "NOISE-P3-JET",
                    warranty = "1 Year Warranty from date of purchase",
                    specifications = "Display: 1.96 inch TFT; Battery: 7 Days normal usage; IP68 Water Resistant",
                    rating = 4.5f,
                    ratingCount = 230,
                    tag = "best_selling",
                    status = "APPROVED"
                )
            )
        }

        categoryMap["Clothes"]?.let { clothesId ->
            products.add(
                ProductEntity(
                    categoryId = clothesId,
                    subCategoryId = subCategoryMap["Shirts"],
                    sellerId = seller2Id,
                    brand = "Highlander",
                    name = "Highlander Men Casual Slim Fit Shirt",
                    description = "100% Breathable pure combed cotton with a sharp spread collar, button placket, and curved hemline for effortless formal and casual styling.",
                    iconType = "shirt",
                    mrp = 1299.0,
                    sellerPrice = 489.22,
                    price = 499.0,
                    discount = 62,
                    stock = 100,
                    sku = "HL-SHIRT-NAVY",
                    warranty = "Sampurna 7 Days Easy Replacement Assurance",
                    specifications = "Fabric: 100% Cotton; Fit: Slim Fit; Sleeve: Long Sleeve; Pattern: Solid",
                    rating = 4.4f,
                    ratingCount = 184,
                    tag = "best_selling",
                    status = "APPROVED"
                )
            )

            products.add(
                ProductEntity(
                    categoryId = clothesId,
                    subCategoryId = subCategoryMap["Kurti"],
                    sellerId = seller2Id,
                    brand = "Jaipuri Libas",
                    name = "Jaipuri Printed Pure Cotton Kurti",
                    description = "Traditional artisanal floral hand-block prints on premium soft cotton fabric, calf length with round neck and three-quarter sleeves.",
                    iconType = "kurti",
                    mrp = 1499.0,
                    sellerPrice = 538.24,
                    price = 549.0,
                    discount = 63,
                    stock = 75,
                    sku = "JL-KURTI-MINT",
                    warranty = "Sampurna Quality Verified",
                    specifications = "Fabric: Pure Cotton; Neck: Round; Length: Calf Length; Wash Care: Machine Wash",
                    rating = 4.6f,
                    ratingCount = 275,
                    tag = "trending",
                    status = "APPROVED"
                )
            )

            products.add(
                ProductEntity(
                    categoryId = clothesId,
                    subCategoryId = subCategoryMap["Jeans"],
                    sellerId = seller2Id,
                    brand = "Flying Machine",
                    name = "Flying Machine Men Tapered Jeans",
                    description = "Lightly washed authentic denim with power-stretch fibers for all-day comfort and modern slim tapered silhouette.",
                    iconType = "jeans",
                    mrp = 2199.0,
                    sellerPrice = 979.41,
                    price = 999.0,
                    discount = 55,
                    stock = 50,
                    sku = "FM-JEANS-DKBLU",
                    warranty = "7 Days Easy Replacement",
                    specifications = "Fabric: 98% Cotton 2% Elastane; Rise: Mid Rise; Fit: Slim Tapered",
                    rating = 4.3f,
                    ratingCount = 88,
                    tag = "new_arrivals",
                    status = "APPROVED"
                )
            )
        }

        categoryMap["Grocery Store"]?.let { groceryId ->
            products.add(
                ProductEntity(
                    categoryId = groceryId,
                    subCategoryId = subCategoryMap["Dairy & Milk"],
                    sellerId = seller3Id,
                    brand = "Sampurna Fresh",
                    name = "Sampurna Grocery Super Saver Pack",
                    description = "Essential monthly family combo: 5kg Basmati Rice, 5kg Premium Chakki Atta, 1L Mustard Oil, and 1kg Tata Salt.",
                    iconType = "grocery_pack",
                    mrp = 969.0,
                    sellerPrice = 685.29,
                    price = 699.0,
                    discount = 28,
                    stock = 120,
                    sku = "SAMP-GROC-PACK",
                    warranty = "100% Freshness Guarantee",
                    specifications = "Includes: Atta, Rice, Oil, Salt; Pack Type: Sealed Carton",
                    rating = 4.2f,
                    ratingCount = 58,
                    tag = "trending",
                    status = "APPROVED"
                )
            )
        }

        val insertedProductIds = database.productDao().insertProducts(products)

        // 8. Seed Demo Customer User
        val customerUserId = database.userDao().insertUser(
            UserEntity(
                name = "Pranaya Khuntia",
                mobile = "9876543210",
                email = "pranayakhuntia85@gmail.com",
                passwordHash = "demo123",
                role = "customer",
                emailVerified = true
            )
        )

        // Seed Customer Notification Preferences
        database.notificationDao().savePreferences(
            NotificationPreferenceEntity(
                userId = customerUserId,
                appEnabled = true,
                emailEnabled = true,
                whatsappEnabled = true,
                smsEnabled = false
            )
        )

        // Seed Customer Addresses
        database.addressDao().insertAddress(
            AddressEntity(
                userId = customerUserId,
                name = "Pranaya Khuntia",
                mobile = "9876543210",
                houseFlat = "Flat 302, Royal Residency",
                streetArea = "College Road, Near Medical Chowk",
                landmark = "Opposite District Hospital",
                city = "Keonjhar",
                district = "Kendujhar",
                state = "Odisha",
                pinCode = "758001",
                latitude = 21.6289,
                longitude = 85.5817,
                addressType = "HOME",
                isDefault = true
            )
        )

        database.addressDao().insertAddress(
            AddressEntity(
                userId = customerUserId,
                name = "Pranaya Khuntia (Office)",
                mobile = "9876543210",
                houseFlat = "Plot No. 45, Tech Park Phase 2",
                streetArea = "Infocity Avenue, Patia",
                landmark = "Near KIIT Square",
                city = "Bhubaneswar",
                district = "Khurda",
                state = "Odisha",
                pinCode = "751024",
                latitude = 20.3541,
                longitude = 85.8189,
                addressType = "WORK",
                isDefault = false
            )
        )

        // Seed Demo Wishlist Items
        if (insertedProductIds.isNotEmpty()) {
            database.wishlistDao().addToWishlist(
                WishlistEntity(
                    userId = customerUserId,
                    productId = insertedProductIds[0] // Redmi Note 13
                )
            )
            if (insertedProductIds.size > 2) {
                database.wishlistDao().addToWishlist(
                    WishlistEntity(
                        userId = customerUserId,
                        productId = insertedProductIds[2] // iPhone 15
                    )
                )
            }
        }

        // Seed Demo Recently Viewed Items
        if (insertedProductIds.size > 1) {
            database.recentlyViewedDao().recordRecentlyViewed(
                RecentlyViewedEntity(
                    userId = customerUserId,
                    productId = insertedProductIds[1] // Samsung S24
                )
            )
            database.recentlyViewedDao().recordRecentlyViewed(
                RecentlyViewedEntity(
                    userId = customerUserId,
                    productId = insertedProductIds[0] // Redmi Note 13
                )
            )
        }

        // 9. Seed Dynamic Offers
        val offers = listOf(
            OfferEntity(
                code = "SAMPURNA50",
                title = "Flat ₹50 OFF on First Order",
                description = "Get flat ₹50 discount on your inaugural order above ₹299 across all categories.",
                discountPercent = 15,
                maxDiscount = 50.0,
                minOrderValue = 299.0,
                offerType = "AVAILABLE",
                badgeColor = "orange"
            ),
            OfferEntity(
                code = "FESTIVE150",
                title = "Mega Festive Saving Coupon",
                description = "Flat ₹150 OFF on Fashion, Clothes & Electronics above ₹999.",
                discountPercent = 20,
                maxDiscount = 150.0,
                minOrderValue = 999.0,
                offerType = "COUPON",
                badgeColor = "violet"
            ),
            OfferEntity(
                code = "EXCLUSIVE10",
                title = "Personalized Loyalty Bonus",
                description = "Special personalized 10% instant discount curated for your favorite tech gadgets.",
                discountPercent = 10,
                maxDiscount = 300.0,
                minOrderValue = 1499.0,
                offerType = "PERSONALIZED",
                badgeColor = "emerald"
            ),
            OfferEntity(
                code = "FLASH24",
                title = "Flash Deal: Ends in 24 Hours",
                description = "Extra ₹100 instant cashback voucher expiring today midnight on orders above ₹799.",
                discountPercent = 25,
                maxDiscount = 100.0,
                minOrderValue = 799.0,
                validUntil = System.currentTimeMillis() + (18 * 60 * 60 * 1000L),
                offerType = "EXPIRING",
                badgeColor = "rose"
            )
        )
        database.offerDao().insertOffers(offers)

        // 10. Seed Notifications
        val notifications = listOf(
            NotificationEntity(
                userId = customerUserId,
                title = "Welcome to Sampurna! 🎉",
                message = "Explore thousands of products with genuine seller assurance and 2% transparent pricing.",
                type = "ACCOUNT"
            ),
            NotificationEntity(
                userId = customerUserId,
                title = "Special Offer for You: SAMPURNA50",
                message = "Apply code SAMPURNA50 at cart to claim ₹50 instant discount on your order.",
                type = "OFFERS"
            ),
            NotificationEntity(
                userId = customerUserId,
                title = "Price Drop Alert on Redmi Note 13 5G",
                message = "An item in your wishlist has an updated seasonal discount. Check it out now!",
                type = "PRODUCT"
            )
        )
        database.notificationDao().insertNotifications(notifications)

        // 11. Seed Demo Cart Items (2 items)
        if (insertedProductIds.size >= 2) {
            val prod1 = database.productDao().getProductById(insertedProductIds[0])
            val prod2 = database.productDao().getProductById(insertedProductIds[1])

            if (prod1 != null) {
                database.cartDao().insertCartItem(
                    CartItemEntity(
                        userId = customerUserId,
                        productId = prod1.id,
                        sellerId = prod1.sellerId ?: seller1Id,
                        quantity = 1,
                        unitPrice = prod1.price,
                        unitMrp = prod1.mrp
                    )
                )
            }
            if (prod2 != null) {
                database.cartDao().insertCartItem(
                    CartItemEntity(
                        userId = customerUserId,
                        productId = prod2.id,
                        sellerId = prod2.sellerId ?: seller2Id,
                        quantity = 1,
                        unitPrice = prod2.price,
                        unitMrp = prod2.mrp
                    )
                )
            }
        }

        // 12. Seed Demo Orders for Orders History & Tracking
        if (insertedProductIds.isNotEmpty()) {
            val p1 = database.productDao().getProductById(insertedProductIds[0])
            val seller1 = database.sellerDao().getSellerById(seller1Id)

            // Order 1: ORDER_PLACED (New Order ready for Seller to accept)
            val order1Id = database.orderDao().insertOrder(
                OrderEntity(
                    orderNumber = "SMP-2026-8812",
                    customerId = customerUserId,
                    customerName = "Pranaya Khuntia",
                    customerMobile = "9876543210",
                    customerEmail = "pranayakhuntia85@gmail.com",
                    sellerId = seller1?.id ?: seller1Id,
                    sellerName = seller1?.businessName ?: "Maa Tarini Electronics",
                    sellerAddress = seller1?.businessAddress ?: "Main Market, Keonjhar, Odisha",
                    deliveryAddressSnapshot = "Flat 302, Royal Residency, College Road, Near Medical Chowk, Keonjhar, Odisha - 758001",
                    deliveryAddressId = 1L,
                    distanceKm = 4.2,
                    productCount = 1,
                    subtotalAmount = p1?.price ?: 14278.0,
                    deliveryCharge = 10.0,
                    codFee = 0.0,
                    totalAmount = (p1?.price ?: 14278.0) + 10.0,
                    paymentMethod = "UPI",
                    paymentUpiApp = "Google Pay",
                    paymentTransactionId = "UPI-GOOG-992817462",
                    paymentStatus = "PAID",
                    orderStatus = "ORDER_PLACED",
                    createdAt = System.currentTimeMillis() - (15 * 60 * 1000L) // 15 mins ago
                )
            )
            if (p1 != null) {
                database.orderDao().insertOrderItems(
                    listOf(
                        OrderItemEntity(
                            orderId = order1Id,
                            orderNumber = "SMP-2026-8812",
                            productId = p1.id,
                            productName = p1.name,
                            productBrand = p1.brand,
                            productImage = p1.imageUrl,
                            categoryName = "Mobile",
                            sellerId = p1.sellerId ?: seller1Id,
                            sellerName = seller1?.businessName ?: "Maa Tarini Electronics",
                            quantity = 1,
                            unitPrice = p1.price,
                            unitMrp = p1.mrp,
                            subtotal = p1.price
                        )
                    )
                )
            }

            // Order 2: SELLER_PROCESSING
            if (insertedProductIds.size > 2) {
                val p3 = database.productDao().getProductById(insertedProductIds[2])
                val order2Id = database.orderDao().insertOrder(
                    OrderEntity(
                        orderNumber = "SMP-2026-7734",
                        customerId = customerUserId,
                        customerName = "Pranaya Khuntia",
                        customerMobile = "9876543210",
                        customerEmail = "pranayakhuntia85@gmail.com",
                        sellerId = seller1?.id ?: seller1Id,
                        sellerName = seller1?.businessName ?: "Maa Tarini Electronics",
                        sellerAddress = seller1?.businessAddress ?: "Main Market, Keonjhar, Odisha",
                        deliveryAddressSnapshot = "Flat 302, Royal Residency, College Road, Near Medical Chowk, Keonjhar, Odisha - 758001",
                        deliveryAddressId = 1L,
                        distanceKm = 6.8,
                        productCount = 1,
                        subtotalAmount = p3?.price ?: 72418.0,
                        deliveryCharge = 16.0,
                        codFee = 0.0,
                        totalAmount = (p3?.price ?: 72418.0) + 16.0,
                        paymentMethod = "DEBIT_CARD",
                        paymentTransactionId = "CARD-HDFC-88271109",
                        paymentStatus = "PAID",
                        orderStatus = "SELLER_PROCESSING",
                        createdAt = System.currentTimeMillis() - (2 * 3600 * 1000L) // 2 hours ago
                    )
                )
                if (p3 != null) {
                    database.orderDao().insertOrderItems(
                        listOf(
                            OrderItemEntity(
                                orderId = order2Id,
                                orderNumber = "SMP-2026-7734",
                                productId = p3.id,
                                productName = p3.name,
                                productBrand = p3.brand,
                                productImage = p3.imageUrl,
                                categoryName = "Mobile",
                                sellerId = p3.sellerId ?: seller1Id,
                                sellerName = seller1?.businessName ?: "Maa Tarini Electronics",
                                quantity = 1,
                                unitPrice = p3.price,
                                unitMrp = p3.mrp,
                                subtotal = p3.price
                            )
                        )
                    )
                }
            }

            // Order 3: READY_FOR_PICKUP
            if (insertedProductIds.size > 1) {
                val p2 = database.productDao().getProductById(insertedProductIds[1])
                val order3Id = database.orderDao().insertOrder(
                    OrderEntity(
                        orderNumber = "SMP-2026-6645",
                        customerId = customerUserId,
                        customerName = "Pranaya Khuntia",
                        customerMobile = "9876543210",
                        customerEmail = "pranayakhuntia85@gmail.com",
                        sellerId = seller1?.id ?: seller1Id,
                        sellerName = seller1?.businessName ?: "Maa Tarini Electronics",
                        sellerAddress = seller1?.businessAddress ?: "Main Market, Keonjhar, Odisha",
                        deliveryAddressSnapshot = "Flat 302, Royal Residency, College Road, Near Medical Chowk, Keonjhar, Odisha - 758001",
                        deliveryAddressId = 1L,
                        distanceKm = 3.0,
                        productCount = 1,
                        subtotalAmount = p2?.price ?: 76498.0,
                        deliveryCharge = 10.0,
                        codFee = 10.0,
                        totalAmount = (p2?.price ?: 76498.0) + 20.0,
                        paymentMethod = "COD",
                        paymentStatus = "PENDING_COD",
                        orderStatus = "READY_FOR_PICKUP",
                        createdAt = System.currentTimeMillis() - (5 * 3600 * 1000L) // 5 hours ago
                    )
                )
                if (p2 != null) {
                    database.orderDao().insertOrderItems(
                        listOf(
                            OrderItemEntity(
                                orderId = order3Id,
                                orderNumber = "SMP-2026-6645",
                                productId = p2.id,
                                productName = p2.name,
                                productBrand = p2.brand,
                                productImage = p2.imageUrl,
                                categoryName = "Mobile",
                                sellerId = p2.sellerId ?: seller1Id,
                                sellerName = seller1?.businessName ?: "Maa Tarini Electronics",
                                quantity = 1,
                                unitPrice = p2.price,
                                unitMrp = p2.mrp,
                                subtotal = p2.price
                            )
                        )
                    )
                }
            }

            // Order 4: DELIVERED (Order History)
            val order4Id = database.orderDao().insertOrder(
                OrderEntity(
                    orderNumber = "SMP-2026-5521",
                    customerId = customerUserId,
                    customerName = "Pranaya Khuntia",
                    customerMobile = "9876543210",
                    customerEmail = "pranayakhuntia85@gmail.com",
                    sellerId = seller1?.id ?: seller1Id,
                    sellerName = seller1?.businessName ?: "Maa Tarini Electronics",
                    sellerAddress = seller1?.businessAddress ?: "Main Market, Keonjhar, Odisha",
                    deliveryAddressSnapshot = "Flat 302, Royal Residency, College Road, Near Medical Chowk, Keonjhar, Odisha - 758001",
                    deliveryAddressId = 1L,
                    distanceKm = 5.0,
                    productCount = 1,
                    subtotalAmount = 2499.0,
                    deliveryCharge = 10.0,
                    codFee = 0.0,
                    totalAmount = 2509.0,
                    paymentMethod = "UPI",
                    paymentUpiApp = "PhonePe",
                    paymentTransactionId = "UPI-PHONEPE-77218392",
                    paymentStatus = "PAID",
                    orderStatus = "DELIVERED",
                    createdAt = System.currentTimeMillis() - (4 * 24 * 3600 * 1000L) // 4 days ago
                )
            )
            if (p1 != null) {
                database.orderDao().insertOrderItems(
                    listOf(
                        OrderItemEntity(
                            orderId = order4Id,
                            orderNumber = "SMP-2026-5521",
                            productId = p1.id,
                            productName = "Boat Rockerz 450 Bluetooth Headphone",
                            productBrand = "Boat",
                            productImage = p1.imageUrl,
                            categoryName = "Electronics",
                            sellerId = p1.sellerId ?: seller1Id,
                            sellerName = seller1?.businessName ?: "Maa Tarini Electronics",
                            quantity = 1,
                            unitPrice = 2499.0,
                            unitMrp = 3990.0,
                            subtotal = 2499.0
                        )
                    )
                )
            }

            // 11. Seed Delivery Partners (Fleet for Keonjhar region)
            val deliveryPartners = listOf(
                DeliveryPartnerEntity(
                    name = "Rakesh Kumar Nayak",
                    mobile = "9861234501",
                    email = "rakesh.delivery@sampurna.in",
                    passwordHash = "delivery123",
                    vehicleType = "Bike",
                    vehicleNumber = "OD-09-AF-2481",
                    licenseNumber = "OD0920210045231",
                    assignedHub = "Keonjhar Central Hub",
                    emergencyContact = "9437123451",
                    isActive = true,
                    isOnDuty = true,
                    totalDeliveries = 148,
                    rating = 4.9f
                ),
                DeliveryPartnerEntity(
                    name = "Bikash Ranjan Jena",
                    mobile = "9861234502",
                    email = "bikash.delivery@sampurna.in",
                    passwordHash = "delivery123",
                    vehicleType = "Scooter",
                    vehicleNumber = "OD-09-BG-5520",
                    licenseNumber = "OD0920220078124",
                    assignedHub = "Anandapur Hub",
                    emergencyContact = "9437123452",
                    isActive = true,
                    isOnDuty = true,
                    totalDeliveries = 94,
                    rating = 4.8f
                ),
                DeliveryPartnerEntity(
                    name = "Manoj Kumar Sahoo",
                    mobile = "9861234503",
                    email = "manoj.delivery@sampurna.in",
                    passwordHash = "delivery123",
                    vehicleType = "Electric Bike",
                    vehicleNumber = "OD-09-EV-1089",
                    licenseNumber = "OD0920200034119",
                    assignedHub = "Barbil & Joda Hub",
                    emergencyContact = "9437123453",
                    isActive = true,
                    isOnDuty = true,
                    totalDeliveries = 76,
                    rating = 4.7f
                ),
                DeliveryPartnerEntity(
                    name = "Deepak Kumar Mahanta",
                    mobile = "9861234504",
                    email = "deepak.delivery@sampurna.in",
                    passwordHash = "delivery123",
                    vehicleType = "Bike",
                    vehicleNumber = "OD-09-CD-3312",
                    licenseNumber = "OD0920230099432",
                    assignedHub = "Ghatagaon Tarini Hub",
                    emergencyContact = "9437123454",
                    isActive = true,
                    isOnDuty = false,
                    totalDeliveries = 52,
                    rating = 4.6f
                )
            )
            database.deliveryBoyDao().insertDeliveryBoys(deliveryPartners)
        }
    }
}
