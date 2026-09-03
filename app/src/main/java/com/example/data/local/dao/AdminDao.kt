package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.AdminUserEntity

@Dao
interface AdminDao {
    @Query("SELECT * FROM admin_users WHERE email = :email AND is_active = 1 LIMIT 1")
    suspend fun getAdminByEmail(email: String): AdminUserEntity?

    @Query("SELECT COUNT(*) FROM admin_users")
    suspend fun getAdminCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminUserEntity): Long
}
