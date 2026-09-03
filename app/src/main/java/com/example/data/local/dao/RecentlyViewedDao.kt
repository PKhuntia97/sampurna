package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.RecentlyViewedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyViewedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordRecentlyViewed(entity: RecentlyViewedEntity)

    @Transaction
    @Query("""
        SELECT p.* FROM products p
        INNER JOIN recently_viewed rv ON p.id = rv.product_id
        WHERE rv.user_id = :userId
        ORDER BY rv.viewed_at DESC
        LIMIT :limit
    """)
    fun getRecentlyViewedProductsFlow(userId: Long, limit: Int = 20): Flow<List<ProductEntity>>

    @Query("DELETE FROM recently_viewed WHERE user_id = :userId")
    suspend fun clearRecentlyViewed(userId: Long)

    @Query("DELETE FROM recently_viewed WHERE user_id = :userId AND product_id = :productId")
    suspend fun removeRecentlyViewedItem(userId: Long, productId: Long)
}
