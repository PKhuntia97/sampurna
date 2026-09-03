package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(wishlist: WishlistEntity): Long

    @Query("DELETE FROM wishlists WHERE user_id = :userId AND product_id = :productId")
    suspend fun removeFromWishlist(userId: Long, productId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlists WHERE user_id = :userId AND product_id = :productId)")
    fun isProductInWishlistFlow(userId: Long, productId: Long): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlists WHERE user_id = :userId AND product_id = :productId)")
    suspend fun isProductInWishlist(userId: Long, productId: Long): Boolean

    @Query("SELECT product_id FROM wishlists WHERE user_id = :userId")
    fun getWishlistProductIdsFlow(userId: Long): Flow<List<Long>>

    @Transaction
    @Query("""
        SELECT p.* FROM products p
        INNER JOIN wishlists w ON p.id = w.product_id
        WHERE w.user_id = :userId
        ORDER BY w.created_at DESC
    """)
    fun getWishlistProductsFlow(userId: Long): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM wishlists WHERE user_id = :userId")
    fun getWishlistCountFlow(userId: Long): Flow<Int>
}
