package com.calcmarket.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.OnConflictStrategy
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.ProductsByBuyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveBuy(buyEntity: BuyEntity)

    @Query("SELECT * FROM buy")
    fun getAllLocalBuy(): Flow<List<BuyEntity>>


    @Insert
    fun saveProducts(products: List<ProductsByBuyEntity>)

}