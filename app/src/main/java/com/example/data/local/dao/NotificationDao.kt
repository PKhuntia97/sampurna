package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.NotificationPreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications WHERE user_id = :userId ORDER BY created_at DESC")
    fun getNotificationsForUserFlow(userId: Long): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE user_id = :userId AND type = :type ORDER BY created_at DESC")
    fun getNotificationsByTypeFlow(userId: Long, type: String): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE user_id = :userId AND is_read = 0")
    fun getUnreadNotificationCountFlow(userId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>): List<Long>

    @Query("UPDATE notifications SET is_read = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: Long)

    @Query("UPDATE notifications SET is_read = 1 WHERE user_id = :userId")
    suspend fun markAllAsRead(userId: Long)

    @Query("DELETE FROM notifications WHERE id = :notificationId")
    suspend fun deleteNotification(notificationId: Long)

    @Query("DELETE FROM notifications WHERE user_id = :userId")
    suspend fun clearAllNotifications(userId: Long)

    // Notification Preferences
    @Query("SELECT * FROM notification_preferences WHERE user_id = :userId LIMIT 1")
    fun getPreferencesForUserFlow(userId: Long): Flow<NotificationPreferenceEntity?>

    @Query("SELECT * FROM notification_preferences WHERE user_id = :userId LIMIT 1")
    suspend fun getPreferencesForUser(userId: Long): NotificationPreferenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePreferences(prefs: NotificationPreferenceEntity): Long
}
