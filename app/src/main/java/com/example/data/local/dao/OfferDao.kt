package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.OfferEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferDao {

    @Query("SELECT * FROM offers ORDER BY valid_until ASC")
    fun getAllOffersFlow(): Flow<List<OfferEntity>>

    @Query("SELECT * FROM offers WHERE offer_type = :type ORDER BY valid_until ASC")
    fun getOffersByTypeFlow(type: String): Flow<List<OfferEntity>>

    @Query("SELECT * FROM offers WHERE is_expired = 0 AND is_used = 0 ORDER BY valid_until ASC")
    fun getActiveOffersFlow(): Flow<List<OfferEntity>>

    @Query("SELECT * FROM offers WHERE code = :code LIMIT 1")
    suspend fun getOfferByCode(code: String): OfferEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffers(offers: List<OfferEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffer(offer: OfferEntity): Long

    @Update
    suspend fun updateOffer(offer: OfferEntity)

    @Query("SELECT COUNT(*) FROM offers")
    suspend fun getOffersCount(): Int
}
