package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "inventory",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SellerEntity::class,
            parentColumns = ["id"],
            childColumns = ["seller_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["product_id"]),
        Index(value = ["seller_id"]),
        Index(value = ["product_id", "seller_id"], unique = true)
    ]
)
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "seller_id")
    val sellerId: Long,
    @ColumnInfo(name = "stock_quantity")
    val stockQuantity: Int,
    @ColumnInfo(name = "low_stock_threshold")
    val lowStockThreshold: Int = 5,
    @ColumnInfo(name = "last_restocked_at")
    val lastRestockedAt: Long = System.currentTimeMillis()
)
