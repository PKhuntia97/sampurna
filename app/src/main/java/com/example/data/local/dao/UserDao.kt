package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserByIdFlow(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): UserEntity?

    @Query("SELECT * FROM users WHERE mobile = :mobile LIMIT 1")
    suspend fun getUserByMobile(mobile: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE (mobile = :identifier OR email = :identifier) LIMIT 1")
    suspend fun getUserByMobileOrEmail(identifier: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET profile_photo_uri = :photoUri, updated_at = :updatedAt WHERE id = :userId")
    suspend fun updateProfilePhoto(userId: Long, photoUri: String?, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE users SET mobile = :newMobile, updated_at = :updatedAt WHERE id = :userId")
    suspend fun updateMobileNumber(userId: Long, newMobile: String, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE users SET password_hash = :newPasswordHash, updated_at = :updatedAt WHERE id = :userId")
    suspend fun updatePassword(userId: Long, newPasswordHash: String, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE users SET email_verified = :isVerified, updated_at = :updatedAt WHERE id = :userId")
    suspend fun updateEmailVerification(userId: Long, isVerified: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM users WHERE role = 'customer'")
    suspend fun getCustomerCount(): Int
}
