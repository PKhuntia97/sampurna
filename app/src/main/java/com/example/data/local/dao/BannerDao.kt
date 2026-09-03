package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CategoryBannerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BannerDao {
    @Query("SELECT * FROM category_banners ORDER BY display_order ASC, id DESC")
    fun getAllBannersFlow(): Flow<List<CategoryBannerEntity>>

    @Query("SELECT * FROM category_banners WHERE is_active = 1 ORDER BY display_order ASC, id DESC")
    fun getActiveBannersFlow(): Flow<List<CategoryBannerEntity>>

    @Query("SELECT * FROM category_banners WHERE category_id IS NULL AND is_active = 1 ORDER BY display_order ASC")
    fun getActiveGeneralBannersFlow(): Flow<List<CategoryBannerEntity>>

    @Query("SELECT * FROM category_banners WHERE category_id = :categoryId AND is_active = 1 ORDER BY display_order ASC LIMIT 1")
    fun getActiveBannerForCategoryFlow(categoryId: Long): Flow<CategoryBannerEntity?>

    @Query("SELECT * FROM category_banners WHERE category_id = :categoryId ORDER BY display_order ASC")
    fun getBannersByCategoryIdFlow(categoryId: Long): Flow<List<CategoryBannerEntity>>

    @Query("SELECT COUNT(*) FROM category_banners")
    fun getTotalBannersCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM category_banners WHERE is_active = 1")
    fun getActiveBannersCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banner: CategoryBannerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanners(banners: List<CategoryBannerEntity>): List<Long>

    @Update
    suspend fun updateBanner(banner: CategoryBannerEntity)

    @Delete
    suspend fun deleteBanner(banner: CategoryBannerEntity)

    @Query("DELETE FROM category_banners WHERE id = :id")
    suspend fun deleteBannerById(id: Long)

    @Query("UPDATE category_banners SET is_active = :isActive WHERE id = :id")
    suspend fun toggleBannerActive(id: Long, isActive: Boolean)
}
