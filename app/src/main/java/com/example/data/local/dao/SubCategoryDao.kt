package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.SubCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubCategoryDao {
    @Query("SELECT * FROM sub_categories ORDER BY display_order ASC, name ASC")
    fun getAllSubCategoriesFlow(): Flow<List<SubCategoryEntity>>

    @Query("SELECT * FROM sub_categories WHERE is_active = 1 ORDER BY display_order ASC, name ASC")
    fun getAllActiveSubCategoriesFlow(): Flow<List<SubCategoryEntity>>

    @Query("SELECT * FROM sub_categories WHERE category_id = :categoryId AND is_active = 1 ORDER BY display_order ASC, name ASC")
    fun getActiveSubCategoriesByCategoryIdFlow(categoryId: Long): Flow<List<SubCategoryEntity>>

    @Query("SELECT * FROM sub_categories WHERE category_id = :categoryId ORDER BY display_order ASC, name ASC")
    fun getSubCategoriesByCategoryIdFlow(categoryId: Long): Flow<List<SubCategoryEntity>>

    @Query("SELECT * FROM sub_categories WHERE id = :id LIMIT 1")
    suspend fun getSubCategoryById(id: Long): SubCategoryEntity?

    @Query("SELECT COUNT(*) FROM sub_categories")
    fun getSubCategoriesCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategory(subCategory: SubCategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategories(subCategories: List<SubCategoryEntity>): List<Long>

    @Update
    suspend fun updateSubCategory(subCategory: SubCategoryEntity)

    @Delete
    suspend fun deleteSubCategory(subCategory: SubCategoryEntity)

    @Query("DELETE FROM sub_categories WHERE id = :id")
    suspend fun deleteSubCategoryById(id: Long)

    @Query("UPDATE sub_categories SET is_active = :isActive WHERE id = :id")
    suspend fun toggleSubCategoryActive(id: Long, isActive: Boolean)
}
