package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SellerCategoryAssignmentEntity
import com.example.data.local.entity.SellerEntity
import com.example.data.local.entity.SellerProductEntity
import com.example.data.local.entity.SubCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SellerDao {

    @Query("SELECT * FROM sellers ORDER BY created_at DESC")
    fun getAllSellersFlow(): Flow<List<SellerEntity>>

    @Query("SELECT * FROM sellers WHERE status = 'PENDING' OR verification_status = 'PENDING_APPROVAL' ORDER BY created_at DESC")
    fun getPendingSellersFlow(): Flow<List<SellerEntity>>

    @Query("UPDATE sellers SET status = :status, verification_status = :verificationStatus WHERE id = :sellerId")
    suspend fun updateSellerApprovalStatus(sellerId: Long, status: String, verificationStatus: String)

    @Query("SELECT * FROM sellers WHERE id = :sellerId LIMIT 1")
    fun getSellerByIdFlow(sellerId: Long): Flow<SellerEntity?>

    @Query("SELECT * FROM sellers WHERE id = :sellerId LIMIT 1")
    suspend fun getSellerById(sellerId: Long): SellerEntity?

    @Query("SELECT * FROM sellers WHERE email = :email LIMIT 1")
    suspend fun getSellerByEmail(email: String): SellerEntity?

    @Query("SELECT * FROM sellers WHERE mobile = :mobile LIMIT 1")
    suspend fun getSellerByMobile(mobile: String): SellerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeller(seller: SellerEntity): Long

    @Update
    suspend fun updateSeller(seller: SellerEntity)

    @Delete
    suspend fun deleteSeller(seller: SellerEntity)

    @Query("SELECT COUNT(*) FROM sellers")
    suspend fun getSellerCount(): Int

    // Category Assignments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignments(assignments: List<SellerCategoryAssignmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: SellerCategoryAssignmentEntity): Long

    @Query("DELETE FROM seller_category_assignments WHERE seller_id = :sellerId")
    suspend fun clearAssignmentsForSeller(sellerId: Long)

    @Query("SELECT * FROM seller_category_assignments WHERE seller_id = :sellerId")
    fun getAssignmentsForSellerFlow(sellerId: Long): Flow<List<SellerCategoryAssignmentEntity>>

    @Query("SELECT * FROM seller_category_assignments WHERE seller_id = :sellerId")
    suspend fun getAssignmentsForSeller(sellerId: Long): List<SellerCategoryAssignmentEntity>

    @Transaction
    @Query("""
        SELECT DISTINCT c.* FROM categories c
        INNER JOIN seller_category_assignments sca ON c.id = sca.category_id
        WHERE sca.seller_id = :sellerId AND c.is_active = 1
        ORDER BY c.display_order ASC
    """)
    fun getAssignedCategoriesForSellerFlow(sellerId: Long): Flow<List<CategoryEntity>>

    @Transaction
    @Query("""
        SELECT DISTINCT sc.* FROM sub_categories sc
        INNER JOIN seller_category_assignments sca ON sc.id = sca.sub_category_id
        WHERE sca.seller_id = :sellerId AND sc.is_active = 1
        ORDER BY sc.display_order ASC
    """)
    fun getAssignedSubCategoriesForSellerFlow(sellerId: Long): Flow<List<SubCategoryEntity>>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM seller_category_assignments
            WHERE seller_id = :sellerId AND category_id = :categoryId
        )
    """)
    suspend fun isCategoryAssignedToSeller(sellerId: Long, categoryId: Long): Boolean

    // Seller Products
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSellerProduct(sellerProduct: SellerProductEntity): Long

    @Update
    suspend fun updateSellerProduct(sellerProduct: SellerProductEntity)

    @Query("SELECT * FROM seller_products WHERE seller_id = :sellerId")
    fun getSellerProductsFlow(sellerId: Long): Flow<List<SellerProductEntity>>

    @Transaction
    @Query("""
        SELECT p.* FROM products p
        INNER JOIN seller_products sp ON p.id = sp.product_id
        WHERE sp.seller_id = :sellerId
    """)
    fun getProductsSoldBySellerFlow(sellerId: Long): Flow<List<ProductEntity>>
}
