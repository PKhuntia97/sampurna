package com.example.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_variants",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["product_id"])
    ]
)
data class ProductVariantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "variant_name")
    val variantName: String, // Size, Color, Storage, Pack
    @ColumnInfo(name = "variant_value")
    val variantValue: String, // e.g. "XL", "Midnight Black", "256 GB"
    @ColumnInfo(name = "price_adjustment")
    val priceAdjustment: Double = 0.0,
    val stock: Int = 20
)
