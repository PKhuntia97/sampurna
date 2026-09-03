package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ProductVariantEntity
import com.example.data.local.entity.InventoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getAllProductsFlow(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE is_active = 1 AND status = 'APPROVED' ORDER BY id DESC")
    fun getActiveProductsFlow(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    fun getProductByIdFlow(productId: Long): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: Long): ProductEntity?

    @Query("SELECT * FROM products WHERE is_active = 1 AND status = 'APPROVED' AND tag = :tag ORDER BY id DESC")
    fun getProductsByTagFlow(tag: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category_id = :categoryId AND is_active = 1 AND status = 'APPROVED' ORDER BY id DESC")
    fun getProductsByCategoryFlow(categoryId: Long): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE sub_category_id = :subCategoryId AND is_active = 1 AND status = 'APPROVED' ORDER BY id DESC")
    fun getProductsBySubCategoryFlow(subCategoryId: Long): Flow<List<ProductEntity>>

    // Comprehensive Customer Search across Name, Brand, and Category
    @Query("""
        SELECT p.* FROM products p
        LEFT JOIN categories c ON p.category_id = c.id
        LEFT JOIN sub_categories sc ON p.sub_category_id = sc.id
        WHERE p.is_active = 1 AND p.status = 'APPROVED' AND (
            p.name LIKE '%' || :query || '%' OR
            p.brand LIKE '%' || :query || '%' OR
            c.name LIKE '%' || :query || '%' OR
            sc.name LIKE '%' || :query || '%'
        )
        ORDER BY p.rating DESC
    """)
    fun searchProductsFlow(query: String): Flow<List<ProductEntity>>

    // Filter Query for Category Pages
    @Query("""
        SELECT * FROM products
        WHERE is_active = 1 AND status = 'APPROVED'
        AND (:categoryId IS NULL OR category_id = :categoryId)
        AND (:subCategoryId IS NULL OR sub_category_id = :subCategoryId)
        AND (:brand IS NULL OR brand = :brand)
        AND price <= :maxPrice
        AND rating >= :minRating
        AND (:inStockOnly = 0 OR stock > 0)
        ORDER BY 
            CASE WHEN :sortBy = 'price_low_high' THEN price END ASC,
            CASE WHEN :sortBy = 'price_high_low' THEN price END DESC,
            CASE WHEN :sortBy = 'rating' THEN rating END DESC,
            id DESC
    """)
    fun filterProductsFlow(
        categoryId: Long?,
        subCategoryId: Long?,
        brand: String?,
        maxPrice: Double = 1000000.0,
        minRating: Float = 0.0f,
        inStockOnly: Int = 0,
        sortBy: String = "default"
    ): Flow<List<ProductEntity>>

    @Query("SELECT DISTINCT brand FROM products WHERE (:categoryId IS NULL OR category_id = :categoryId) AND is_active = 1 AND status = 'APPROVED'")
    fun getBrandsForCategoryFlow(categoryId: Long?): Flow<List<String>>

    // Seller Specific Queries
    @Query("SELECT * FROM products WHERE seller_id = :sellerId ORDER BY id DESC")
    fun getProductsBySellerFlow(sellerId: Long): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE seller_id = :sellerId AND status = :status ORDER BY id DESC")
    fun getSellerProductsByStatusFlow(sellerId: Long, status: String): Flow<List<ProductEntity>>

    // Admin Review Queries
    @Query("SELECT * FROM products WHERE status = 'SUBMITTED' ORDER BY updated_at DESC")
    fun getPendingApprovalProductsFlow(): Flow<List<ProductEntity>>

    @Query("UPDATE products SET status = :status, rejection_reason = :reason, is_active = :isActive, updated_at = :timestamp WHERE id = :productId")
    suspend fun updateProductStatus(productId: Long, status: String, reason: String? = null, isActive: Boolean = true, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM products")
    fun getTotalProductsCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM products WHERE is_active = 1 AND status = 'APPROVED'")
    fun getActiveProductsCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM products WHERE status = 'SUBMITTED'")
    fun getPendingProductsCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>): List<Long>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProductById(id: Long)

    @Query("UPDATE products SET is_active = :isActive WHERE id = :id")
    suspend fun toggleProductActive(id: Long, isActive: Boolean)

    @Query("UPDATE products SET price = :price, seller_price = :sellerPrice, updated_at = :timestamp WHERE id = :id")
    suspend fun updateProductPrices(id: Long, sellerPrice: Double, price: Double, timestamp: Long = System.currentTimeMillis())

    // Product Variants
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariants(variants: List<ProductVariantEntity>)

    @Query("SELECT * FROM product_variants WHERE product_id = :productId")
    fun getVariantsForProductFlow(productId: Long): Flow<List<ProductVariantEntity>>

    // Inventory
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateInventory(inventory: InventoryEntity)

    @Query("SELECT * FROM inventory WHERE product_id = :productId LIMIT 1")
    fun getInventoryForProductFlow(productId: Long): Flow<InventoryEntity?>

    @Query("UPDATE products SET stock = :newStock WHERE id = :productId")
    suspend fun updateStock(productId: Long, newStock: Int)
}
