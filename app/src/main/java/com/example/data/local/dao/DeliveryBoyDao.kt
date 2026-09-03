package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.DeliveryPartnerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryBoyDao {

    @Query("SELECT * FROM delivery_partners ORDER BY created_at DESC")
    fun getAllDeliveryBoysFlow(): Flow<List<DeliveryPartnerEntity>>

    @Query("SELECT * FROM delivery_partners WHERE is_active = 1 ORDER BY name ASC")
    fun getActiveDeliveryBoysFlow(): Flow<List<DeliveryPartnerEntity>>

    @Query("SELECT * FROM delivery_partners WHERE is_active = 0 ORDER BY created_at DESC")
    fun getPendingDeliveryBoysFlow(): Flow<List<DeliveryPartnerEntity>>

    @Query("SELECT * FROM delivery_partners WHERE id = :id LIMIT 1")
    suspend fun getDeliveryBoyById(id: Long): DeliveryPartnerEntity?

    @Query("SELECT * FROM delivery_partners WHERE mobile = :mobile LIMIT 1")
    suspend fun getDeliveryBoyByMobile(mobile: String): DeliveryPartnerEntity?

    @Query("SELECT * FROM delivery_partners WHERE email = :email LIMIT 1")
    suspend fun getDeliveryBoyByEmail(email: String): DeliveryPartnerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeliveryBoy(deliveryBoy: DeliveryPartnerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeliveryBoys(deliveryBoys: List<DeliveryPartnerEntity>): List<Long>

    @Update
    suspend fun updateDeliveryBoy(deliveryBoy: DeliveryPartnerEntity)

    @Delete
    suspend fun deleteDeliveryBoy(deliveryBoy: DeliveryPartnerEntity)

    @Query("UPDATE delivery_partners SET is_active = :isActive, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateActiveStatus(id: Long, isActive: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE delivery_partners SET is_on_duty = :isOnDuty, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateDutyStatus(id: Long, isOnDuty: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM delivery_partners")
    suspend fun getTotalCount(): Int

    @Query("SELECT COUNT(*) FROM delivery_partners WHERE is_active = 1 AND is_on_duty = 1")
    suspend fun getActiveOnDutyCount(): Int
}
