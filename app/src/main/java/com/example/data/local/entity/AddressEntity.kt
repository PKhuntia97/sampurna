package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "addresses",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id"])
    ]
)
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    val name: String,
    val mobile: String,
    @ColumnInfo(name = "house_flat")
    val houseFlat: String,
    @ColumnInfo(name = "street_area")
    val streetArea: String,
    val landmark: String = "",
    val city: String,
    val district: String,
    val state: String,
    @ColumnInfo(name = "pin_code")
    val pinCode: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @ColumnInfo(name = "address_type")
    val addressType: String = "HOME", // HOME, WORK, OTHER
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
