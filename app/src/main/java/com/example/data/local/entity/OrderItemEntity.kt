package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["order_id"]),
        Index(value = ["product_id"])
    ]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "order_id")
    val orderId: Long,
    @ColumnInfo(name = "order_number")
    val orderNumber: String,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "product_brand")
    val productBrand: String,
    @ColumnInfo(name = "product_image")
    val productImage: String? = null,
    @ColumnInfo(name = "category_name")
    val categoryName: String? = null,
    @ColumnInfo(name = "seller_id")
    val sellerId: Long,
    @ColumnInfo(name = "seller_name")
    val sellerName: String,
    val quantity: Int = 1,
    @ColumnInfo(name = "unit_price")
    val unitPrice: Double,
    @ColumnInfo(name = "unit_mrp")
    val unitMrp: Double,
    val subtotal: Double,
    @ColumnInfo(name = "had_cancellation_penalty")
    val hadCancellationPenalty: Boolean = false,
    @ColumnInfo(name = "penalty_amount")
    val penaltyAmount: Double = 0.0
)
