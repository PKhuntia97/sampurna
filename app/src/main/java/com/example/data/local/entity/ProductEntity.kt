package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SubCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["sub_category_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = SellerEntity::class,
            parentColumns = ["id"],
            childColumns = ["seller_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["category_id"]),
        Index(value = ["sub_category_id"]),
        Index(value = ["seller_id"]),
        Index(value = ["status"])
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    @ColumnInfo(name = "sub_category_id")
    val subCategoryId: Long? = null,
    @ColumnInfo(name = "seller_id")
    val sellerId: Long? = null,
    val brand: String = "Sampurna Official",
    val name: String,
    val description: String = "Experience premium quality with genuine assurance, verified seller standards, and instant doorstep delivery.",
    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,
    @ColumnInfo(name = "icon_type")
    val iconType: String = "general",
    val mrp: Double,
    @ColumnInfo(name = "seller_price")
    val sellerPrice: Double = 0.0,
    val price: Double = 0.0, // Customer Final Price (Seller Price + Sampurna Commission %)
    val discount: Int = 0,
    val stock: Int = 50,
    val sku: String = "SAM-001",
    val warranty: String = "1 Year Brand Warranty",
    val rating: Float = 4.5f,
    @ColumnInfo(name = "rating_count")
    val ratingCount: Int = 50,
    val tag: String = "trending", // trending, best_selling, todays_offers, new_arrivals, recommended
    val status: String = "APPROVED", // DRAFT, SUBMITTED, APPROVED, REJECTED, LIVE
    @ColumnInfo(name = "rejection_reason")
    val rejectionReason: String? = null,
    val specifications: String = "Standard Official Grade; Verified by Sampurna Quality Assurance",
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
