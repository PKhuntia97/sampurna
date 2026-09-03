package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    indices = [
        Index(value = ["order_number"], unique = true),
        Index(value = ["customer_id"]),
        Index(value = ["seller_id"]),
        Index(value = ["order_status"])
    ]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "order_number")
    val orderNumber: String,
    @ColumnInfo(name = "customer_id")
    val customerId: Long,
    @ColumnInfo(name = "customer_name")
    val customerName: String,
    @ColumnInfo(name = "customer_mobile")
    val customerMobile: String,
    @ColumnInfo(name = "customer_email")
    val customerEmail: String,
    @ColumnInfo(name = "seller_id")
    val sellerId: Long,
    @ColumnInfo(name = "seller_name")
    val sellerName: String,
    @ColumnInfo(name = "seller_address")
    val sellerAddress: String,
    @ColumnInfo(name = "delivery_address_snapshot")
    val deliveryAddressSnapshot: String,
    @ColumnInfo(name = "delivery_address_id")
    val deliveryAddressId: Long? = null,
    @ColumnInfo(name = "distance_km")
    val distanceKm: Double = 3.5,
    @ColumnInfo(name = "product_count")
    val productCount: Int = 1,
    @ColumnInfo(name = "subtotal_amount")
    val subtotalAmount: Double,
    @ColumnInfo(name = "delivery_charge")
    val deliveryCharge: Double,
    @ColumnInfo(name = "cod_fee")
    val codFee: Double = 0.0,
    @ColumnInfo(name = "cancellation_adjustment_amount")
    val cancellationAdjustmentAmount: Double = 0.0,
    @ColumnInfo(name = "total_amount")
    val totalAmount: Double,
    @ColumnInfo(name = "payment_method")
    val paymentMethod: String, // UPI, DEBIT_CARD, CREDIT_CARD, COD
    @ColumnInfo(name = "payment_upi_app")
    val paymentUpiApp: String? = null,
    @ColumnInfo(name = "payment_transaction_id")
    val paymentTransactionId: String? = null,
    @ColumnInfo(name = "payment_status")
    val paymentStatus: String, // PAID, PENDING_COD, FAILED, REFUNDED
    @ColumnInfo(name = "order_status")
    val orderStatus: String, // ORDER_PLACED, SELLER_PROCESSING, READY_FOR_PICKUP, PICKED_UP, OUT_FOR_DELIVERY, DELIVERED, CANCELLED_BY_CUSTOMER, CANCELLED_BY_SELLER
    @ColumnInfo(name = "cancel_reason")
    val cancelReason: String? = null,
    @ColumnInfo(name = "cancelled_by")
    val cancelledBy: String? = null, // CUSTOMER, SELLER, ADMIN
    @ColumnInfo(name = "cancelled_at")
    val cancelledAt: Long? = null,
    @ColumnInfo(name = "refund_status")
    val refundStatus: String? = "NONE", // NONE, INITIATED, COMPLETED
    @ColumnInfo(name = "refund_amount")
    val refundAmount: Double = 0.0,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
