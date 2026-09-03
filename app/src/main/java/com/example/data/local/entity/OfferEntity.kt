package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offers")
data class OfferEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val title: String,
    val description: String,
    @ColumnInfo(name = "discount_percent")
    val discountPercent: Int,
    @ColumnInfo(name = "max_discount")
    val maxDiscount: Double = 500.0,
    @ColumnInfo(name = "min_order_value")
    val minOrderValue: Double = 499.0,
    @ColumnInfo(name = "valid_until")
    val validUntil: Long = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L),
    @ColumnInfo(name = "offer_type")
    val offerType: String = "AVAILABLE", // AVAILABLE, COUPON, PERSONALIZED, EXPIRING
    @ColumnInfo(name = "is_used")
    val isUsed: Boolean = false,
    @ColumnInfo(name = "is_expired")
    val isExpired: Boolean = false,
    @ColumnInfo(name = "applicable_category")
    val applicableCategory: String? = null,
    @ColumnInfo(name = "badge_color")
    val badgeColor: String = "orange",
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
