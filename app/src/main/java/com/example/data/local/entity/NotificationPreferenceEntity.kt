package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notification_preferences",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id"], unique = true)
    ]
)
data class NotificationPreferenceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "app_enabled")
    val appEnabled: Boolean = true,
    @ColumnInfo(name = "email_enabled")
    val emailEnabled: Boolean = true,
    @ColumnInfo(name = "whatsapp_enabled")
    val whatsappEnabled: Boolean = false,
    @ColumnInfo(name = "sms_enabled")
    val smsEnabled: Boolean = false
)
