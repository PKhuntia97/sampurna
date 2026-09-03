package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.AddressDao
import com.example.data.local.dao.AdminDao
import com.example.data.local.dao.BannerDao
import com.example.data.local.dao.CartDao
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.DeliveryBoyDao
import com.example.data.local.dao.NotificationDao
import com.example.data.local.dao.OfferDao
import com.example.data.local.dao.OrderDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.dao.RecentlyViewedDao
import com.example.data.local.dao.SellerDao
import com.example.data.local.dao.SubCategoryDao
import com.example.data.local.dao.SystemSettingDao
import com.example.data.local.dao.UserDao
import com.example.data.local.dao.WishlistDao
import com.example.data.local.entity.AddressEntity
import com.example.data.local.entity.AdminUserEntity
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.CategoryBannerEntity
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.CustomerProductCancellationEntity
import com.example.data.local.entity.DeliveryPartnerEntity
import com.example.data.local.entity.InventoryEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.NotificationPreferenceEntity
import com.example.data.local.entity.OfferEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.OrderItemEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ProductVariantEntity
import com.example.data.local.entity.RecentlyViewedEntity
import com.example.data.local.entity.SellerCategoryAssignmentEntity
import com.example.data.local.entity.SellerEntity
import com.example.data.local.entity.SellerProductEntity
import com.example.data.local.entity.SubCategoryEntity
import com.example.data.local.entity.SystemSettingEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.WishlistEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CategoryEntity::class,
        SubCategoryEntity::class,
        CategoryBannerEntity::class,
        ProductEntity::class,
        AdminUserEntity::class,
        UserEntity::class,
        AddressEntity::class,
        WishlistEntity::class,
        RecentlyViewedEntity::class,
        OfferEntity::class,
        SellerEntity::class,
        SellerCategoryAssignmentEntity::class,
        SellerProductEntity::class,
        ProductVariantEntity::class,
        InventoryEntity::class,
        NotificationEntity::class,
        NotificationPreferenceEntity::class,
        SystemSettingEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        CustomerProductCancellationEntity::class,
        DeliveryPartnerEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class SampurnaDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun subCategoryDao(): SubCategoryDao
    abstract fun bannerDao(): BannerDao
    abstract fun productDao(): ProductDao
    abstract fun adminDao(): AdminDao
    abstract fun userDao(): UserDao
    abstract fun addressDao(): AddressDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun recentlyViewedDao(): RecentlyViewedDao
    abstract fun offerDao(): OfferDao
    abstract fun sellerDao(): SellerDao
    abstract fun notificationDao(): NotificationDao
    abstract fun systemSettingDao(): SystemSettingDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun deliveryBoyDao(): DeliveryBoyDao

    companion object {
        @Volatile
        private var INSTANCE: SampurnaDatabase? = null

        fun getDatabase(context: Context): SampurnaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SampurnaDatabase::class.java,
                    "sampurna_ecommerce_db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed default data asynchronously on creation
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.let { database ->
                                DatabaseInitializer.seedDatabase(database)
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
