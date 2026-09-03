package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "customer_product_cancellations",
    indices = [
        Index(value = ["customer_id"]),
        Index(value = ["product_id"]),
        Index(value = ["customer_id", "product_id"])
    ]
)
data class CustomerProductCancellationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "customer_id")
    val customerId: Long,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "order_id")
    val orderId: Long,
    @ColumnInfo(name = "cancellation_reason")
    val cancellationReason: String,
    @ColumnInfo(name = "adjustment_rate_percent")
    val adjustmentRatePercent: Double = 1.0,
    @ColumnInfo(name = "is_admin_overridden")
    val isAdminOverridden: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
