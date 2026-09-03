package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "seller_products",
    foreignKeys = [
        ForeignKey(
            entity = SellerEntity::class,
            parentColumns = ["id"],
            childColumns = ["seller_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["seller_id"]),
        Index(value = ["product_id"]),
        Index(value = ["seller_id", "product_id"], unique = true)
    ]
)
data class SellerProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "seller_id")
    val sellerId: Long,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "seller_price")
    val sellerPrice: Double,
    val mrp: Double,
    @ColumnInfo(name = "customer_price")
    val customerPrice: Double,
    val stock: Int = 10,
    val sku: String = "",
    @ColumnInfo(name = "custom_offer")
    val customOffer: String = "",
    val status: String = "APPROVED", // DRAFT, SUBMITTED, APPROVED, REJECTED, LIVE
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
