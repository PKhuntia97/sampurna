package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SystemSettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SystemSettingDao {

    @Query("SELECT value FROM system_settings WHERE `key` = :key LIMIT 1")
    fun getSettingValueFlow(key: String): Flow<String?>

    @Query("SELECT value FROM system_settings WHERE `key` = :key LIMIT 1")
    suspend fun getSettingValue(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSetting(setting: SystemSettingEntity)

    @Query("SELECT * FROM system_settings")
    fun getAllSettingsFlow(): Flow<List<SystemSettingEntity>>
}
