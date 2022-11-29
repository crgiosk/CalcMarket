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
    fun saveProduct(buyEntity: ProductEntity)
    @Query("SELECT product_name FROM product WHERE product_name LIKE '%' || :queryString || '%' ")
    fun getNameProducts(queryString: String): Flow<List<String>>

    @Transaction
    @Query("SELECT * FROM product WHERE fk_buy_id = :idBuy")
    fun getProductsByBuy(idBuy: Int): Flow<ProductEntity>

}