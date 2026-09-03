package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "delivery_partners",
    indices = [
        Index(value = ["mobile"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class DeliveryPartnerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val mobile: String,
    val email: String,
    @ColumnInfo(name = "password_hash")
    val passwordHash: String = "delivery123",
    @ColumnInfo(name = "vehicle_type")
    val vehicleType: String = "Bike", // Bike, Scooter, Electric Bike, Van, Cycle
    @ColumnInfo(name = "vehicle_number")
    val vehicleNumber: String = "",
    @ColumnInfo(name = "license_number")
    val licenseNumber: String = "",
    @ColumnInfo(name = "assigned_hub")
    val assignedHub: String = "Keonjhar Central Hub",
    @ColumnInfo(name = "emergency_contact")
    val emergencyContact: String? = null,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true, // Admin approval / active state
    @ColumnInfo(name = "is_on_duty")
    val isOnDuty: Boolean = true, // Online / Offline for delivery
    @ColumnInfo(name = "total_deliveries")
    val totalDeliveries: Int = 0,
    val rating: Float = 4.8f,
    @ColumnInfo(name = "profile_photo_uri")
    val profilePhotoUri: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
