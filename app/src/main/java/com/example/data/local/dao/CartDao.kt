package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity): Long

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Query("SELECT * FROM cart_items WHERE user_id = :userId ORDER BY added_at DESC")
    fun getCartItemsFlow(userId: Long): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE user_id = :userId ORDER BY added_at DESC")
    suspend fun getCartItemsSync(userId: Long): List<CartItemEntity>

    @Query("SELECT * FROM cart_items WHERE user_id = :userId AND product_id = :productId LIMIT 1")
    suspend fun getCartItemByProduct(userId: Long, productId: Long): CartItemEntity?

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: Long)

    @Query("DELETE FROM cart_items WHERE user_id = :userId")
    suspend fun clearCart(userId: Long)

    @Query("SELECT COUNT(*) FROM cart_items WHERE user_id = :userId")
    fun getCartCountFlow(userId: Long): Flow<Int>
}
