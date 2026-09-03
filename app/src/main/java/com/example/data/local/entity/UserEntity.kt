package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["mobile"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val mobile: String,
    val email: String,
    @ColumnInfo(name = "password_hash")
    val passwordHash: String,
    val role: String = "customer", // customer, seller, admin
    @ColumnInfo(name = "profile_photo_uri")
    val profilePhotoUri: String? = null,
    @ColumnInfo(name = "email_verified")
    val emailVerified: Boolean = false,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
