package com.calcmarket.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.calcmarket.data.local.entities.BuyEntity
import com.calcmarket.data.local.entities.FullBuyEntity
import com.calcmarket.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDAO {

    @Insert
    fun saveProduct(buyEntity: List<ProductEntity>)

    @Query("SELECT * FROM product")
    fun getAllBuys(): Flow<ProductEntity>

    @Transaction
    @Query("SELECT * FROM product WHERE fk_buy_id = :idBuy")
    fun getProductsByBuy(idBuy: Int): Flow<ProductEntity>

}