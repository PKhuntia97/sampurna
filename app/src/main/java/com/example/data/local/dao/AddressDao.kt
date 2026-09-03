package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.entity.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Query("SELECT * FROM addresses WHERE user_id = :userId ORDER BY is_default DESC, created_at DESC")
    fun getAddressesByUserIdFlow(userId: Long): Flow<List<AddressEntity>>

    @Query("SELECT * FROM addresses WHERE user_id = :userId ORDER BY is_default DESC, created_at DESC")
    suspend fun getAddressesByUserId(userId: Long): List<AddressEntity>

    @Query("SELECT * FROM addresses WHERE user_id = :userId AND is_default = 1 LIMIT 1")
    suspend fun getDefaultAddress(userId: Long): AddressEntity?

    @Query("SELECT * FROM addresses WHERE user_id = :userId AND is_default = 1 LIMIT 1")
    fun getDefaultAddressFlow(userId: Long): Flow<AddressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity): Long

    @Update
    suspend fun updateAddress(address: AddressEntity)

    @Delete
    suspend fun deleteAddress(address: AddressEntity)

    @Query("DELETE FROM addresses WHERE id = :id AND user_id = :userId")
    suspend fun deleteAddressById(id: Long, userId: Long)

    @Query("UPDATE addresses SET is_default = 0 WHERE user_id = :userId")
    suspend fun clearDefaultAddress(userId: Long)

    @Query("UPDATE addresses SET is_default = 1 WHERE id = :addressId AND user_id = :userId")
    suspend fun markAddressAsDefault(addressId: Long, userId: Long)

    @Transaction
    suspend fun setDefaultAddressTransaction(addressId: Long, userId: Long) {
        clearDefaultAddress(userId)
        markAddressAsDefault(addressId, userId)
    }

    @Transaction
    suspend fun insertNewAddressWithDefault(address: AddressEntity, setAsDefault: Boolean): Long {
        if (setAsDefault) {
            clearDefaultAddress(address.userId)
        }
        return insertAddress(address.copy(isDefault = setAsDefault))
    }
}
