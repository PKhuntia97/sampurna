package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CustomerProductCancellationEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: Long): OrderEntity?

    @Query("SELECT * FROM orders WHERE order_number = :orderNumber LIMIT 1")
    suspend fun getOrderByNumber(orderNumber: String): OrderEntity?

    @Query("SELECT * FROM orders WHERE customer_id = :customerId ORDER BY created_at DESC")
    fun getOrdersForCustomer(customerId: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE seller_id = :sellerId ORDER BY created_at DESC")
    fun getOrdersForSeller(sellerId: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders ORDER BY created_at DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    fun getOrderItemsFlow(orderId: Long): Flow<List<OrderItemEntity>>

    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    suspend fun getOrderItemsSync(orderId: Long): List<OrderItemEntity>

    @Query("UPDATE orders SET order_status = :status, updated_at = :updatedAt WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: String, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE orders SET order_status = 'CANCELLED_BY_CUSTOMER', cancel_reason = :reason, cancelled_by = 'CUSTOMER', cancelled_at = :timestamp, refund_status = :refundStatus, updated_at = :timestamp WHERE id = :orderId")
    suspend fun cancelOrderByCustomer(orderId: Long, reason: String, refundStatus: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE orders SET order_status = 'CANCELLED_BY_SELLER', cancel_reason = :reason, cancelled_by = 'SELLER', cancelled_at = :timestamp, refund_status = :refundStatus, updated_at = :timestamp WHERE id = :orderId")
    suspend fun cancelOrderBySeller(orderId: Long, reason: String, refundStatus: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE orders SET refund_status = :status, updated_at = :timestamp WHERE id = :orderId")
    suspend fun updateRefundStatus(orderId: Long, status: String, timestamp: Long = System.currentTimeMillis())

    // Cancellation Tracking (1% same product repeated cancellation penalty)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCancellation(cancellation: CustomerProductCancellationEntity): Long

    @Query("SELECT * FROM customer_product_cancellations WHERE customer_id = :customerId AND product_id = :productId")
    suspend fun getCancellationsForCustomerProduct(customerId: Long, productId: Long): List<CustomerProductCancellationEntity>

    @Query("SELECT COUNT(*) FROM customer_product_cancellations WHERE customer_id = :customerId AND product_id = :productId AND is_admin_overridden = 0")
    suspend fun getActiveCancellationCount(customerId: Long, productId: Long): Int

    @Query("SELECT * FROM customer_product_cancellations ORDER BY created_at DESC")
    fun getAllCancellationsFlow(): Flow<List<CustomerProductCancellationEntity>>

    @Query("DELETE FROM customer_product_cancellations WHERE customer_id = :customerId AND product_id = :productId")
    suspend fun resetCancellationsForCustomerProduct(customerId: Long, productId: Long)

    @Query("UPDATE customer_product_cancellations SET is_admin_overridden = :override WHERE id = :cancellationId")
    suspend fun updateCancellationOverride(cancellationId: Long, override: Boolean)
}
