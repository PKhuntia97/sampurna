package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "seller_category_assignments",
    foreignKeys = [
        ForeignKey(
            entity = SellerEntity::class,
            parentColumns = ["id"],
            childColumns = ["seller_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SubCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["sub_category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["seller_id"]),
        Index(value = ["category_id"]),
        Index(value = ["sub_category_id"]),
        Index(value = ["seller_id", "category_id", "sub_category_id"], unique = true)
    ]
)
data class SellerCategoryAssignmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "seller_id")
    val sellerId: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    @ColumnInfo(name = "sub_category_id")
    val subCategoryId: Long? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
