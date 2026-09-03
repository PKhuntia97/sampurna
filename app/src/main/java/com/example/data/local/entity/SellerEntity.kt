package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sellers",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["email"], unique = true),
        Index(value = ["mobile"], unique = true)
    ]
)
data class SellerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "user_id")
    val userId: Long? = null,
    @ColumnInfo(name = "seller_name")
    val sellerName: String,
    @ColumnInfo(name = "business_name")
    val businessName: String,
    val mobile: String,
    val email: String,
    @ColumnInfo(name = "business_address")
    val businessAddress: String,
    @ColumnInfo(name = "store_info")
    val storeInfo: String,
    @ColumnInfo(name = "gst_number")
    val gstNumber: String = "21ABCDE1234F1Z5",
    @ColumnInfo(name = "verification_status")
    val verificationStatus: String = "VERIFIED",
    @ColumnInfo(name = "logo_url")
    val logoUrl: String? = null,
    val status: String = "ACTIVE", // ACTIVE, PENDING, SUSPENDED
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
